package mx.aplazo.bnpl.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mx.aplazo.bnpl.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
  DateUtils dateUtils = new DateUtils();
  @Autowired
  private JwtConfiguration jwtConfiguration;

  public String generateToken(UUID customerId) {
    int expirationTimeInHours = jwtConfiguration.getExpirationTimeInHours();
    int expirationTimeInMilliseconds = 1000 * 60 * 60 * expirationTimeInHours;
    return Jwts.builder()
            .setSubject(customerId.toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMilliseconds))
            .signWith(SignatureAlgorithm.HS256, jwtConfiguration.getSecret())
            .compact();
  }

  public UUID parseToken(String token) {
    return UUID.fromString(Jwts.parser()
            .setSigningKey(jwtConfiguration.getSecret())
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
  }

  public boolean isTokenExpired(String token) {
    return Jwts.parser()
            .setSigningKey(jwtConfiguration.getSecret())
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(dateUtils.now());
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    return new UsernamePasswordAuthenticationToken(parseToken(token), null, null);
  }
}
