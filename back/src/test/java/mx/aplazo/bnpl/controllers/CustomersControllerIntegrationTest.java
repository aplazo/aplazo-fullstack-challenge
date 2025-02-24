package mx.aplazo.bnpl.controllers;


import mx.aplazo.bnpl.BnplApplication;
import mx.aplazo.bnpl.customers.constants.CreditLineConstants;
import mx.aplazo.bnpl.customers.dto.request.CreateCustomerRequest;
import mx.aplazo.bnpl.customers.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BnplApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomersControllerIntegrationTest {
  TestRestTemplate restTemplate = new TestRestTemplate();

  @LocalServerPort
  private int port;

  @Test
  public void testCreateCustomer() {
    // Given
    Date now = new Date();
    long twentyYears = 631152000000L;
    Date dateOfBirth = new Date(now.getTime() - twentyYears);
    CreateCustomerRequest customer = new CreateCustomerRequest(
            "John",
            "Doe",
            "Smith",
            dateOfBirth
    );

    // When
    String url = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> response = restTemplate.postForEntity(url, customer, Customer.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().getId()).isNotNull();
    assertThat(response.getHeaders().get("X-Auth-Token")).isNotNull();
    assertThat(response.getBody().getCreditLineAmount()).isEqualTo(0);
    assertThat(response.getBody().getAvailableCreditLineAmount()).isEqualTo(CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT);
  }

  @Test
  public void testCreateCustomerWithInvalidAge() {
    // Given
    Date now = new Date();
    CreateCustomerRequest customer = new CreateCustomerRequest(
            "John",
            "Doe",
            "Smith",
            now
    );

    // When
    String url = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> response = restTemplate.postForEntity(url, customer, Customer.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testGetCustomerById() {
    // Given
    Date now = new Date();
    long twentyYears = 631152000000L;
    Date dateOfBirth = new Date(now.getTime() - twentyYears);
    CreateCustomerRequest customer = new CreateCustomerRequest(
            "John",
            "Doe",
            "Smith",
            dateOfBirth
    );

    String createUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> response = restTemplate.postForEntity(createUrl, customer, Customer.class);
    Customer createdCustomer = response.getBody();

    // When
    String url = createURLWithPort("/v1/customers/" + createdCustomer.getId());
    ResponseEntity<Customer> getResponse = restTemplate.getForEntity(url, Customer.class);

    // Then
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody().getId()).isEqualTo(createdCustomer.getId());
    assertThat(getResponse.getBody().getAvailableCreditLineAmount()).isEqualTo(createdCustomer.getAvailableCreditLineAmount());
    assertThat(getResponse.getBody().getCreditLineAmount()).isEqualTo(createdCustomer.getCreditLineAmount());
  }

  @Test
  public void testGetCustomerByIdNotFound() {
    // Given
    String url = createURLWithPort("/v1/customers/123e4567-e89b-12d3-a456-426614174000");
    ResponseEntity<Customer> response = restTemplate.getForEntity(url, Customer.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testGetCustomerInvalidId() {
    // Given
    String url = createURLWithPort("/v1/customers/123");
    ResponseEntity<Customer> response = restTemplate.getForEntity(url, Customer.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
