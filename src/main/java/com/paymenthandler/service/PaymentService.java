package com.paymenthandler.service;

import com.paymenthandler.model.*;
import com.paymenthandler.payment.PaymentHandler;
import com.paymenthandler.dao.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.enterprise.inject.Instance;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {

    @Inject
    private BalanceService balanceService; 

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private Instance<PaymentHandler> handlers; 

    public PaymentResponse process(PaymentRequest request) {
        PaymentHandler selected = null;
        for (PaymentHandler h : handlers) {
            if (h.getMethod().equalsIgnoreCase(request.getMethod())) {
                selected = h;
                break;
            }
        }
        if (selected == null)
            return new PaymentResponse(false, "Unsupported payment method: " + request.getMethod(), null);

        if ("wallet".equalsIgnoreCase(request.getMethod())) {
            Optional<String> transferErr = balanceService.transferBalance(
                request.getPayerUserId(),
                request.getPayeeUserId(),
                request.getAmount()
            );
            if (transferErr.isPresent()) {
                return new PaymentResponse(false, transferErr.get(), null);
            }
        }

        Payment payment = Payment.builder()
                .payerUserId(request.getPayerUserId())
                .payeeUserId(request.getPayeeUserId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .createdAt(Instant.now())
                .build();

        PaymentResponse response = selected.handle(payment);

        if (response.isSuccess()) {
            Transaction tx = new Transaction(null, payment.getPayerUserId(), payment.getPayeeUserId(),
                    payment.getAmount(), payment.getMethod(), payment.getCreatedAt());
            Transaction saved = transactionDao.save(tx);
            return new PaymentResponse(true, response.getMessage(), saved.getId());
        } else {
            return response;
        }
    }
}
