package com.paymenthandler.model;

public class FeeCalculationResult {
    private final double baseAmount;
    private final double feeAmount;
    private final double totalAmount;

    public FeeCalculationResult(double baseAmount, double feeAmount) {
        this.baseAmount = baseAmount;
        this.feeAmount = feeAmount;
        this.totalAmount = baseAmount + feeAmount;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
