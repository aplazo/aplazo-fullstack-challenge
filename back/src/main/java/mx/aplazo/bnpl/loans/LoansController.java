package mx.aplazo.bnpl.loans;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoansController {
  @PostMapping
  public void create() {}

  @GetMapping("/{loanId}")
  public void findById(@PathVariable String loanId) {}
}
