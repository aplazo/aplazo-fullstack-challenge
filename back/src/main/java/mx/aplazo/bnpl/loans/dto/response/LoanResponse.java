package mx.aplazo.bnpl.loans.dto.response;

import java.time.Instant;
import java.util.UUID;

public record LoanResponse(UUID id, UUID customerId, double amount, Instant createdAt) {
}
