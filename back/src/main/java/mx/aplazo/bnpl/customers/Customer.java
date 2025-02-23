package mx.aplazo.bnpl.customers;

import jakarta.persistence.*;

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
}
