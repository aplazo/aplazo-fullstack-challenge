package mx.aplazo.bnpl.loans.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import mx.aplazo.bnpl.validation.Patterns;

import java.util.UUID;

public class CreateLoanRequest {
  @NotNull(message = "Customer id cannot be null")
  @Pattern(regexp = Patterns.UUID_PATTERN, message = "Invalid customer id")
  private UUID customerId;

  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  private double amount;

  public UUID getCustomerId() {
    return customerId;
  }

  public double getAmount() {
    return amount;
  }
}
