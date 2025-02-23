package mx.aplazo.bnpl.customers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomersController {

  @PostMapping
  public ResponseEntity<Object> create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
    return ResponseEntity.ok().build();
  }

  @GetMapping("{customerId}")
  public void findById(@PathVariable String customerId) {}
}
