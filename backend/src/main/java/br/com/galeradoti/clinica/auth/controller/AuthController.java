package br.com.galeradoti.clinica.auth.controller;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.galeradoti.clinica.auth.dto.LoginRequest;
import br.com.galeradoti.clinica.auth.dto.LoginResponse;
import br.com.galeradoti.clinica.auth.dto.RefreshResponse;
import br.com.galeradoti.clinica.auth.dto.UsuarioAutenticadoResponse;
import br.com.galeradoti.clinica.auth.service.AuthService;
import br.com.galeradoti.clinica.security.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refreshToken";

    private final AuthService authService;
    private final SecurityProperties securityProperties;

    public AuthController(
        AuthService authService,
        SecurityProperties securityProperties
    ) {
        this.authService = authService;
        this.securityProperties = securityProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest
    ) {
        AuthService.LoginResult result = authService.login(
            request,
            obterIp(httpRequest),
            httpRequest.getHeader(HttpHeaders.USER_AGENT)
        );

        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE,
                criarRefreshCookie(
                    result.refreshToken(),
                    result.refreshTokenExpiresAt()
                ).toString()
            )
            .body(result.response());
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
        @CookieValue(
            name = REFRESH_COOKIE,
            required = false
        ) String refreshToken,
        HttpServletRequest httpRequest
    ) {
        AuthService.RefreshResult result = authService.refresh(
            refreshToken,
            obterIp(httpRequest),
            httpRequest.getHeader(HttpHeaders.USER_AGENT)
        );

        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE,
                criarRefreshCookie(
                    result.refreshToken(),
                    result.refreshTokenExpiresAt()
                ).toString()
            )
            .body(result.response());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @CookieValue(
            name = REFRESH_COOKIE,
            required = false
        ) String refreshToken
    ) {
        authService.logout(refreshToken);

        return ResponseEntity
            .noContent()
            .header(
                HttpHeaders.SET_COOKIE,
                removerRefreshCookie().toString()
            )
            .build();
    }

    @GetMapping("/me")
    public UsuarioAutenticadoResponse me(
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long usuarioId = Long.valueOf(jwt.getSubject());

        return authService.me(usuarioId);
    }

    private ResponseCookie criarRefreshCookie(
        String token,
        OffsetDateTime expiracao
    ) {
        Duration maxAge = Duration.between(
            OffsetDateTime.now(ZoneOffset.UTC),
            expiracao.withOffsetSameInstant(ZoneOffset.UTC)
        );

        return ResponseCookie
            .from(REFRESH_COOKIE, token)
            .httpOnly(true)
            .secure(securityProperties.cookie().secure())
            .sameSite(securityProperties.cookie().sameSite())
            .path("/api/auth")
            .maxAge(maxAge)
            .build();
    }

    private ResponseCookie removerRefreshCookie() {
        return ResponseCookie
            .from(REFRESH_COOKIE, "")
            .httpOnly(true)
            .secure(securityProperties.cookie().secure())
            .sameSite(securityProperties.cookie().sameSite())
            .path("/api/auth")
            .maxAge(Duration.ZERO)
            .build();
    }

    private String obterIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}