package com.paymenthandler.payment;

import com.paymenthandler.model.Payment;
import com.paymenthandler.model.PaymentResponse;

import javax.inject.Singleton;

@Singleton
public class WalletPaymentHandler implements PaymentHandler {
    @Override
    public PaymentResponse handle(Payment payment){
        System.out.println("[WALLET] processing payment: " + payment);
        return new PaymentResponse(true, "Wallet payment completed", null);
    }
    @Override public String getMethod(){ return "wallet"; }
}
