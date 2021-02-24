package co.sptnk.service.user.controllers;

import co.sptnk.service.user.dto.Tokens;
import co.sptnk.service.user.services.Impl.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("validation")
public class ValidationController {

    private final AuthService authService;

    public ValidationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Tokens> validate(@RequestParam String code, @RequestParam String id) {
        if (code.length() > 256) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authService.validate(code, id), HttpStatus.OK);
    }
}
