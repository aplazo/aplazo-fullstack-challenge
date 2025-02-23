package mx.aplazo.bnpl.validation.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)

  public ResponseEntity<?> invalidBody(HttpServletRequest request) {
    // TODO: add code and error
    ErrorResponse response = new ErrorResponse(
            "???",
            "INVALID_REQUEST",
            System.currentTimeMillis(),
            "Invalid request body",
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
    String readableMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .reduce("", (acc, error) -> acc + error + "\n");

    // TODO: add code and error
    ErrorResponse response = new ErrorResponse(
            "???",
            "INVALID_REQUEST",
            System.currentTimeMillis(),
            readableMessage,
            request.getRequestURI()
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}

