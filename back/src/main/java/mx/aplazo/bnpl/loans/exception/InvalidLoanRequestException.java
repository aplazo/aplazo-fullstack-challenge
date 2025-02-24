package mx.aplazo.bnpl.loans.exception;

public class InvalidLoanRequestException extends RuntimeException {
  public InvalidLoanRequestException(String message) {
    super(message);
  }
}
