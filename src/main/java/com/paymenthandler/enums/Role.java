package com.paymenthandler.enums;

public enum Role {
    USER,
    ADMIN;

    public static Role fromString(String role) {
        if (role == null || role.trim().isEmpty()) {
            return USER;
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return USER;
        }
    }
}
