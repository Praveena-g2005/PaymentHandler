package com.paymenthandler.payment;

import com.paymenthandler.dto.response.PaymentResponse;
import com.paymenthandler.model.Payment;

import javax.inject.Singleton;

@Singleton
public class WalletPaymentHandler implements PaymentHandler {
    @Override
    public PaymentResponse handle(Payment payment){
        System.out.println("[WALLET] processing payment: " + payment);
        return new PaymentResponse(true, "Wallet payment completed", null, null, null);
    }
    @Override public String getMethod(){ return "wallet"; }
}
