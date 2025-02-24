package mx.aplazo.bnpl.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import mx.aplazo.bnpl.validation.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder {
  public static ResponseEntity<ErrorResponse> toResponse(String message, HttpServletRequest request, ErrorCode error) {
    ErrorResponse body = new ErrorResponse(error.getCode(), error.getError(), System.currentTimeMillis(), message, request.getRequestURI());
    return ResponseEntity.status(error.getStatus())
            .body(body);
  }
}
