package mx.aplazo.bnpl.customers;

import mx.aplazo.bnpl.customers.constants.CreditLineConstants;
import mx.aplazo.bnpl.customers.dto.request.CreateCustomerRequest;
import mx.aplazo.bnpl.customers.dto.response.CustomerResponse;
import mx.aplazo.bnpl.customers.exception.CustomerNotFoundException;
import mx.aplazo.bnpl.customers.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CustomersService {
  Logger logger = LoggerFactory.getLogger(CustomersService.class);

  @Autowired
  private CustomersRepository customersRepository;

  public CustomerResponse create(CreateCustomerRequest createCustomerRequest) {
    String fullName = createCustomerRequest.getFullName();
    logger.info("Creating customer with full name: " + fullName);

    Customer customer = new Customer();
    customer.setFirstName(createCustomerRequest.getFirstName());
    customer.setLastName(createCustomerRequest.getLastName());
    customer.setSecondLastName(createCustomerRequest.getSecondLastName());
    customer.setDateOfBirth(createCustomerRequest.getDateOfBirth());
    customer.setCreditLineAmount(CreditLineConstants.DEFAULT_CREDIT_LINE_AMOUNT);
    customer.setAvailableCreditLineAmount(CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT);
    customer.setCreatedAt(Instant.now());
    customersRepository.save(customer);

    logger.info("Customer with full name: " + fullName + " and id: " + customer.getId() + " created successfully");

    return toCustomerResponse(customer);
  }

  public CustomerResponse findById(UUID customerId, UUID tokenCustomerId) {
    logger.info("Finding customer with id: " + customerId);
    Customer customer = customersRepository.findById(customerId).orElseThrow(
            () -> new CustomerNotFoundException("Customer with id " + customerId + " not found")
    );
    if (!customer.getId().equals(tokenCustomerId)) {
      logger.error("Customer with id: " + tokenCustomerId + " is not authorized to access customer with id: " + customerId);
      throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
    }

    return toCustomerResponse(customer);
  }

  private CustomerResponse toCustomerResponse(Customer customer) {
    return new CustomerResponse(
            customer.getId(),
            customer.getCreditLineAmount(),
            customer.getAvailableCreditLineAmount(),
            customer.getCreatedAt()
    );
  }
}
