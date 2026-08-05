package br.com.galeradoti.clinica.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
    Jwt jwt,
    Cookie cookie
) {

    public record Jwt(
        String issuer,
        long accessTokenExpirationSeconds,
        long refreshTokenExpirationDays,
        String secret
    ) {
    }

    public record Cookie(
        boolean secure,
        String sameSite
    ) {
    }
}