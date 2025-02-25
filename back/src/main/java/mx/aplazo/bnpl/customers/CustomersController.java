package mx.aplazo.bnpl.customers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mx.aplazo.bnpl.authentication.AuthTokenProvider;
import mx.aplazo.bnpl.authentication.JwtService;
import mx.aplazo.bnpl.customers.dto.request.CreateCustomerRequest;
import mx.aplazo.bnpl.customers.dto.response.CustomerResponse;
import mx.aplazo.bnpl.customers.exception.CustomerNotFoundException;
import mx.aplazo.bnpl.exceptions.ErrorCode;
import mx.aplazo.bnpl.exceptions.ErrorResponse;
import mx.aplazo.bnpl.exceptions.ErrorResponseBuilder;
import mx.aplazo.bnpl.validation.annotations.ValidationExceptionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/customers")
public class CustomersController {
  @Autowired
  private CustomersService customersService;
  @Autowired
  private JwtService jwtService;

  @PostMapping
  public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
    CustomerResponse body = customersService.create(createCustomerRequest);
    URI location = URI.create("/v1/customers/" + body.id());
    String token = jwtService.generateToken(body.id());
    return ResponseEntity.created(location)
            .header("X-Auth-Token", token)
            .body(body);
  }

  @GetMapping("{customerId}")
  public CustomerResponse findById(@PathVariable UUID customerId) {
    UUID tokenCustomerId = AuthTokenProvider.getCustomerId();
    return customersService.findById(customerId, tokenCustomerId);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleCustomerValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.INVALID_CUSTOMER_REQUEST;
    String message = ValidationExceptionParser.parse(ex);
    return ErrorResponseBuilder.toResponse(message, request, error);
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest request) {
    ErrorCode error = ErrorCode.CUSTOMER_NOT_FOUND;
    return ErrorResponseBuilder.toResponse(ex.getMessage(), request, error);
  }
}
