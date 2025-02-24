package mx.aplazo.bnpl.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
  private String secret;
  private int expirationTimeInHours;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public int getExpirationTimeInHours() {
    return expirationTimeInHours;
  }

  public void setExpirationTimeInHours(int expirationTimeInHours) {
    this.expirationTimeInHours = expirationTimeInHours;
  }
}
