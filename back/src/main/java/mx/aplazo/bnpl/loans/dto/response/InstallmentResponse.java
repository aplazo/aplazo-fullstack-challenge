package mx.aplazo.bnpl.loans.dto.response;

import mx.aplazo.bnpl.loans.enums.InstallmentStatus;

import java.time.Instant;

public record InstallmentResponse(double amount, Instant scheduledPaymentDate, InstallmentStatus status) {
}
