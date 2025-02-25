package mx.aplazo.bnpl.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.aplazo.bnpl.exceptions.ErrorCode;
import mx.aplazo.bnpl.exceptions.ErrorResponse;
import mx.aplazo.bnpl.exceptions.ErrorResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements Filter {
  private static final int MAX_REQUESTS_PER_MINUTE = 60;
  // TODO: use redis or other distributed cache to store request counts
  private final Map<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
  Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    String clientIpAddress = httpServletRequest.getRemoteAddr();

    logger.info("Request from IP address: {}", clientIpAddress);

    // Initialize request count for the client IP address
    requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
    AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIpAddress);

    logger.info("Request count: {}", requestCount.get());

    // Increment the request count
    int requests = requestCount.incrementAndGet();

    // Check if the request limit has been exceeded
    if (requests > MAX_REQUESTS_PER_MINUTE) {
      ErrorCode errorCode = ErrorCode.RATE_LIMIT_ERROR;
      ResponseEntity<ErrorResponse> body = ErrorResponseBuilder
              .toResponse("RATE_LIMIT_ERROR", httpServletRequest, errorCode);
      httpServletResponse.setContentType("application/json");
      httpServletResponse.setStatus(body.getStatusCode().value());

      ObjectMapper objectMapper = new ObjectMapper();
      httpServletResponse.getWriter().write(
              objectMapper.writeValueAsString(body.getBody())
      );

      logger.error("Rate limit exceeded for IP address: {}", clientIpAddress);
      return;
    }

    logger.debug("Request count for IP address {} is {}", clientIpAddress, requests);

    // Allow the request to proceed
    chain.doFilter(request, response);

    // TODO: reset request counts periodically (not implemented in this simple example)
  }

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void destroy() {
  }
}

