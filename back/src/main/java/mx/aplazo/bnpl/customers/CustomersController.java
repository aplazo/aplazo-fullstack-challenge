package mx.aplazo.bnpl.customers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomersController {
  @Autowired
  private CustomersService customersService;

  @PostMapping
  public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
    CustomerResponse body = customersService.create(createCustomerRequest);
    URI location = URI.create("/customers/" + body.getId());
    String token = ""; // TODO: implement authentication
    return ResponseEntity.created(location)
            .header("X-Auth-Token", token)
            .body(body);
  }

  @GetMapping("{customerId}")
  public CustomerResponse findById(@PathVariable UUID customerId) {
    return customersService.findById(customerId);
  }
}
