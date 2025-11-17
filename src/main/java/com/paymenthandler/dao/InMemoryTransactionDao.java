
package com.paymenthandler.dao;

import com.paymenthandler.model.Transaction;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InMemoryTransactionDao implements TransactionDao {
    private final Map<Long, Transaction> store = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public Transaction save(Transaction tx) {
        Long id = idGen.getAndIncrement();
        Transaction t = new Transaction(id, tx.getPayerId(), tx.getPayeeId(), tx.getAmount(), tx.getMethod(), tx.getCreatedAt());
        store.put(id, t);
        return t;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t : store.values()) {
            if (Objects.equals(t.getPayerId(), userId) || Objects.equals(t.getPayeeId(), userId)) list.add(t);
        }
        return list;
    }
}
