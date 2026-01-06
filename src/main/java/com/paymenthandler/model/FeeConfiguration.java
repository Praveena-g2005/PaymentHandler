package com.paymenthandler.model;

import com.paymenthandler.enums.FeeType;

import java.time.Instant;
import java.util.Objects;

public class FeeConfiguration {
    private final Long id;
    private final String paymentMethod;
    private final FeeType feeType;
    private final double feeValue;
    private final Instant createdAt;
    private final Instant updatedAt;

    private FeeConfiguration(Long id, String paymentMethod, FeeType feeType,
                            double feeValue, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.feeType = feeType;
        this.feeValue = feeValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public double getFeeValue() {
        return feeValue;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public double calculateFee(double baseAmount) {
        if (feeType == FeeType.PERCENTAGE) {
            return baseAmount/100 * feeValue;
        } else {
            return feeValue;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String paymentMethod;
        private FeeType feeType = FeeType.PERCENTAGE;
        private double feeValue;
        private Instant createdAt = Instant.now();
        private Instant updatedAt = Instant.now();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder feeType(FeeType feeType) {
            this.feeType = feeType;
            return this;
        }

        public Builder feeValue(double feeValue) {
            this.feeValue = feeValue;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FeeConfiguration build() {
            Objects.requireNonNull(paymentMethod, "paymentMethod is required");
            Objects.requireNonNull(feeType, "feeType is required");
            if (feeValue < 0) {
                throw new IllegalArgumentException("feeValue must be non-negative");
            }
            return new FeeConfiguration(id, paymentMethod, feeType, feeValue, createdAt, updatedAt);
        }
    }
}
