package com.paymenthandler.payment.gateway;

public interface PaymentGateway {
    PaymentGatewayResponse processPayment(
        double amount,
        String cardNumber,
        int expiryMonth,
        int expiryYear,
        String cvv,
        String cardholderName
    );

    String getGatewayName();
}
