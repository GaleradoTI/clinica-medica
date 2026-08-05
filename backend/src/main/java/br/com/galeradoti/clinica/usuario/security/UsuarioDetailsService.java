package br.com.galeradoti.clinica.usuario.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.galeradoti.clinica.usuario.entity.Usuario;
import br.com.galeradoti.clinica.usuario.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository
            .findByEmailIgnoreCase(email.trim())
            .orElseThrow(() ->
                new UsernameNotFoundException("Credenciais inválidas.")
            );

        return User
            .withUsername(usuario.getEmail())
            .password(usuario.getSenha())
            .authorities("ROLE_" + usuario.getPerfil().name())
            .disabled(!usuario.estaAtivo())
            .build();
    }
}