package com.paymenthandler.model;

import java.time.Instant;
import java.util.Objects;

public class Payment {
    private final Long id;
    private final Long payerUserId;
    private final Long payeeUserId;
    private final double amount;
    private final String method; // "card", "upi", "wallet"
    private final Instant createdAt;

    private Payment(Long id, Long payerUserId, Long payeeUserId, double amount, String method, Instant createdAt){
        this.id = id;
        this.payerUserId = payerUserId;
        this.payeeUserId = payeeUserId;
        this.amount = amount;
        this.method = method;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getPayerUserId() { return payerUserId; }
    public Long getPayeeUserId() { return payeeUserId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public Instant getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long payerUserId;
        private Long payeeUserId;
        private double amount;
        private String method;
        private Instant createdAt = Instant.now();

        public Builder id(Long id){ this.id = id; return this; }
        public Builder payerUserId(Long id){ this.payerUserId = id; return this; }
        public Builder payeeUserId(Long id){ this.payeeUserId = id; return this; }
        public Builder amount(double a){ this.amount = a; return this; }
        public Builder method(String m){ this.method = m; return this; }
        public Builder createdAt(Instant t){ this.createdAt = t; return this; }

        public Payment build(){
            Objects.requireNonNull(payerUserId, "payerUserId required");
            Objects.requireNonNull(method, "method required");
            return new Payment(id, payerUserId, payeeUserId, amount, method, createdAt);
        }
    }

    @Override public String toString() {
        return "Payment{id=" + id + ", payer=" + payerUserId + ", amount=" + amount + ", method=" + method + "}";
    }
}
