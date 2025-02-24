package mx.aplazo.bnpl.services;

import mx.aplazo.bnpl.authentication.JwtConfiguration;
import mx.aplazo.bnpl.authentication.JwtService;
import mx.aplazo.bnpl.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
  @InjectMocks
  private JwtService jwtService;

  @Mock
  private JwtConfiguration jwtConfiguration;

  @Mock
  private DateUtils dateUtils;

  @Test
  void testGenerateToken() {
    UUID customerId = UUID.randomUUID();

    when(jwtConfiguration.getExpirationTimeInHours()).thenReturn(1);
    when(jwtConfiguration.getSecret()).thenReturn("secret");

    String token = jwtService.generateToken(customerId);

    assertNotNull(token);
    verify(jwtConfiguration, times(1)).getExpirationTimeInHours();
    verify(jwtConfiguration, times(1)).getSecret();
  }

  @Test
  void testParseToken() {
    UUID customerId = UUID.randomUUID();

    when(jwtConfiguration.getExpirationTimeInHours()).thenReturn(1);
    when(jwtConfiguration.getSecret()).thenReturn("secret");
    String token = jwtService.generateToken(customerId);

    UUID parsedCustomerId = jwtService.parseToken(token);

    assertNotNull(parsedCustomerId);
    assertEquals(customerId, parsedCustomerId);
  }

  @Test
  void testParseTokenFailIfInvalidSecret() {
    UUID customerId = UUID.randomUUID();

    when(jwtConfiguration.getExpirationTimeInHours()).thenReturn(1);
    when(jwtConfiguration.getSecret()).thenReturn("secret");
    String token = jwtService.generateToken(customerId);

    when(jwtConfiguration.getSecret()).thenReturn("invalidSecret");
    assertThrows(Exception.class, () -> jwtService.parseToken(token));
  }

  @Test
  void testIsTokenExpiredFalse() {
    UUID customerId = UUID.randomUUID();

    when(jwtConfiguration.getExpirationTimeInHours()).thenReturn(1);
    when(jwtConfiguration.getSecret()).thenReturn("secret");
    String token = jwtService.generateToken(customerId);

    when(dateUtils.now()).thenReturn(new Date());
    boolean isTokenExpired = jwtService.isTokenExpired(token);

    assertFalse(isTokenExpired);
  }

  @Test
  void testIsTokenExpiredTrue() {
    UUID customerId = UUID.randomUUID();

    int jwtExpirationTimeInHours = 1;
    when(jwtConfiguration.getExpirationTimeInHours()).thenReturn(jwtExpirationTimeInHours);
    when(jwtConfiguration.getSecret()).thenReturn("secret");
    String token = jwtService.generateToken(customerId);

    int expirationTimeInMilliseconds = 1000 * 60 * 60 * jwtExpirationTimeInHours * 20;
    Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMilliseconds);
    when(dateUtils.now()).thenReturn(expirationDate);

    boolean isTokenExpired = jwtService.isTokenExpired(token);

    assertTrue(isTokenExpired);
  }
}
