
package com.paymenthandler.dao;

import com.paymenthandler.model.Balance;
import java.util.Optional;

public interface BalanceDao {
    Optional<Balance> getBalance(Long userId);
    Balance updateBalance(Long userId, double amount);
}
