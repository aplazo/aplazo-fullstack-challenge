package mx.aplazo.bnpl.loans;

import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoansService {
  @Autowired
  private LoansRepository loansRepository;

  public LoanResponse create(UUID customerId, CreateLoanRequest createLoanRequest) {
    if (!customerId.equals(createLoanRequest.getCustomerId())) {
      throw new UnauthorizedException("Customer ID does not match authenticated customer");
    }

    Loan loan = new Loan();
    return mapToLoanResponse(loan);
  }

  public LoanResponse findById(UUID customerId, UUID loanId) {
    Loan loan = loansRepository.findById(loanId)
            .orElseThrow(() -> new LoanNotFoundException("Loan with id " + loanId + " not found"));
    if (!customerId.equals(loan.getCustomerId())) {
      throw new UnauthorizedException("Customer ID does not match authenticated customer");
    }
    return mapToLoanResponse(loan);
  }

  private LoanResponse mapToLoanResponse(Loan loan) {
    return new LoanResponse(loan.getId(), loan.getCustomerId(), 0, loan.getCreatedAt());
  }
}
