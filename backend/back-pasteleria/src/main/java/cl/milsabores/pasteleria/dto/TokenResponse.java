package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponse {
    String token;
    String refreshToken;
    String email;
    String userId;
    String[] roles;
}

