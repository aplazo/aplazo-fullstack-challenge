package mx.aplazo.bnpl.customers.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(UUID id, double creditLineAmount, double availableCreditLineAmount, Instant createdAt) {
}
