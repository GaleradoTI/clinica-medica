package br.com.galeradoti.clinica.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.galeradoti.clinica.auth.exception.InvalidRefreshTokenException;
import br.com.galeradoti.clinica.refreshtoken.entity.RefreshToken;
import br.com.galeradoti.clinica.refreshtoken.repository.RefreshTokenRepository;
import br.com.galeradoti.clinica.security.SecurityProperties;
import br.com.galeradoti.clinica.usuario.entity.Usuario;

@Service
public class RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;

    public RefreshTokenService(
        RefreshTokenRepository refreshTokenRepository,
        SecurityProperties securityProperties
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.securityProperties = securityProperties;
    }

    @Transactional
    public GeneratedRefreshToken criar(
        Usuario usuario,
        String ipOrigem,
        String userAgent
    ) {
        String token = gerarTokenSeguro();
        String hash = calcularHash(token);

        OffsetDateTime expiracao = OffsetDateTime
            .now()
            .plusDays(
                securityProperties
                    .jwt()
                    .refreshTokenExpirationDays()
            );

        RefreshToken refreshToken = new RefreshToken(
            usuario,
            hash,
            expiracao,
            limitar(ipOrigem, 45),
            limitar(userAgent, 500)
        );

        refreshTokenRepository.save(refreshToken);

        return new GeneratedRefreshToken(token, expiracao);
    }

    @Transactional
    public RotatedRefreshToken rotacionar(
        String token,
        String ipOrigem,
        String userAgent
    ) {
        RefreshToken refreshToken = buscarTokenValido(token);

        refreshToken.revogar();

        GeneratedRefreshToken novoToken = criar(
            refreshToken.getUsuario(),
            ipOrigem,
            userAgent
        );

        return new RotatedRefreshToken(
            refreshToken.getUsuario(),
            novoToken.token(),
            novoToken.expiraEm()
        );
    }

    @Transactional
    public void revogar(String token) {
        if (token == null || token.isBlank()) {
            return;
        }

        refreshTokenRepository
            .findByTokenHash(calcularHash(token))
            .ifPresent(RefreshToken::revogar);
    }

    private RefreshToken buscarTokenValido(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidRefreshTokenException(
                "Refresh token não informado."
            );
        }

        RefreshToken refreshToken = refreshTokenRepository
            .findByTokenHash(calcularHash(token))
            .orElseThrow(() ->
                new InvalidRefreshTokenException(
                    "Refresh token inválido."
                )
            );

        if (refreshToken.estaRevogado()) {
            throw new InvalidRefreshTokenException(
                "Refresh token revogado."
            );
        }

        if (refreshToken.estaExpirado()) {
            throw new InvalidRefreshTokenException(
                "Refresh token expirado."
            );
        }

        if (!refreshToken.getUsuario().estaAtivo()) {
            throw new InvalidRefreshTokenException(
                "Usuário inativo."
            );
        }

        return refreshToken;
    }

    private String gerarTokenSeguro() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }

    private String calcularHash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                token.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                "SHA-256 não está disponível.",
                exception
            );
        }
    }

    private String limitar(String valor, int tamanho) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.length() <= tamanho
            ? valor
            : valor.substring(0, tamanho);
    }

    public record GeneratedRefreshToken(
        String token,
        OffsetDateTime expiraEm
    ) {
    }

    public record RotatedRefreshToken(
        Usuario usuario,
        String token,
        OffsetDateTime expiraEm
    ) {
    }
}