package mx.aplazo.bnpl.customers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomersController {

  @PostMapping()
  public void create() {}

  @GetMapping("{customerId}")
  public void findById(@PathVariable String customerId) {}
}
