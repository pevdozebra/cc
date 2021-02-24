package co.sptnk.service.user.services;

import co.sptnk.service.user.common.GeneratedCode;
import co.sptnk.service.user.dto.Auth;
import co.sptnk.service.user.dto.Tokens;

public interface IAuthService {

    GeneratedCode signIn(Auth auth);
    Tokens validate(String code, String validationId);
}
