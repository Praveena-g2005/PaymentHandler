package com.paymenthandler.payment;

import com.paymenthandler.model.Payment;
import com.paymenthandler.model.PaymentResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("card")
public class CardPaymentHandler implements PaymentHandler {
    @Override
    public PaymentResponse handle(Payment payment){
        // Dummy processing logic (simulate card gateway)
        System.out.println("[CARD] processing payment: " + payment);
        // accept all card payments in this demo
        return new PaymentResponse(true, "Card charged successfully", null);
    }
    @Override public String getMethod(){ return "card"; }
}
