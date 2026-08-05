package br.com.galeradoti.clinica.usuario.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PerfilUsuario perfil;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "ultimo_login_em")
    private OffsetDateTime ultimoLoginEm;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "criado_por")
    private Long criadoPor;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    protected Usuario() {
    }

    public Usuario(
        String nome,
        String email,
        String senha,
        PerfilUsuario perfil
    ) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.ativo = true;
        this.criadoEm = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public OffsetDateTime getUltimoLoginEm() {
        return ultimoLoginEm;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void registrarLogin() {
        this.ultimoLoginEm = OffsetDateTime.now();
    }

    public void atualizarSenha(String senha) {
        this.senha = senha;
        this.atualizadoEm = OffsetDateTime.now();
    }

    public boolean estaAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
}