package co.sptnk.service.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Tokens {
    private UUID id;
    private String accessToken;
    private String refreshToken;
    private Boolean isNew;
}
