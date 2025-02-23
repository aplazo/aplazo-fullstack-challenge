package mx.aplazo.bnpl.loans.exception;

public class LoanNotFoundException extends RuntimeException {
  public LoanNotFoundException(String message) {
    super(message);
  }
}
