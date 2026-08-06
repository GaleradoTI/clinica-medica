package br.com.galeradoti.clinica.paciente.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(

    @Pattern(
        regexp = "^\\d{8}$",
        message = "O CEP deve possuir oito números."
    )
    String cep,

    @Size(
        max = 180,
        message = "O logradouro deve possuir no máximo 180 caracteres."
    )
    String logradouro,

    @Size(
        max = 20,
        message = "O número deve possuir no máximo 20 caracteres."
    )
    String numero,

    @Size(
        max = 100,
        message = "O complemento deve possuir no máximo 100 caracteres."
    )
    String complemento,

    @Size(
        max = 100,
        message = "O bairro deve possuir no máximo 100 caracteres."
    )
    String bairro,

    @Size(
        max = 100,
        message = "A cidade deve possuir no máximo 100 caracteres."
    )
    String cidade,

    @Pattern(
        regexp = "^[A-Za-z]{2}$",
        message = "O estado deve possuir duas letras."
    )
    String estado

) {
}