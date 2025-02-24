package mx.aplazo.bnpl.loans.model;

import jakarta.persistence.*;
import mx.aplazo.bnpl.loans.enums.InstallmentStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "installments")
public class Installment {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "loan_id", nullable = false)
  private Loan loan;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
  private Instant scheduledPaymentDate;

  @Column(nullable = false)
  private InstallmentStatus status;

  @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public void setLoan(Loan loan) {
    this.loan = loan;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Instant getScheduledPaymentDate() {
    return scheduledPaymentDate;
  }

  public void setScheduledPaymentDate(Instant scheduledPaymentDate) {
    this.scheduledPaymentDate = scheduledPaymentDate;
  }

  public InstallmentStatus getStatus() {
    return status;
  }

  public void setStatus(InstallmentStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}