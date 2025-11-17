package com.paymenthandler.service;

import com.paymenthandler.model.*;
import com.paymenthandler.payment.PaymentHandler;
import com.paymenthandler.dao.*;
import com.paymenthandler.service.BalanceService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.enterprise.inject.Instance;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {

    @Inject
    private BalanceService balanceService; // withdraw/deposit logic

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private Instance<PaymentHandler> handlers; // all available handlers

    /**
     * process payment request:
     *  - check handler by method
     *  - attempt withdraw from balance (if wallet-like) or just simulate
     *  - create transaction entry
     */
    public PaymentResponse process(PaymentRequest request) {
        // find handler
        PaymentHandler selected = null;
        for (PaymentHandler h : handlers) {
            if (h.getMethod().equalsIgnoreCase(request.getMethod())) { selected = h; break; }
        }
        if (selected == null) return new PaymentResponse(false, "Unsupported payment method: " + request.getMethod(), null);

        // for demo: if method == "wallet", require balance withdraw; otherwise assume external gateway charges succeed
        if ("wallet".equalsIgnoreCase(request.getMethod())) {
            Optional<String> withdrawErr = balanceService.withdraw(request.getPayerUserId(), request.getAmount());
            if (withdrawErr.isPresent()) {
                return new PaymentResponse(false, withdrawErr.get(), null);
            }
        }

        // build Payment object
        Payment payment = Payment.builder()
                .payerUserId(request.getPayerUserId())
                .payeeUserId(request.getPayeeUserId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .createdAt(Instant.now())
                .build();

        // delegate actual processing to handler (simulated)
        PaymentResponse response = selected.handle(payment);

        // persist transaction if success
        if (response.isSuccess()) {
            Transaction tx = new Transaction(null, payment.getPayerUserId(), payment.getPayeeUserId(), payment.getAmount(), payment.getMethod(), payment.getCreatedAt());
            Transaction saved = transactionDao.save(tx);
            return new PaymentResponse(true, response.getMessage(), saved.getId());
        } else {
            return response;
        }
    }
}
