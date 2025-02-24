package mx.aplazo.bnpl.loans;

import jakarta.transaction.Transactional;
import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import mx.aplazo.bnpl.customers.CustomersRepository;
import mx.aplazo.bnpl.customers.model.Customer;
import mx.aplazo.bnpl.loans.dto.request.CreateLoanRequest;
import mx.aplazo.bnpl.loans.dto.response.InstallmentResponse;
import mx.aplazo.bnpl.loans.dto.response.LoanResponse;
import mx.aplazo.bnpl.loans.dto.response.PaymentPlan;
import mx.aplazo.bnpl.loans.enums.InstallmentStatus;
import mx.aplazo.bnpl.loans.enums.LoanStatus;
import mx.aplazo.bnpl.loans.exception.InvalidLoanRequestException;
import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import mx.aplazo.bnpl.loans.model.Installment;
import mx.aplazo.bnpl.loans.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoansService {
  @Autowired
  private LoansRepository loansRepository;

  @Autowired
  private CustomersRepository customersRepository;

  @Transactional
  public LoanResponse create(UUID customerId, CreateLoanRequest createLoanRequest) {
    if (!customerId.equals(createLoanRequest.getCustomerId())) {
      throw new UnauthorizedException("Customer ID does not match authenticated customer");
    }

    boolean hasPendingToPay = loansRepository.existsByCustomerIdAndStatusIn(customerId, new LoanStatus[]{LoanStatus.ACTIVE, LoanStatus.LATE});
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
    loan.setCustomer(customer);
    loan.setStatus(LoanStatus.ACTIVE);
    loan.setCreatedAt(Instant.now());

    int numberOfInstallments = 5;
    double amount = createLoanRequest.getAmount() / numberOfInstallments;

    List<Installment> installments = new ArrayList<>();
    LocalDate localDate = LocalDate.now();
    for (int i = 0; i < numberOfInstallments; i++) {
      InstallmentStatus status = i == 0 ? InstallmentStatus.NEXT : InstallmentStatus.PENDING;
      Installment installment = new Installment();
      Instant nextPaymentDate = localDate.plusMonths(i).atStartOfDay().toInstant(ZoneOffset.UTC);
      installment.setAmount(amount);
      installment.setLoan(loan);
      installment.setScheduledPaymentDate(nextPaymentDate);
      installment.setStatus(status);
      installment.setCreatedAt(Instant.now());
      installments.add(installment);
    }
    loan.setInstallments(installments);

    customer.setAvailableCreditLineAmount(
            customer.getAvailableCreditLineAmount() - createLoanRequest.getAmount());
    customersRepository.save(customer);

    Loan savedLoan = loansRepository.save(loan);
    return mapToLoanResponse(savedLoan);
  }

  public LoanResponse findById(UUID customerId, UUID loanId) {
    Loan loan = loansRepository.findById(loanId)
            .orElseThrow(() -> new LoanNotFoundException("Loan with id " + loanId + " not found"));
    UUID loanCustomerId = loan.getCustomer().getId();
    if (!customerId.equals(loanCustomerId)) {
      throw new UnauthorizedException("Customer ID does not match authenticated customer");
    }
    return mapToLoanResponse(loan);
  }

  private LoanResponse mapToLoanResponse(Loan loan) {
    List<InstallmentResponse> installmentResponses = loan.getInstallments().stream()
            .map(installment -> new InstallmentResponse(installment.getAmount(), installment.getScheduledPaymentDate(), installment.getStatus()))
            .toList();
    PaymentPlan paymentPlan = new PaymentPlan(installmentResponses, 0);
    return new LoanResponse(loan.getId(), loan.getCustomer().getId(), loan.getAmount(), loan.getCreatedAt(), paymentPlan);
  }
}
