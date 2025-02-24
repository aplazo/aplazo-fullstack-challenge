package mx.aplazo.bnpl.customers;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String secondLastName;

  @Column(nullable = false)
  private Date dateOfBirth;

  @Column(nullable = false)
  private double creditLineAmount;

  @Column(nullable = false)
  private double availableCreditLineAmount;

  @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setSecondLastName(String secondLastName) {
    this.secondLastName = secondLastName;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public double getCreditLineAmount() {
    return creditLineAmount;
  }

  public void setCreditLineAmount(double creditLineAmount) {
    this.creditLineAmount = creditLineAmount;
  }

  public double getAvailableCreditLineAmount() {
    return availableCreditLineAmount;
  }

  public void setAvailableCreditLineAmount(double availableCreditLineAmount) {
    this.availableCreditLineAmount = availableCreditLineAmount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

}
