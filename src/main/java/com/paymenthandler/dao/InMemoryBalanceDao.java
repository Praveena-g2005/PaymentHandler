package com.paymenthandler.dao;

import java.util.*;
import com.paymenthandler.model.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("balanceDao")
public class InMemoryBalanceDao implements BalanceDao {

    private Map<Long, Double> store = new HashMap<>();

    @Override
    public Optional<Balance> getBalance(Long userId) {
        return store.containsKey(userId)
            ? Optional.of(new Balance(userId, store.get(userId)))
            : Optional.empty();
    }

    @Override
    public Balance updateBalance(Long userId, double amount) {
        store.put(userId, amount);
        return new Balance(userId, amount);
    }
}
