package br.com.galeradoti.clinica.paciente.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CPFValidator.class)
@Target({
    FIELD,
    PARAMETER,
    ANNOTATION_TYPE,
    RECORD_COMPONENT
})
@Retention(RUNTIME)
public @interface CPF {

    String message() default "O CPF informado é inválido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}