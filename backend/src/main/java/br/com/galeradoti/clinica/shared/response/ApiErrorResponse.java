package br.com.galeradoti.clinica.shared.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String traceId,
    List<FieldErrorResponse> fieldErrors
) {
}