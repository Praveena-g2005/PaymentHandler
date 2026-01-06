package com.paymenthandler.enums;

public enum FeeType {
    PERCENTAGE,
    FIXED;

    public static FeeType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            return PERCENTAGE;
        }
        try {
            return FeeType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid fee type: " + type + ". Must be PERCENTAGE or FIXED");
        }
    }
}
