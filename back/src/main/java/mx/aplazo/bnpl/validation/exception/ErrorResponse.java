package mx.aplazo.bnpl.validation.exception;

public record ErrorResponse(
        String code,
        String error,
        Long timestamp,
        String message,
        String path
) {
}
