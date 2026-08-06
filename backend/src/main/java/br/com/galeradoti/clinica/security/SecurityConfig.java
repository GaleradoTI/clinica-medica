package br.com.galeradoti.clinica.security;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/refresh",
                        "/api/health",
                        "/actuator/health",
                        "/actuator/info"
                    ).permitAll()
                    .requestMatchers("/api/usuarios/**")
                        .hasRole("ADMIN")
                    .requestMatchers("/api/pacientes/**")
                        .authenticated()
                    .anyRequest()
                        .authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter
                    )
                )
            )
            .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecretKey jwtSecretKey(SecurityProperties properties) {
        byte[] decodedSecret;

        try {
            decodedSecret = Base64
                .getDecoder()
                .decode(properties.jwt().secret());
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                "JWT_SECRET_BASE64 não contém um Base64 válido.",
                exception
            );
        }

        if (decodedSecret.length < 32) {
            throw new IllegalStateException(
                "A chave JWT deve possuir pelo menos 32 bytes."
            );
        }

        return new SecretKeySpec(decodedSecret, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey secretKey) {
        return NimbusJwtEncoder
            .withSecretKey(secretKey)
            .algorithm(MacAlgorithm.HS256)
            .build();
    }

    @Bean
    JwtDecoder jwtDecoder(
        SecretKey secretKey,
        SecurityProperties properties
    ) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();

        decoder.setJwtValidator(
            JwtValidators.createDefaultWithIssuer(
                properties.jwt().issuer()
            )
        );

        return decoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter =
            new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
            authoritiesConverter
        );

        return authenticationConverter;
    }
}