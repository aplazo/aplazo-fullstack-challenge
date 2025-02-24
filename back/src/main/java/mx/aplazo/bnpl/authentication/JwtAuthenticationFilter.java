package mx.aplazo.bnpl.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      tryToAuthenticate(request);
    } catch (Exception e) {
      this.logger.error("Error occurred while processing JWT token", e);
    }
    filterChain.doFilter(request, response);
  }

  private void tryToAuthenticate(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      return;
    }

    String token = authorizationHeader.substring(7);
    boolean isTokenExpired = jwtService.isTokenExpired(token);
    if (isTokenExpired) {
      return;
    }
    UsernamePasswordAuthenticationToken authentication = jwtService.getAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
