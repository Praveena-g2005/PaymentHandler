package com.paymenthandler.model;

public class DepositRequest {

    private final Long userId;
    private final double amount;
    private final String cardNumber;
    private final int expiryMonth;
    private final int expiryYear;
    private final String cvv;
    private final String cardholderName;

    public DepositRequest(Long userId, double amount, String cardNumber,
                         int expiryMonth, int expiryYear, String cvv, String cardholderName) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (expiryMonth < 1 || expiryMonth > 12 ) {
            throw new IllegalArgumentException("Invalid expiry month");
        }
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cardholder name is required");
        }

        this.userId = userId;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.cardholderName = cardholderName;
    }

    public Long getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public String getCardholderName() {
        return cardholderName;
    }
}
