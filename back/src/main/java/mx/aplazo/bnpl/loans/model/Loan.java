package mx.aplazo.bnpl.loans.model;

import jakarta.persistence.*;
import mx.aplazo.bnpl.customers.model.Customer;
import mx.aplazo.bnpl.loans.enums.LoanStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private LoanStatus status;

  @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Installment> installments = List.of();

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

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public void setStatus(LoanStatus status) {
    this.status = status;
  }

  public List<Installment> getInstallments() {
    return installments;
  }

  public void setInstallments(List<Installment> installments) {
    this.installments = installments;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
