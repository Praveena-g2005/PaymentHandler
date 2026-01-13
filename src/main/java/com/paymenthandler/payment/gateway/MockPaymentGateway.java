package com.paymenthandler.payment.gateway;

import javax.inject.Singleton;
import java.time.YearMonth;
import java.util.UUID;

@Singleton
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentGatewayResponse processPayment(double amount, String cardNumber,
                                                 int expiryMonth, int expiryYear,
                                                 String cvv, String cardholderName) {
        if (amount <= 0) {
            return new PaymentGatewayResponse(false, "Amount must be positive", null, "Mock");
        }

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return new PaymentGatewayResponse(false, "Card number is required", null, "Mock");
        }

        String normalizedCard = cardNumber.replaceAll("\\s+", "");

        if (!normalizedCard.matches("\\d+")) {
            return new PaymentGatewayResponse(false, "Card number must contain only digits", null, "Mock");
        }

        if (normalizedCard.length() < 13 || normalizedCard.length() > 19) {
            return new PaymentGatewayResponse(false, "Card number must be between 13 and 19 digits", null, "Mock");
        }

        if (expiryMonth < 1 || expiryMonth > 12) {
            return new PaymentGatewayResponse(false, "Expiry month must be between 1 and 12", null, "Mock");
        }

        int currentYear = YearMonth.now().getYear();
        if (expiryYear < currentYear || expiryYear > currentYear + 20) {
            return new PaymentGatewayResponse(false, "Invalid expiry year", null, "Mock");
        }

        YearMonth cardExpiry = YearMonth.of(expiryYear, expiryMonth);
        YearMonth currentMonth = YearMonth.now();
        if (cardExpiry.isBefore(currentMonth)) {
            return new PaymentGatewayResponse(false, "Card has expired", null, "Mock");
        }

        if (cvv == null || cvv.trim().isEmpty()) {
            return new PaymentGatewayResponse(false, "CVV is required", null, "Mock");
        }

        if (cvv.contains("-")) {
            return new PaymentGatewayResponse(false, "CVV cannot be negative", null, "Mock");
        }

        if (!cvv.matches("\\d{3,4}")) {
            return new PaymentGatewayResponse(false, "CVV must be 3 or 4 digits", null, "Mock");
        }

        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            return new PaymentGatewayResponse(false, "Cardholder name is required", null, "Mock");
        }

        if (cardholderName.trim().length() < 2) {
            return new PaymentGatewayResponse(false, "Cardholder name is too short", null, "Mock");
        }

        if (!cardholderName.matches("[a-zA-Z\\s]+")) {
            return new PaymentGatewayResponse(false, "Cardholder name must contain only letters and spaces", null, "Mock");
        }

        if (normalizedCard.equals("4000000000000002")) {
            return new PaymentGatewayResponse(false, "Card declined", null, "Mock");
        }

        String txnId = "MOCK_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentGatewayResponse(true, "Payment successful", txnId, "Mock");
    }

    @Override
    public String getGatewayName() {
        return "Mock Gateway";
    }
}
