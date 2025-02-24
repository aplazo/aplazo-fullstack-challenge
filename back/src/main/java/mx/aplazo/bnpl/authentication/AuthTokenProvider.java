package mx.aplazo.bnpl.authentication;

import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AuthTokenProvider {
  public static UUID getToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("You must be authenticated to perform this action.");
    }

    return (UUID) authentication.getPrincipal();
  }
}
