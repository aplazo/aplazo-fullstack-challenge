package mx.aplazo.bnpl.loans.dto.response;

import java.util.List;

public record PaymentPlan(List<InstallmentResponse> installments, double commissionAmount) {
}