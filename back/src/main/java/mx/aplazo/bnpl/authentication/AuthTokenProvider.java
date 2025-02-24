package mx.aplazo.bnpl.authentication;

import mx.aplazo.bnpl.authentication.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AuthTokenProvider {
  static Logger logger = LoggerFactory.getLogger(AuthTokenProvider.class);

  public static UUID getToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    logger.info("Getting token from authentication context");
    if (authentication == null || !authentication.isAuthenticated()) {
      logger.error("User is not authenticated");
      throw new UnauthorizedException("You must be authenticated to perform this action.");
    }

    return (UUID) authentication.getPrincipal();
  }
}
