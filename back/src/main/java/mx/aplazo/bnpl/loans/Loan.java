package mx.aplazo.bnpl.loans;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loans")
public class Loan {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private UUID customerId;

  @Column(nullable = false, columnDefinition = "TIMESTAMPTZ")
  private Instant createdAt;

  public UUID getId() {
    return id;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
