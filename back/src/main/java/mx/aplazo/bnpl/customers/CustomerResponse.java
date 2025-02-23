package mx.aplazo.bnpl.customers;

import java.time.Instant;
import java.util.UUID;

public class CustomerResponse {
  private UUID id;
  private double creditLineAmount;
  private double availableCreditLineAmount;
  private Instant createdAt;

  public CustomerResponse(UUID id, double creditLineAmount, double availableCreditLineAmount, Instant createdAt) {
    this.id = id;
    this.creditLineAmount = creditLineAmount;
    this.availableCreditLineAmount = availableCreditLineAmount;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public double getCreditLineAmount() {
    return creditLineAmount;
  }

  public double getAvailableCreditLineAmount() {
    return availableCreditLineAmount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
