package mx.aplazo.bnpl.loans;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mx.aplazo.bnpl.authentication.AuthTokenProvider;
import mx.aplazo.bnpl.exceptions.ErrorCode;
import mx.aplazo.bnpl.exceptions.ErrorResponseBuilder;
import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import mx.aplazo.bnpl.validation.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/loans")
public class LoansController {
  @Autowired
  private LoansService loansService;

  @PostMapping
  public ResponseEntity<LoanResponse> create(@Valid @RequestBody CreateLoanRequest createLoanRequest) {
    UUID customerId = AuthTokenProvider.getToken();
    LoanResponse loan = loansService.create(customerId, createLoanRequest);
    URI location = URI.create("/loans/" + loan.id());
    return ResponseEntity.created(location)
            .body(loan);
  }

  @GetMapping("/{loanId}")
  public LoanResponse findById(@PathVariable UUID loanId) {
    UUID customerId = AuthTokenProvider.getToken();
    return loansService.findById(customerId, loanId);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleLoanValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_LOAN_REQUEST;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }

  @ExceptionHandler(LoanNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLoanNotFound(LoanNotFoundException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.LOAN_NOT_FOUND;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }
}
