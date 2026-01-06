package com.paymenthandler.service;

import com.paymenthandler.model.*;
import com.paymenthandler.payment.PaymentHandler;
import com.paymenthandler.dao.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Singleton
public class PaymentService {

    private final BalanceService balanceService;
    private final TransactionDao transactionDao;
    private final Set<PaymentHandler> handlers;
    private final FeeService feeService;

    @Inject
    public PaymentService(Set<PaymentHandler> handlers, BalanceService balanceService,
                         TransactionDao transactionDao, FeeService feeService) {
        this.handlers = handlers;
        this.balanceService = balanceService;
        this.transactionDao = transactionDao;
        this.feeService = feeService;
    } 

    public PaymentResponse process(PaymentRequest request) {
        PaymentHandler selected = null;
        for (PaymentHandler h : handlers) {
            if (h.getMethod().equalsIgnoreCase(request.getMethod())) {
                selected = h;
                break;
            }
        }
        if (selected == null)
            return new PaymentResponse(false, "Unsupported payment method: " + request.getMethod(), null, null, null);

        FeeCalculationResult feeCalc = feeService.calculateFee(
            request.getAmount(),
            request.getMethod()
        );
        double baseAmount = feeCalc.getBaseAmount();      
        double feeAmount = feeCalc.getFeeAmount();       
        double totalAmount = feeCalc.getTotalAmount();   

        if ("wallet".equalsIgnoreCase(request.getMethod())) {
            Optional<String> transferErr = balanceService.transferBalance(
                request.getPayerUserId(),
                request.getPayeeUserId(),
                totalAmount  
            );
            if (transferErr.isPresent()) {
                return new PaymentResponse(false, transferErr.get(), null, feeAmount, totalAmount);
            }
        }

        Payment payment = Payment.builder()
                .payerUserId(request.getPayerUserId())
                .payeeUserId(request.getPayeeUserId())
                .amount(baseAmount)
                .method(request.getMethod())
                .createdAt(Instant.now())
                .build();

        PaymentResponse response = selected.handle(payment);

        if (response.isSuccess()) {
            Transaction tx = new Transaction(
                null,
                payment.getPayerUserId(),
                payment.getPayeeUserId(),
                baseAmount,
                payment.getMethod(),
                payment.getCreatedAt(),
                feeAmount,          
                totalAmount             
            );
            Transaction saved = transactionDao.save(tx);

            return new PaymentResponse(
                true,
                response.getMessage(),
                saved.getId(),
                feeAmount,
                totalAmount
            );
        } else {
            return new PaymentResponse(false, response.getMessage(), null, feeAmount, totalAmount);
        }
    }
}
