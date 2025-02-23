package mx.aplazo.bnpl.customers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomersController {

  @PostMapping()
  public void create(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
    System.out.println(createCustomerRequest);
  }

  @GetMapping("{customerId}")
  public void findById(@PathVariable String customerId) {}
}
