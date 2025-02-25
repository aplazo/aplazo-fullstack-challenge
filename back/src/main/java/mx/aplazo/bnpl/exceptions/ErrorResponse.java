package mx.aplazo.bnpl.exceptions;

public record ErrorResponse(
        String code,
        String error,
        Long timestamp,
        String message,
        String path
) {
}
