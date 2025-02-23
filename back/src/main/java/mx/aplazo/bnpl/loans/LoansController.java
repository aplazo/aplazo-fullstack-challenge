package mx.aplazo.bnpl.loans;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoansController {
  @Autowired
  private LoansService loansService;

  @PostMapping
  public ResponseEntity<LoanResponse> create(@Valid @RequestBody CreateLoanRequest createLoanRequest) {
    LoanResponse loan = loansService.create(createLoanRequest);
    URI location = URI.create("/loans/" + loan.id());
    return ResponseEntity.created(location)
            .body(loan);
  }

  @GetMapping("/{loanId}")
  public LoanResponse findById(@PathVariable UUID loanId) {
    return loansService.findById(loanId);
  }
}
