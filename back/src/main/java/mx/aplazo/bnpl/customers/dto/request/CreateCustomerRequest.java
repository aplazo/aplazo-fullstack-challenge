package mx.aplazo.bnpl.customers.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import mx.aplazo.bnpl.validation.annotations.AgeRange;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CreateCustomerRequest {
  @NotEmpty(message = "First name is required")
  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  private String firstName;

  @NotEmpty(message = "Last name is required")
  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  private String lastName;

  @NotEmpty(message = "Second last name is required")
  @Size(min = 2, max = 50, message = "Second last name must be between 2 and 50 characters")
  private String secondLastName;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past(message = "Date of birth must be in the past")
  @AgeRange(min = 18, max = 65)
  private Date dateOfBirth;


  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getSecondLastName() {
    return secondLastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }
}

