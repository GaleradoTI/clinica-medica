package br.com.galeradoti.clinica.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.galeradoti.clinica.usuario.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}