package mx.aplazo.bnpl.customers;

import mx.aplazo.bnpl.customers.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CustomersService {
  @Autowired
  private CustomersRepository customersRepository;

  public CustomerResponse create(CreateCustomerRequest createCustomerRequest) {
    Customer customer = new Customer();
    customer.setFirstName(createCustomerRequest.getFirstName());
    customer.setLastName(createCustomerRequest.getLastName());
    customer.setSecondLastName(createCustomerRequest.getSecondLastName());
    customer.setDateOfBirth(createCustomerRequest.getDateOfBirth());
    customer.setCreatedAt(Instant.now());
    customersRepository.save(customer);
    return toCustomerResponse(customer);
  }

  public CustomerResponse findById(UUID customerId) {
    Customer customer = customersRepository.findById(customerId).orElseThrow(
            () -> new CustomerNotFoundException("Customer with id " + customerId + " not found")
    );
    return toCustomerResponse(customer);
  }

  private CustomerResponse toCustomerResponse(Customer customer) {
    return new CustomerResponse(
            customer.getId(),
            // TODO: where is creditLineAmount and availableCreditLineAmount?
            0,
            0,
            customer.getCreatedAt()
    );
  }
}
