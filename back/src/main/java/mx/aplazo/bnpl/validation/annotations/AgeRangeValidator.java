package mx.aplazo.bnpl.validation.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AgeRangeValidator implements ConstraintValidator<AgeRange, Date> {
  private int minAge;
  private int maxAge;

  @Override
  public void initialize(AgeRange constraintAnnotation) {
    this.minAge = constraintAnnotation.min();
    this.maxAge = constraintAnnotation.max();
  }

  @Override
  public boolean isValid(Date dateOfBirth, ConstraintValidatorContext context) {
    if (dateOfBirth == null) {
      return false;
    }

    LocalDate dob = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate today = LocalDate.now();

    int age = today.getYear() - dob.getYear();
    // TODO: what if the birthday hasn't happened yet?
    return age >= minAge && age <= maxAge;
  }
}
