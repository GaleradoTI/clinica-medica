package br.com.galeradoti.clinica.paciente.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(
        String value,
        ConstraintValidatorContext context
    ) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11 || todosDigitosIguais(cpf)) {
            return false;
        }

        int primeiroDigito = calcularDigito(cpf.substring(0, 9), 10);
        int segundoDigito = calcularDigito(cpf.substring(0, 10), 11);

        return primeiroDigito == Character.getNumericValue(cpf.charAt(9))
            && segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    private int calcularDigito(String base, int pesoInicial) {
        int soma = 0;

        for (int indice = 0; indice < base.length(); indice++) {
            int numero = Character.getNumericValue(base.charAt(indice));
            soma += numero * (pesoInicial - indice);
        }

        int resto = soma % 11;

        return resto < 2 ? 0 : 11 - resto;
    }

    private boolean todosDigitosIguais(String cpf) {
        char primeiro = cpf.charAt(0);

        for (int indice = 1; indice < cpf.length(); indice++) {
            if (cpf.charAt(indice) != primeiro) {
                return false;
            }
        }

        return true;
    }
}