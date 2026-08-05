package br.com.galeradoti.clinica.auth.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.galeradoti.clinica.auth.dto.LoginRequest;
import br.com.galeradoti.clinica.auth.dto.LoginResponse;
import br.com.galeradoti.clinica.auth.dto.RefreshResponse;
import br.com.galeradoti.clinica.auth.dto.UsuarioAutenticadoResponse;
import br.com.galeradoti.clinica.usuario.entity.Usuario;
import br.com.galeradoti.clinica.usuario.repository.UsuarioRepository;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
        AuthenticationManager authenticationManager,
        UsuarioRepository usuarioRepository,
        JwtTokenService jwtTokenService,
        RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public LoginResult login(
        LoginRequest request,
        String ipOrigem,
        String userAgent
    ) {
        String email = normalizarEmail(request.email());

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                email,
                request.senha()
            )
        );

        Usuario usuario = usuarioRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow();

        usuario.registrarLogin();

        String accessToken =
            jwtTokenService.gerarAccessToken(usuario);

        RefreshTokenService.GeneratedRefreshToken refreshToken =
            refreshTokenService.criar(
                usuario,
                ipOrigem,
                userAgent
            );

        LoginResponse response = new LoginResponse(
            accessToken,
            "Bearer",
            jwtTokenService.expiracaoEmSegundos(),
            UsuarioAutenticadoResponse.from(usuario)
        );

        return new LoginResult(
            response,
            refreshToken.token(),
            refreshToken.expiraEm()
        );
    }

    @Transactional
    public RefreshResult refresh(
        String token,
        String ipOrigem,
        String userAgent
    ) {
        RefreshTokenService.RotatedRefreshToken rotatedToken =
            refreshTokenService.rotacionar(
                token,
                ipOrigem,
                userAgent
            );

        String accessToken = jwtTokenService
            .gerarAccessToken(rotatedToken.usuario());

        RefreshResponse response = new RefreshResponse(
            accessToken,
            "Bearer",
            jwtTokenService.expiracaoEmSegundos()
        );

        return new RefreshResult(
            response,
            rotatedToken.token(),
            rotatedToken.expiraEm()
        );
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revogar(refreshToken);
    }

    @Transactional(readOnly = true)
    public UsuarioAutenticadoResponse me(Long usuarioId) {
        Usuario usuario = usuarioRepository
            .findById(usuarioId)
            .orElseThrow();

        return UsuarioAutenticadoResponse.from(usuario);
    }

    private String normalizarEmail(String email) {
        return email
            .trim()
            .toLowerCase(Locale.ROOT);
    }

    public record LoginResult(
        LoginResponse response,
        String refreshToken,
        java.time.OffsetDateTime refreshTokenExpiresAt
    ) {
    }

    public record RefreshResult(
        RefreshResponse response,
        String refreshToken,
        java.time.OffsetDateTime refreshTokenExpiresAt
    ) {
    }
}