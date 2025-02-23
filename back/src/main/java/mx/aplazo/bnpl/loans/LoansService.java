package mx.aplazo.bnpl.loans;

import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoansService {
  @Autowired
  private LoansRepository loansRepository;

  public LoanResponse create(CreateLoanRequest createLoanRequest) {
    Loan loan = new Loan();
    return mapToLoanResponse(loan);
  }

  public LoanResponse findById(UUID loanId) {
    Loan loan = loansRepository.findById(loanId)
            .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
    return mapToLoanResponse(loan);
  }

  private LoanResponse mapToLoanResponse(Loan loan) {
    return new LoanResponse(loan.getId(), loan.getCustomerId(), 0, loan.getCreatedAt());
  }
}
