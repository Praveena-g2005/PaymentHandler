package com.paymenthandler.model;

public class FeeConfigurationRequest {
    private final String paymentMethod;
    private final String feeType;
    private final double feeValue;

    public FeeConfigurationRequest(String paymentMethod, String feeType, double feeValue) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method can't be empty");
        }
        if (feeType == null || feeType.trim().isEmpty()) {
            throw new IllegalArgumentException("Fee type can't be empty");
        }
        if (feeValue < 0) {
            throw new IllegalArgumentException("Fee value must be positive");
        }

        this.paymentMethod = paymentMethod.toLowerCase().trim();
        this.feeType = feeType.toUpperCase().trim();
        this.feeValue = feeValue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getFeeType() {
        return feeType;
    }

    public double getFeeValue() {
        return feeValue;
    }
}
