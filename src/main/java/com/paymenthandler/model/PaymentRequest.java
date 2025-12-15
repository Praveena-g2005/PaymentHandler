package com.paymenthandler.model;

public class PaymentRequest {
    private final Long payerUserId;
    private final Long payeeUserId; 
    private final double amount;
    private final String method; // "card"/"upi"/"wallet"

    public PaymentRequest(Long payerUserId, Long payeeUserId, double amount, String method){
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }
        if (payerUserId == null) {
            throw new IllegalArgumentException("Payer user ID cannot be null");
        }
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }

        this.payerUserId = payerUserId;
        this.payeeUserId = payeeUserId;
        this.amount = amount;
        this.method = method;
    }

    public Long getPayerUserId(){ return payerUserId; }
    public Long getPayeeUserId(){ return payeeUserId; }
    public double getAmount(){ return amount; }
    public String getMethod(){ return method; }
}
