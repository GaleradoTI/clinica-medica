package br.com.galeradoti.clinica.shared.exception;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.galeradoti.clinica.shared.response.ApiErrorResponse;
import br.com.galeradoti.clinica.shared.response.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
        ResourceNotFoundException exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.NOT_FOUND,
            "RESOURCE_NOT_FOUND",
            exception.getMessage(),
            request,
            List.of()
        );
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(
        ResourceConflictException exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.CONFLICT,
            "RESOURCE_CONFLICT",
            exception.getMessage(),
            request,
            List.of()
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(
        BusinessException exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.UNPROCESSABLE_ENTITY,
            "BUSINESS_RULE_VIOLATION",
            exception.getMessage(),
            request,
            List.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        List<FieldErrorResponse> fieldErrors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error ->
                new FieldErrorResponse(
                    error.getField(),
                    error.getDefaultMessage()
                )
            )
            .toList();

        return build(
            HttpStatus.BAD_REQUEST,
            "VALIDATION_ERROR",
            "Existem campos inválidos.",
            request,
            fieldErrors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
        Exception exception,
        HttpServletRequest request
    ) {
        return build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_SERVER_ERROR",
            "Ocorreu um erro inesperado.",
            request,
            List.of()
        );
    }

    private ResponseEntity<ApiErrorResponse> build(
        HttpStatus status,
        String error,
        String message,
        HttpServletRequest request,
        List<FieldErrorResponse> fieldErrors
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
            OffsetDateTime.now(ZoneOffset.UTC),
            status.value(),
            error,
            message,
            request.getRequestURI(),
            UUID.randomUUID().toString(),
            fieldErrors
        );

        return ResponseEntity
            .status(status)
            .body(response);
    }
}