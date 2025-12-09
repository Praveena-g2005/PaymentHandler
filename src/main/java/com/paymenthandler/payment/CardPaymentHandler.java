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
        System.out.println("[CARD] processing payment: " + payment);
        return new PaymentResponse(true, "Card charged successfully", null);
    }
    @Override public String getMethod(){ return "card"; }
}
