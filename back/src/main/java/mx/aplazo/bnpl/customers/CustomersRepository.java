package mx.aplazo.bnpl.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomersRepository extends JpaRepository<Customer, UUID> {
  Optional<Customer> findById(UUID customerId);
}
