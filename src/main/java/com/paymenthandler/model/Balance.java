package com.paymenthandler.model;

public class Balance {
    private Long userId;
    private double amount;

    public Balance(Long userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() { return userId; }
    public double getAmount() { return amount; }
}
