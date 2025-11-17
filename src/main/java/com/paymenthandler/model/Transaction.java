
package com.paymenthandler.model;

import java.time.Instant;

public class Transaction {
    private final Long id;
    private final Long payerId;
    private final Long payeeId;
    private final double amount;
    private final String method;
    private final Instant createdAt;

    public Transaction(Long id, Long payerId, Long payeeId, double amount, String method, Instant createdAt){
        this.id = id; this.payerId = payerId; this.payeeId = payeeId;
        this.amount = amount; this.method = method; this.createdAt = createdAt;
    }

    public Long getId(){ return id; }
    public Long getPayerId(){ return payerId; }
    public Long getPayeeId(){ return payeeId; }
    public double getAmount(){ return amount; }
    public String getMethod(){ return method; }
    public Instant getCreatedAt(){ return createdAt; }

    @Override public String toString(){ return "Transaction{id=" + id + ", payer=" + payerId + ", amount=" + amount + "}"; }
}
