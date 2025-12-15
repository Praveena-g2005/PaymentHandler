package com.paymenthandler.service;

import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.paymenthandler.model.*;
import com.paymenthandler.dao.*;


@ApplicationScoped
public class BalanceService {

    @Inject
    @Named("balanceDao")
    private BalanceDao dao;

    public Optional<Balance> getBalance(Long userId) {
        return dao.getBalance(userId);
    }

    public Balance deposit(Long userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        Optional<Balance> current = dao.getBalance(userId);
        double newAmount = current.map(b -> b.getAmount() + amount).orElse(amount);
        System.out.println("Payee Balance after transaction : "+ newAmount);
        return dao.updateBalance(userId, newAmount);
    }

    public Optional<String> withdraw(Long userId, double amount) {
        if (amount <= 0) {
            return Optional.of("Withdrawal amount must be greater than 0");
        }

        Optional<Balance> current = dao.getBalance(userId);

        if (current.isEmpty()) return Optional.of("No balance found");

        double currentAmount = current.get().getAmount();
        System.out.println("Payer Balance : "+ currentAmount);
        if (currentAmount < amount) return Optional.of("Insufficient funds");

        dao.updateBalance(userId, currentAmount - amount);
        return Optional.empty();
    }

    public Optional<String> transferBalance(Long fromUserId, Long toUserId, double amount) {
        if (amount <= 0) {
            return Optional.of("Transfer amount must be greater than 0");
        }
        return dao.transferBalance(fromUserId, toUserId, amount);
    }
}
