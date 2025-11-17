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

    // get balance
    public Optional<Balance> getBalance(Long userId) {
        return dao.getBalance(userId);
    }

    // deposit
    public Balance deposit(Long userId, double amount) {
        Optional<Balance> current = dao.getBalance(userId);
        double newAmount = current.map(b -> b.getAmount() + amount).orElse(amount);
        return dao.updateBalance(userId, newAmount);
    }

    // withdraw
    public Optional<String> withdraw(Long userId, double amount) {
        Optional<Balance> current = dao.getBalance(userId);

        if (current.isEmpty()) return Optional.of("No balance found");

        double currentAmount = current.get().getAmount();
        if (currentAmount < amount) return Optional.of("Insufficient funds");

        dao.updateBalance(userId, currentAmount - amount);
        return Optional.empty();
    }
}
