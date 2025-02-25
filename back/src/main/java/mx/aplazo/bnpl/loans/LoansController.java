package mx.aplazo.bnpl.loans;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mx.aplazo.bnpl.authentication.AuthTokenProvider;
import mx.aplazo.bnpl.exceptions.ErrorCode;
import mx.aplazo.bnpl.exceptions.ErrorResponse;
import mx.aplazo.bnpl.exceptions.ErrorResponseBuilder;
import mx.aplazo.bnpl.loans.dto.request.CreateLoanRequest;
import mx.aplazo.bnpl.loans.dto.response.LoanResponse;
import mx.aplazo.bnpl.loans.exception.InvalidLoanRequestException;
import mx.aplazo.bnpl.loans.exception.LoanNotFoundException;
import mx.aplazo.bnpl.validation.annotations.ValidationExceptionParser;
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
    UUID customerId = AuthTokenProvider.getCustomerId();
    LoanResponse loan = loansService.create(customerId, createLoanRequest);
    URI location = URI.create("/v1/loans/" + loan.id());
    return ResponseEntity.created(location)
            .body(loan);
  }

  @GetMapping("/{loanId}")
  public LoanResponse findById(@PathVariable UUID loanId) {
    UUID customerId = AuthTokenProvider.getCustomerId();
    return loansService.findById(customerId, loanId);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleLoanValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_LOAN_REQUEST;
    String message = ValidationExceptionParser.parse(ex);
    return ErrorResponseBuilder.toResponse(message, request, error);
  }

  @ExceptionHandler(LoanNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleLoanNotFound(LoanNotFoundException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.LOAN_NOT_FOUND;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }

  @ExceptionHandler(InvalidLoanRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidLoanRequest(InvalidLoanRequestException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_LOAN_REQUEST;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }
}
