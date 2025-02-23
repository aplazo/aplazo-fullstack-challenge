package mx.aplazo.bnpl.customers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomersController {
  @Autowired
  private CustomersService customersService;

  @PostMapping
  public void create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
    customersService.create(createCustomerRequest);
  }

  @GetMapping("{customerId}")
  public Customer findById(@PathVariable UUID customerId) {
    return customersService.findById(customerId);
  }
}
