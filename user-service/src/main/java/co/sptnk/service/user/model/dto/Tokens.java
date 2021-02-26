package co.sptnk.service.user.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Tokens {
    private final UUID id;
    private final String accessToken;
    private final String refreshToken;
    private final Boolean isNew;
}
