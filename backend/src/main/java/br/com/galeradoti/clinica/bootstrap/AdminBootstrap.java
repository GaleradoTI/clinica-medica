package br.com.galeradoti.clinica.bootstrap;

import java.util.Locale;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.galeradoti.clinica.usuario.entity.PerfilUsuario;
import br.com.galeradoti.clinica.usuario.entity.Usuario;
import br.com.galeradoti.clinica.usuario.repository.UsuarioRepository;

@Component
@Profile("dev")
@EnableConfigurationProperties(AdminBootstrapProperties.class)
public class AdminBootstrap implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties properties;

    public AdminBootstrap(
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder,
        AdminBootstrapProperties properties
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!properties.enabled()) {
            return;
        }

        String email = properties
            .email()
            .trim()
            .toLowerCase(Locale.ROOT);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            return;
        }

        Usuario administrador = new Usuario(
            properties.name(),
            email,
            passwordEncoder.encode(properties.password()),
            PerfilUsuario.ADMIN
        );

        usuarioRepository.save(administrador);
    }
}