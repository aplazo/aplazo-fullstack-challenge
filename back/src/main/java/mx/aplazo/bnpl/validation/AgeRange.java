package mx.aplazo.bnpl.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {
  String message() default "You must be between {min} and {max} years old";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  int min();
  int max();
}
