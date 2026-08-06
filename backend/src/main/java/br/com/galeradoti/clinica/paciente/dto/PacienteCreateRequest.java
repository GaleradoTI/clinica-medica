package br.com.galeradoti.clinica.paciente.dto;

import java.time.LocalDate;

import br.com.galeradoti.clinica.paciente.entity.Sexo;
import br.com.galeradoti.clinica.paciente.validation.CPF;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record PacienteCreateRequest(

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(
        max = 180,
        message = "O nome completo deve possuir no máximo 180 caracteres."
    )
    String nomeCompleto,

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF
    String cpf,

    @NotNull(message = "A data de nascimento é obrigatória.")
    @PastOrPresent(
        message = "A data de nascimento não pode ser futura."
    )
    LocalDate dataNascimento,

    Sexo sexo,

    @NotBlank(message = "O telefone é obrigatório.")
    @Size(
        max = 20,
        message = "O telefone deve possuir no máximo 20 caracteres."
    )
    String telefone,

    @Size(
        max = 20,
        message = "O telefone secundário deve possuir no máximo 20 caracteres."
    )
    String telefoneSecundario,

    @Email(message = "O e-mail informado é inválido.")
    @Size(
        max = 180,
        message = "O e-mail deve possuir no máximo 180 caracteres."
    )
    String email,

    @Size(
        max = 180,
        message = "O nome da mãe deve possuir no máximo 180 caracteres."
    )
    String nomeMae,

    @Valid
    EnderecoRequest endereco,

    @Size(
        max = 5000,
        message = "As observações devem possuir no máximo 5000 caracteres."
    )
    String observacoes

) {
}