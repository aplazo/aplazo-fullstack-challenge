package mx.aplazo.bnpl.loans.model;

import mx.aplazo.bnpl.loans.enums.InstallmentStatus;

import java.time.Instant;

public record Installment(double amount, Instant scheduledPaymentDate, InstallmentStatus status) {
}