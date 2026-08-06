package br.com.galeradoti.clinica.paciente.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import br.com.galeradoti.clinica.paciente.entity.Paciente;
import br.com.galeradoti.clinica.paciente.entity.Sexo;

public record PacienteResponse(
    Long id,
    String nomeCompleto,
    String cpf,
    LocalDate dataNascimento,
    Sexo sexo,
    String telefone,
    String telefoneSecundario,
    String email,
    String nomeMae,
    EnderecoResponse endereco,
    String observacoes,
    boolean ativo,
    OffsetDateTime criadoEm,
    OffsetDateTime atualizadoEm
) {

    public static PacienteResponse from(Paciente paciente) {
        EnderecoResponse endereco = new EnderecoResponse(
            paciente.getCep(),
            paciente.getLogradouro(),
            paciente.getNumero(),
            paciente.getComplemento(),
            paciente.getBairro(),
            paciente.getCidade(),
            paciente.getEstado()
        );

        return new PacienteResponse(
            paciente.getId(),
            paciente.getNomeCompleto(),
            paciente.getCpf(),
            paciente.getDataNascimento(),
            paciente.getSexo(),
            paciente.getTelefone(),
            paciente.getTelefoneSecundario(),
            paciente.getEmail(),
            paciente.getNomeMae(),
            endereco,
            paciente.getObservacoes(),
            paciente.estaAtivo(),
            paciente.getCriadoEm(),
            paciente.getAtualizadoEm()
        );
    }

    public record EnderecoResponse(
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado
    ) {
    }
}