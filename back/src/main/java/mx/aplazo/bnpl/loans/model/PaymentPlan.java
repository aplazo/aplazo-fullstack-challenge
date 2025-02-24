package mx.aplazo.bnpl.loans.model;

import java.util.List;

public record PaymentPlan(List<Installment> installments, double commissionAmount) {
}