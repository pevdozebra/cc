package co.sptnk.service.user;

import co.sptnk.service.user.common.GeneratedCode;
import co.sptnk.service.user.dto.Auth;
import co.sptnk.service.user.services.Impl.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@ActiveProfiles("staging")
public class AuthServiceTest {

    @Autowired
    private AuthService service;

    private GeneratedCode code;
    private final String phone = "79843456740";

    @Test
    public void signIn() {
        Auth auth = new Auth();
        auth.setPhone(phone);
        try {
            code = service.signIn(auth);
            log.info(code.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void singInMultiple() {
        Auth auth = new Auth();
        auth.setPhone(RandomStringUtils.randomNumeric(11));
        assertThrows(ResponseStatusException.class, () -> {
            for (int i = 0; i < 6; i++) {
                GeneratedCode gCode = service.signIn(auth);
                log.info(String.format("Получен код: %s. Всего попыток генерации - %s", gCode, i));
            }
        });

    }

    @Test
    public void testValidate() {
        try {
            service.validate(code.getCode(), phone);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
