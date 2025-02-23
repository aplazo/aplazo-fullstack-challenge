package mx.aplazo.bnpl.customers;

import mx.aplazo.bnpl.customers.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomersService {
  @Autowired
  private CustomersRepository customersRepository;

  public void create(CreateCustomerRequest createCustomerRequest) {
    Customer customer = new Customer();
    customer.setFirstName(createCustomerRequest.getFirstName());
    customer.setLastName(createCustomerRequest.getLastName());
    customer.setSecondLastName(createCustomerRequest.getSecondLastName());
    customer.setDateOfBirth(createCustomerRequest.getDateOfBirth());
    customersRepository.save(customer);
  }

  public Customer findById(UUID customerId) {
    return customersRepository.findById(customerId).orElseThrow(
      () -> new CustomerNotFoundException("Customer not found")
    );
  }
}
