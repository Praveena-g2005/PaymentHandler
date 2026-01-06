package com.paymenthandler.payment.gateway;

import javax.inject.Singleton;
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
            return new PaymentGatewayResponse(false, "Card number required", null, "Mock");
        }

        String normalizedCard = cardNumber.replaceAll("\\s+", "");

        if (normalizedCard.length() < 13 || normalizedCard.length() > 19) {
            return new PaymentGatewayResponse(false, "Invalid card number", null, "Mock");
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
