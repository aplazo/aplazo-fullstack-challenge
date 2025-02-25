package mx.aplazo.bnpl.validation.annotations;

import org.springframework.web.bind.MethodArgumentNotValidException;

public class ValidationExceptionParser {
  public static String parse(MethodArgumentNotValidException exception) {
    return exception.getBindingResult().getFieldError().getDefaultMessage();
  }
}
