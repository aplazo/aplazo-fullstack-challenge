package mx.aplazo.bnpl.controllers;

import mx.aplazo.bnpl.BnplApplication;
import mx.aplazo.bnpl.customers.constants.CreditLineConstants;
import mx.aplazo.bnpl.customers.dto.request.CreateCustomerRequest;
import mx.aplazo.bnpl.customers.model.Customer;
import mx.aplazo.bnpl.loans.dto.request.CreateLoanRequest;
import mx.aplazo.bnpl.loans.dto.response.InstallmentResponse;
import mx.aplazo.bnpl.loans.dto.response.LoanResponse;
import mx.aplazo.bnpl.loans.enums.InstallmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BnplApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoansControllerIntegrationTest {
  TestRestTemplate restTemplate = new TestRestTemplate();

  @LocalServerPort
  private int port;

  @Test
  public void testCreateLoan() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);

    // When
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class, token);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().id()).isNotNull();
    assertThat(response.getBody().amount()).isEqualTo(CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT);

    List<InstallmentResponse> installments = response.getBody().paymentPlan().installments();
    assertThat(installments.size()).isEqualTo(5);

    InstallmentResponse firstInstallment = installments.getFirst();
    assertThat(firstInstallment.status()).isEqualTo(InstallmentStatus.NEXT);
    assertThat(firstInstallment.amount()).isEqualTo(CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT / 5);

    InstallmentResponse secondInstallment = installments.get(1);
    assertThat(secondInstallment.status()).isEqualTo(InstallmentStatus.PENDING);
  }

  @Test
  public void testCreateLoanWithInvalidAmount() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            0
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);

    // When
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testCreateLoanWithInvalidToken() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("invalid-token");

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);

    // When
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  @Test
  public void testCreateLoanWithActiveLoan() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // When
    ResponseEntity<Object> secondResponse = restTemplate.postForEntity(url, requestEntity, Object.class);

    // Then
    assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testCreateLoanWithInvalidAmountExceedsCreditLine() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT + 1
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);

    // When
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testGetLoanById() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // When
    String loanId = response.getBody().id().toString();
    String getLoanUrl = createURLWithPort("/v1/loans/" + loanId);
    HttpEntity<String> getLoanRequestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<LoanResponse> getLoanResponse = restTemplate.exchange(
            getLoanUrl,
            HttpMethod.GET,
            getLoanRequestEntity,
            LoanResponse.class
    );

    // Then
    assertThat(getLoanResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(getLoanResponse.getBody().id()).isEqualTo(response.getBody().id());
    assertThat(getLoanResponse.getBody().amount()).isEqualTo(response.getBody().amount());
  }

  @Test
  public void testGetLoanByIdWithInvalidToken() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // When
    String loanId = response.getBody().id().toString();
    String getLoanUrl = createURLWithPort("/v1/loans/" + loanId);
    headers.setBearerAuth("invalid-token");
    HttpEntity<String> getLoanRequestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<Object> getLoanResponse = restTemplate.exchange(
            getLoanUrl,
            HttpMethod.GET,
            getLoanRequestEntity,
            Object.class
    );

    // Then
    assertThat(getLoanResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  public void testGetLoanWithInvalidId() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // When
    String getLoanUrl = createURLWithPort("/v1/loans/invalid-id");
    HttpEntity<String> getLoanRequestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<Object> getLoanResponse = restTemplate.exchange(
            getLoanUrl,
            HttpMethod.GET,
            getLoanRequestEntity,
            Object.class
    );

    // Then
    assertThat(getLoanResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testGetLoanWithNonExistentId() {
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
    String createCustomerUrl = createURLWithPort("/v1/customers");
    ResponseEntity<Customer> createCustomerResponse = restTemplate.postForEntity(createCustomerUrl, customer, Customer.class);

    String customerId = createCustomerResponse.getBody().getId().toString();
    CreateLoanRequest loan = new CreateLoanRequest(
            customerId,
            CreditLineConstants.DEFAULT_AVAILABLE_CREDIT_LINE_AMOUNT
    );
    String token = createCustomerResponse.getHeaders().get("X-Auth-Token").getFirst();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);

    HttpEntity<CreateLoanRequest> requestEntity = new HttpEntity<>(loan, headers);
    String url = createURLWithPort("/v1/loans");
    ResponseEntity<LoanResponse> response = restTemplate.postForEntity(url, requestEntity, LoanResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // When
    String getLoanUrl = createURLWithPort("/v1/loans/00000000-0000-0000-0000-000000000000");
    HttpEntity<String> getLoanRequestEntity = new HttpEntity<>(null, headers);
    ResponseEntity<Object> getLoanResponse = restTemplate.exchange(
            getLoanUrl,
            HttpMethod.GET,
            getLoanRequestEntity,
            Object.class
    );

    // Then
    assertThat(getLoanResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  private String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
