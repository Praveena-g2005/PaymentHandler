
package com.paymenthandler.payment;

import com.paymenthandler.model.Payment;
import com.paymenthandler.model.PaymentResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("upi")
public class UpiPaymentHandler implements PaymentHandler {
    @Override
    public PaymentResponse handle(Payment payment){
        System.out.println("[UPI] processing payment: " + payment);
        return new PaymentResponse(true, "UPI transfer successful", null);
    }
    @Override public String getMethod(){ return "upi"; }
}
