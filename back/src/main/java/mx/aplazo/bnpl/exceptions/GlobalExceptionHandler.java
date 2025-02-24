package mx.aplazo.bnpl.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import mx.aplazo.bnpl.validation.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> invalidBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_REQUEST;
    return ErrorResponseBuilder.toResponse("Invalid body", request, error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleArgumentMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_REQUEST;
    String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
    return ErrorResponseBuilder.toResponse(message, request, error);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.UNAUTHORIZED;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INTERNAL_SERVER_ERROR;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }
}
