package mx.aplazo.bnpl.loans;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoansRepository extends JpaRepository<Loan, UUID> {
  Optional<Loan> findById(UUID loanId);
}
