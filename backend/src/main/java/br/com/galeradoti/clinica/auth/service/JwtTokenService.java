package br.com.galeradoti.clinica.auth.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.galeradoti.clinica.security.SecurityProperties;
import br.com.galeradoti.clinica.usuario.entity.Usuario;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;

    public JwtTokenService(
        JwtEncoder jwtEncoder,
        SecurityProperties securityProperties
    ) {
        this.jwtEncoder = jwtEncoder;
        this.securityProperties = securityProperties;
    }

    public String gerarAccessToken(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiracao = agora.plusSeconds(
            securityProperties.jwt().accessTokenExpirationSeconds()
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(securityProperties.jwt().issuer())
            .issuedAt(agora)
            .expiresAt(expiracao)
            .subject(usuario.getId().toString())
            .id(UUID.randomUUID().toString())
            .claim("email", usuario.getEmail())
            .claim("nome", usuario.getNome())
            .claim("perfil", usuario.getPerfil().name())
            .claim(
                "authorities",
                List.of("ROLE_" + usuario.getPerfil().name())
            )
            .build();

        return jwtEncoder
            .encode(JwtEncoderParameters.from(claims))
            .getTokenValue();
    }

    public long expiracaoEmSegundos() {
        return securityProperties
            .jwt()
            .accessTokenExpirationSeconds();
    }
}