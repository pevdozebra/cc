package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.GeneratedCode;
import co.sptnk.service.user.common.KeycloakProvider;
import co.sptnk.service.user.common.ValidationType;
import co.sptnk.service.user.common.config.ConfigName;
import co.sptnk.service.user.common.config.ConfigStore;
import co.sptnk.service.user.model.dto.Auth;
import co.sptnk.service.user.model.dto.Tokens;
import co.sptnk.service.user.model.User;
import co.sptnk.service.user.model.Validation;
import co.sptnk.service.user.model.ValidationCode;
import co.sptnk.service.user.model.keys.ValidationPK;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.repositories.ValidationCodeRepo;
import co.sptnk.service.user.repositories.ValidationRepo;
import co.sptnk.service.user.services.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService implements IAuthService {

    private final UsersRepo usersRepo;

    private final ValidationRepo validationRepo;

    private final ValidationCodeRepo codeRepo;

    private final RealmResource keyCloak;

    private final KeycloakProvider provider;

    @Autowired
    private ConfigStore config;

    @Autowired
    private ModelMapper mapper;



    public AuthService(UsersRepo usersRepo, ValidationRepo validationRepo,
                       ValidationCodeRepo codeRepo, Environment environment,
                       @Lazy KeycloakProvider provider) {
        this.usersRepo = usersRepo;
        this.validationRepo = validationRepo;
        this.codeRepo = codeRepo;
        this.provider = provider;
        this.keyCloak = provider.get().realm(environment.getProperty("keycloak.realm"));
    }

    @Override
    @Transactional
    public GeneratedCode signIn(Auth auth) {
        if (auth.getPhone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = usersRepo.findUserByUsername(auth.getPhone())
                .orElse(null);
        ValidationPK key;
        if (user == null) {
            key = new ValidationPK(auth.getPhone(), ValidationType.SIGN_UP_SMS);
        } else {
            key = new ValidationPK(auth.getPhone(), ValidationType.SIGN_IN_SMS);
        }

        Validation validation = validationRepo
                .findById(key)
                .orElse(new Validation());
        if (validation.getId() == null) {
            validation.setId(key);
            validation.setSendCount(1);
            validation.setLastSendDate(OffsetDateTime.now());
        } else {
            if (validation.getSendCount() < config.getConfig(ConfigName.VALIDATION_SMS_SEND_MAX, Integer.class)) {
                validation.setSendCount(validation.getSendCount() + 1);
            } else {
                if (Duration.between(OffsetDateTime.now(ZoneOffset.UTC), validation.getLastSendDate())
                        .compareTo(config.getConfig(ConfigName.VALIDATION_SMS_SEND_BLOCK_PERIOD, Duration.class)) > 0) {
                    validation.setSendCount(1);
                    validation.setLastSendDate(OffsetDateTime.now());
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }
        }
        log.info(String.format("Отправлено %s кодов для %s", validation.getSendCount(), validation.getId().getId()));
        ValidationCode validationCode = new ValidationCode();
        GeneratedCode code;
        if (config.getConfig(ConfigName.VALIDATION_SMS_TEST_MODE, Boolean.class)) {
            code = new GeneratedCode(config.getConfig(ConfigName.VALIDATION_SMS_TEST_CODE, String.class));
        } else {
            code = new GeneratedCode(config.getConfig(ConfigName.VALIDATION_SMS_CODE_LENGTH, Integer.class));
        }
        validationCode.setValue(code.hash());
        validationCode.setIssueDate(OffsetDateTime.now());
        validationCode.setExpireDate(OffsetDateTime.now()
                .plus(config.getConfig(ConfigName.VALIDATION_SMS_TIMEOUT, Duration.class)));

        codeRepo.save(validationCode);
        validation.addCode(validationCode);
        validationRepo.save(validation);
        return code;
    }

    @Override
    @Transactional
    public Tokens validate(String code, String validationId) {
        Tokens tokens;
        ValidationType type;
        User user = usersRepo.findUserByUsername(validationId)
                .orElse(null);
        if (user == null) {
            user = createUser(validationId);
            type = ValidationType.SIGN_UP_SMS;
        } else {
            if (user.getDeleted() || user.getBlocked()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            type = ValidationType.SIGN_IN_SMS;
        }
        log.info(String.format("Проверка кода валидации для типа %s", type.getName()));
        GeneratedCode gCode = new GeneratedCode(code);
        Validation validation = validationRepo
                .findById(new ValidationPK(validationId, type))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (validation.getCodes().isEmpty()) {
            log.info(String.format("Для пользователя %s не наден код валидации для типа %s", validationId, type.getName()));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<ValidationCode> codes = validation.getCodes().stream()
                .filter(c -> c.getValue().equals(gCode.hash())).collect(Collectors.toList());
        if (codes.isEmpty()) {
            log.info(String.format("Код валидации %s не найден", code));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            ValidationCode value = codes.stream().findFirst().get();
            if(Duration.between(value.getExpireDate(),OffsetDateTime.now(ZoneOffset.UTC)).getSeconds() > 0) {
                validation.deleteCode(value);
                log.info(String.format("Код %s просрочен", code));
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        validation.setSendCount(0);
        AccessTokenResponse response = provider.getAccessTokenForUser(user.getId().toString());
        tokens = Tokens.builder()
                .id(user.getId())
                .accessToken(response.getToken())
                .refreshToken(response.getRefreshToken())
                .isNew(type == ValidationType.SIGN_UP_SMS)
                .build();
        return tokens;
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setBlocked(false);
        user.setDeleted(false);
        UserRepresentation representation = mapper.map(user, UserRepresentation.class);
        representation.setEnabled(!user.getBlocked());
        Response response = keyCloak.users().create(representation);
        String userId = CreatedResponseUtil.getCreatedId(response);
        user.setId(UUID.fromString(userId));
        usersRepo.save(user);
        return user;
    }
}
