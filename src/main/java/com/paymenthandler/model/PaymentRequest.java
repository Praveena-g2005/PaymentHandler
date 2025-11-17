package com.paymenthandler.model;

public class PaymentRequest {
    private final Long payerUserId;
    private final Long payeeUserId; // optional
    private final double amount;
    private final String method; // "card"/"upi"/"wallet"

    public PaymentRequest(Long payerUserId, Long payeeUserId, double amount, String method){
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
