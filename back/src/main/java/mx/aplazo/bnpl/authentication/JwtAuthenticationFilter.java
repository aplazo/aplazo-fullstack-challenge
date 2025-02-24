package mx.aplazo.bnpl.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      tryToAuthenticate(request);
    } catch (Exception e) {
      logger.error("Error occurred while processing JWT token", e);
    }
    filterChain.doFilter(request, response);
  }

  private void tryToAuthenticate(HttpServletRequest request) {
    logger.info("Trying to authenticate request");

    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      logger.info("No Authorization header found");
      return;
    }

    String token = authorizationHeader.substring(7);
    boolean isTokenExpired = jwtService.isTokenExpired(token);
    if (isTokenExpired) {
      logger.info("Token is expired, not setting authentication");
      return;
    }

    logger.info("Token is not expired, setting authentication");
    UsernamePasswordAuthenticationToken authentication = jwtService.getAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
