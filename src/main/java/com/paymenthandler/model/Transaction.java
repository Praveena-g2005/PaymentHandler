
package com.paymenthandler.model;

import java.time.Instant;

public class Transaction {
    private final Long id;
    private final Long payerId;
    private final Long payeeId;
    private final double amount;
    private final String method;
    private final Instant createdAt;
    private final double feeAmount;
    private final double totalAmount;

    public Transaction(Long id, Long payerId, Long payeeId, double amount, String method, Instant createdAt,
                      double feeAmount, double totalAmount){
        this.id = id;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
        this.method = method;
        this.createdAt = createdAt;
        this.feeAmount = feeAmount;
        this.totalAmount = totalAmount;
    }

    public Long getId(){ return id; }
    public Long getPayerId(){ return payerId; }
    public Long getPayeeId(){ return payeeId; }
    public double getAmount(){ return amount; }
    public String getMethod(){ return method; }
    public Instant getCreatedAt(){ return createdAt; }
    public double getFeeAmount(){ return feeAmount; }
    public double getTotalAmount(){ return totalAmount; }

    @Override
    public String toString(){
        return "Transaction{id=" + id + ", payer=" + payerId + ", amount=" + amount +
               ", fee=" + feeAmount + ", total=" + totalAmount + "}";
    }
}
