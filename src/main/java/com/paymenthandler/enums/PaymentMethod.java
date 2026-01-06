package com.paymenthandler.enums;

public enum PaymentMethod {
    CARD("card", 0.001), 
    UPI("upi", 0.0002),    
    WALLET("wallet", 0.0);    

    private final String value;
    private final double defaultFeePercentage;

    PaymentMethod(String value, double defaultFeePercentage) {
        this.value = value;
        this.defaultFeePercentage = defaultFeePercentage;
    }

    public String getValue() {
        return value;
    }

    public double getDefaultFeePercentage() {
        return defaultFeePercentage;
    }

    public static PaymentMethod fromString(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }

        String normalizedMethod = method.toLowerCase().trim();

        for (PaymentMethod pm : values()) {
            if (pm.value.equals(normalizedMethod)) {
                return pm;
            }
        }

        throw new IllegalArgumentException(
            "Invalid payment method: '" + method + "'. " +
            "Allowed methods: card, upi, wallet"
        );
    }
}
