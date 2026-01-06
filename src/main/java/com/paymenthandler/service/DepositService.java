package com.paymenthandler.service;

import com.paymenthandler.dao.TransactionDao;
import com.paymenthandler.model.Balance;
import com.paymenthandler.model.DepositRequest;
import com.paymenthandler.model.DepositResponse;
import com.paymenthandler.model.Transaction;
import com.paymenthandler.payment.gateway.PaymentGateway;
import com.paymenthandler.payment.gateway.PaymentGatewayResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;

@Singleton
public class DepositService {

    private final PaymentGateway paymentGateway;
    private final BalanceService balanceService;
    private final TransactionDao transactionDao;

    @Inject
    public DepositService(PaymentGateway paymentGateway, BalanceService balanceService,
                         TransactionDao transactionDao) {
        this.paymentGateway = paymentGateway;
        this.balanceService = balanceService;
        this.transactionDao = transactionDao;
    }

    public DepositResponse deposit(DepositRequest request) {
        PaymentGatewayResponse gatewayResponse = paymentGateway.processPayment(
            request.getAmount(),
            request.getCardNumber(),
            request.getExpiryMonth(),
            request.getExpiryYear(),
            request.getCvv(),
            request.getCardholderName()
        );

        if (!gatewayResponse.isSuccess()) {
            double currentBalance = balanceService.getBalance(request.getUserId())
                .map(Balance::getAmount)
                .orElse(0.0);
            return new DepositResponse(false, gatewayResponse.getMessage(), null, currentBalance);
        }

        Balance updatedBalance = balanceService.deposit(request.getUserId(), request.getAmount());

        double newBalance = updatedBalance.getAmount();

        Transaction transaction = new Transaction(
            null,
            request.getUserId(),
            null,
            request.getAmount(),
            "CARD_DEPOSIT",
            Instant.now(),
            0.0,
            request.getAmount()
        );

        transactionDao.save(transaction);

        return new DepositResponse(
            true,
            "Deposit successful",
            gatewayResponse.getTransactionId(),
            newBalance
        );
    }
}
