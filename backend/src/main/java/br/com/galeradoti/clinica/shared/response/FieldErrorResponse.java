package br.com.galeradoti.clinica.shared.response;

public record FieldErrorResponse(
    String field,
    String message
) {
}