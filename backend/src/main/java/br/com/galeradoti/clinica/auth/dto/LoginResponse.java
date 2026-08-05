package br.com.galeradoti.clinica.auth.dto;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    UsuarioAutenticadoResponse usuario
) {
}