package mx.aplazo.bnpl.loans.model;

import jakarta.persistence.*;
import mx.aplazo.bnpl.loans.enums.LoanStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private LoanStatus status;

  @Column(nullable = false)
  private UUID customerId;

  @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public LoanStatus getStatus() {
    return status;
  }

  public void setStatus(LoanStatus status) {
    this.status = status;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
