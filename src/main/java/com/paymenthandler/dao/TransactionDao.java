
package com.paymenthandler.dao;

import com.paymenthandler.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionDao {
    Transaction save(Transaction tx);
    Optional<Transaction> findById(Long id);
    List<Transaction> findByUserId(Long userId);
}
