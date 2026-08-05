package br.com.galeradoti.clinica.auth.dto;

public record RefreshResponse(
    String accessToken,
    String tokenType,
    long expiresIn
) {
}