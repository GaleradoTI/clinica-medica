package br.com.galeradoti.clinica.refreshtoken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.galeradoti.clinica.refreshtoken.entity.RefreshToken;

public interface RefreshTokenRepository
    extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}