package br.com.galeradoti.clinica.refreshtoken.entity;

import java.time.OffsetDateTime;

import br.com.galeradoti.clinica.usuario.entity.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash;

    @Column(name = "expira_em", nullable = false)
    private OffsetDateTime expiraEm;

    @Column(name = "revogado_em")
    private OffsetDateTime revogadoEm;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    protected RefreshToken() {
    }

    public RefreshToken(
        Usuario usuario,
        String tokenHash,
        OffsetDateTime expiraEm,
        String ipOrigem,
        String userAgent
    ) {
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.expiraEm = expiraEm;
        this.ipOrigem = ipOrigem;
        this.userAgent = userAgent;
        this.criadoEm = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public OffsetDateTime getExpiraEm() {
        return expiraEm;
    }

    public OffsetDateTime getRevogadoEm() {
        return revogadoEm;
    }

    public boolean estaExpirado() {
        return OffsetDateTime.now().isAfter(expiraEm);
    }

    public boolean estaRevogado() {
        return revogadoEm != null;
    }

    public void revogar() {
        if (this.revogadoEm == null) {
            this.revogadoEm = OffsetDateTime.now();
        }
    }
}