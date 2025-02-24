package mx.aplazo.bnpl.loans;

import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import mx.aplazo.bnpl.customers.CustomersRepository;
import mx.aplazo.bnpl.customers.model.Customer;
import mx.aplazo.bnpl.loans.dto.request.CreateLoanRequest;
import mx.aplazo.bnpl.loans.dto.response.LoanResponse;
import mx.aplazo.bnpl.loans.enums.LoanStatus;
import mx.aplazo.bnpl.loans.exception.InvalidLoanRequestException;
import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import mx.aplazo.bnpl.loans.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class LoansService {
  @Autowired
  private LoansRepository loansRepository;

  @Autowired
  private CustomersRepository customersRepository;

  public LoanResponse create(UUID customerId, CreateLoanRequest createLoanRequest) {
    if (!customerId.equals(createLoanRequest.getCustomerId())) {
      throw new UnauthorizedException("Customer ID does not match authenticated customer");
    }

    boolean hasPendingToPay = loansRepository.hasPendingToPayByCustomerId(customerId);
    if (hasPendingToPay) {
      throw new InvalidLoanRequestException("Customer has an active or late loan");
    }

    Customer customer = customersRepository.findById(customerId)
            .orElseThrow(() -> new UnauthorizedException("Customer with id " + customerId + " not found"));
    if (createLoanRequest.getAmount() > customer.getAvailableCreditLineAmount()) {
      throw new InvalidLoanRequestException("Loan amount exceeds maximum loan amount." +
              " Available credit line amount: " + customer.getAvailableCreditLineAmount() +
              ", requested loan amount: " + createLoanRequest.getAmount());
    }

    Loan loan = new Loan();
    loan.setAmount(createLoanRequest.getAmount());
    loan.setCustomerId(createLoanRequest.getCustomerId());
    loan.setStatus(LoanStatus.ACTIVE);
    loan.setCreatedAt(Instant.now());

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
