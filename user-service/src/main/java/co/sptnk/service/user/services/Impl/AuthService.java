package co.sptnk.service.user.services.Impl;

import co.sptnk.service.user.common.GeneratedCode;
import co.sptnk.service.user.common.ValidationType;
import co.sptnk.service.user.common.config.ConfigName;
import co.sptnk.service.user.common.config.ConfigStore;
import co.sptnk.service.user.dto.Auth;
import co.sptnk.service.user.dto.Tokens;
import co.sptnk.service.user.model.Validation;
import co.sptnk.service.user.model.ValidationCode;
import co.sptnk.service.user.model.keys.ValidationPK;
import co.sptnk.service.user.repositories.UsersRepo;
import co.sptnk.service.user.repositories.ValidationCodeRepo;
import co.sptnk.service.user.repositories.ValidationRepo;
import co.sptnk.service.user.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class AuthService implements IAuthService {

    private final UsersRepo usersRepo;

    private final ValidationRepo validationRepo;

    private final ValidationCodeRepo codeRepo;

    @Autowired
    private ConfigStore config;

    public AuthService(UsersRepo usersRepo, ValidationRepo validationRepo, ValidationCodeRepo codeRepo) {
        this.usersRepo = usersRepo;
        this.validationRepo = validationRepo;
        this.codeRepo = codeRepo;
    }

    @Override
    @Transactional
    public GeneratedCode signIn(Auth auth) {
        if (auth.getPhone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
//        User user = usersRepo.findUserByUsername(auth.getPhone())
//                .orElse(null);
//        if (user == null) {
//            //TODO new user
//            user = new User();
//        }
//        if (user.getDeleted() || user.getBlocked()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
        ValidationPK key = new ValidationPK(auth.getPhone(), ValidationType.SING_IN_SMS);
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
    public Tokens validate(String code, String validationId) {
        Tokens tokens = new Tokens();
        Validation vExample = new Validation();
        vExample.setId(new ValidationPK(validationId, null));
        Validation validation = validationRepo
                .findOne(Example.of(vExample, ExampleMatcher.matching().withIgnoreNullValues()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (validation.getCodes().isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        return tokens;
    }
}
