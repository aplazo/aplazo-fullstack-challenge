package mx.aplazo.bnpl.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  INTERNAL_SERVER_ERROR("APZ000001", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_CUSTOMER_REQUEST("APZ000002", "INVALID_CUSTOMER_REQUEST", HttpStatus.BAD_REQUEST),
  RATE_LIMIT_ERROR("APZ000003", "RATE_LIMIT_ERROR", HttpStatus.TOO_MANY_REQUESTS),
  INVALID_REQUEST("APZ000004", "INVALID_REQUEST", HttpStatus.BAD_REQUEST),
  CUSTOMER_NOT_FOUND("APZ000005", "CUSTOMER_NOT_FOUND", HttpStatus.NOT_FOUND),
  INVALID_LOAN_REQUEST("APZ000006", "INVALID_LOAN_REQUEST", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED("APZ000007", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
  LOAN_NOT_FOUND("APZ000008", "LOAN_NOT_FOUND", HttpStatus.NOT_FOUND),
  ;

  private final String code;
  private final String error;
  private final HttpStatus status;

  ErrorCode(String code, String error, HttpStatus status) {
    this.code = code;
    this.error = error;
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public String getError() {
    return error;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
