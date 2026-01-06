package com.paymenthandler.payment.gateway;

public class PaymentGatewayResponse {

    private final boolean success;
    private final String message;
    private final String transactionId;
    private final String gatewayName;

    public PaymentGatewayResponse(boolean success, String message, String transactionId, String gatewayName) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.gatewayName = gatewayName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    @Override
    public String toString() {
        return String.format("PaymentGatewayResponse{success=%s, message='%s', transactionId='%s', gateway='%s'}",
            success, message, transactionId, gatewayName);
    }
}
