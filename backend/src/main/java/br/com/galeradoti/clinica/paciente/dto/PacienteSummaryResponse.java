package br.com.galeradoti.clinica.paciente.dto;

import java.time.LocalDate;

import br.com.galeradoti.clinica.paciente.entity.Paciente;
import br.com.galeradoti.clinica.paciente.entity.Sexo;

public record PacienteSummaryResponse(
    Long id,
    String nomeCompleto,
    String cpf,
    LocalDate dataNascimento,
    Sexo sexo,
    String telefone,
    String email,
    boolean ativo
) {

    public static PacienteSummaryResponse from(Paciente paciente) {
        return new PacienteSummaryResponse(
            paciente.getId(),
            paciente.getNomeCompleto(),
            paciente.getCpf(),
            paciente.getDataNascimento(),
            paciente.getSexo(),
            paciente.getTelefone(),
            paciente.getEmail(),
            paciente.estaAtivo()
        );
    }
}