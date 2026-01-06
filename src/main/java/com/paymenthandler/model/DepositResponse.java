package com.paymenthandler.model;

public class DepositResponse {

    private final boolean success;
    private final String message;
    private final String transactionId;
    private final double newBalance;

    public DepositResponse(boolean success, String message, String transactionId, double newBalance) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.newBalance = newBalance;
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

    public double getNewBalance() {
        return newBalance;
    }
}
