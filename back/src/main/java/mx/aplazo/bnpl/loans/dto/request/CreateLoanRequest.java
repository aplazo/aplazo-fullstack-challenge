package mx.aplazo.bnpl.loans.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class CreateLoanRequest {
  @NotNull(message = "Customer id cannot be null")
  @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid customer id")
  private String customerId;

  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  private double amount;

  public UUID getCustomerId() {
    return UUID.fromString(customerId);
  }

  public double getAmount() {
    return amount;
  }
}
