package br.com.galeradoti.clinica.paciente.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "nome_completo",
        nullable = false,
        length = 180
    )
    private String nomeCompleto;

    @Column(
        nullable = false,
        unique = true,
        length = 11
    )
    private String cpf;

    @Column(
        name = "data_nascimento",
        nullable = false
    )
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Sexo sexo;

    @Column(
        nullable = false,
        length = 20
    )
    private String telefone;

    @Column(
        name = "telefone_secundario",
        length = 20
    )
    private String telefoneSecundario;

    @Column(length = 180)
    private String email;

    @Column(
        name = "nome_mae",
        length = 180
    )
    private String nomeMae;

    @Column(length = 8)
    private String cep;

    @Column(length = 180)
    private String logradouro;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(
        name = "criado_em",
        nullable = false
    )
    private OffsetDateTime criadoEm;

    @Column(name = "criado_por")
    private Long criadoPor;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;

    protected Paciente() {
    }

    public Paciente(
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        Sexo sexo,
        String telefone,
        String telefoneSecundario,
        String email,
        String nomeMae,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String observacoes,
        Long criadoPor
    ) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.telefone = telefone;
        this.telefoneSecundario = telefoneSecundario;
        this.email = email;
        this.nomeMae = nomeMae;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.observacoes = observacoes;
        this.ativo = true;
        this.criadoEm = OffsetDateTime.now(ZoneOffset.UTC);
        this.criadoPor = criadoPor;
    }

    public void atualizar(
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        Sexo sexo,
        String telefone,
        String telefoneSecundario,
        String email,
        String nomeMae,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String observacoes,
        Long atualizadoPor
    ) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.telefone = telefone;
        this.telefoneSecundario = telefoneSecundario;
        this.email = email;
        this.nomeMae = nomeMae;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.observacoes = observacoes;
        this.atualizadoEm = OffsetDateTime.now(ZoneOffset.UTC);
        this.atualizadoPor = atualizadoPor;
    }

    public void ativar(Long usuarioId) {
        this.ativo = true;
        registrarAtualizacao(usuarioId);
    }

    public void desativar(Long usuarioId) {
        this.ativo = false;
        registrarAtualizacao(usuarioId);
    }

    private void registrarAtualizacao(Long usuarioId) {
        this.atualizadoEm = OffsetDateTime.now(ZoneOffset.UTC);
        this.atualizadoPor = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public Long getCriadoPor() {
        return criadoPor;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public Long getAtualizadoPor() {
        return atualizadoPor;
    }

    public boolean estaAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
}