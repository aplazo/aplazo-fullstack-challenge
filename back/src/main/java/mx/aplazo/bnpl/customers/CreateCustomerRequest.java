package mx.aplazo.bnpl.customers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import mx.aplazo.bnpl.validation.AgeRange;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CreateCustomerRequest {
  @NotBlank(message = "First name is mandatory")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  private String firstName;

  @NotBlank(message = "Last name is mandatory")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  private String lastName;

  @NotBlank(message = "Second last name is mandatory")
  @Size(min = 2, max = 50, message = "Second last name must be between 2 and 50 characters")
  private String secondLastName;

  @NotBlank(message = "Date of birth is mandatory")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past(message = "Date of birth must be in the past")
  @AgeRange(min = 18, max = 65)
  private Date dateOfBirth;
}

