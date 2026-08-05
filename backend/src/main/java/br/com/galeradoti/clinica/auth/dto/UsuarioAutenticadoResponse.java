package br.com.galeradoti.clinica.auth.dto;

import br.com.galeradoti.clinica.usuario.entity.Usuario;

public record UsuarioAutenticadoResponse(
    Long id,
    String nome,
    String email,
    String perfil,
    boolean ativo
) {

    public static UsuarioAutenticadoResponse from(Usuario usuario) {
        return new UsuarioAutenticadoResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil().name(),
            usuario.estaAtivo()
        );
    }
}