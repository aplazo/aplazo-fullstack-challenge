package mx.aplazo.bnpl.controllers;


import mx.aplazo.bnpl.BnplApplication;
import mx.aplazo.bnpl.customers.constants.CreditLineConstants;
import mx.aplazo.bnpl.customers.dto.request.CreateCustomerRequest;
import mx.aplazo.bnpl.customers.dto.response.CustomerResponse;
import mx.aplazo.bnpl.customers.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

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
    ResponseEntity<CustomerResponse> response = restTemplate.postForEntity(url, customer, CustomerResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().id()).isNotNull();
    assertThat(response.getHeaders().get("X-Auth-Token")).isNotNull();
    assertThat(response.getBody().creditLineAmount()).isEqualTo(0);
    assertThat(response.getBody().availableCreditLineAmount()).isEqualTo(CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT);
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
    ResponseEntity<Object> response = restTemplate.postForEntity(url, customer, Object.class);

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
    String token = response.getHeaders().get("X-Auth-Token").get(0);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    // When
    String url = createURLWithPort("/v1/customers/" + createdCustomer.getId());
    ResponseEntity<CustomerResponse> getResponse = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            CustomerResponse.class
    );

    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getResponse.getBody().id()).isEqualTo(createdCustomer.getId());
    assertThat(getResponse.getBody().availableCreditLineAmount()).isEqualTo(createdCustomer.getAvailableCreditLineAmount());
    assertThat(getResponse.getBody().creditLineAmount()).isEqualTo(createdCustomer.getCreditLineAmount());
  }

  @Test
  public void testGetCustomerByIdNotFound() {
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
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createUrl, customer, Customer.class);
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").get(0);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    // When
    UUID customerId = UUID.randomUUID();
    String url = createURLWithPort("/v1/customers/" + customerId);
    ResponseEntity<Object> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Object.class
    );

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testGetCustomerInvalidId() {
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
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createUrl, customer, Customer.class);
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").get(0);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.setBearerAuth(token);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    // When
    String url = createURLWithPort("/v1/customers/123");
    ResponseEntity<Object> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Object.class
    );

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
