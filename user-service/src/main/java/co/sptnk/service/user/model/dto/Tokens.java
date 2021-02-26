package co.sptnk.service.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Tokens {
    private UUID id;
    private String accessToken;
    private String refreshToken;
    private Boolean isNew;
}
