
package com.paymenthandler.payment;

import com.paymenthandler.model.Payment;
import com.paymenthandler.model.PaymentResponse;

public interface PaymentHandler {

    PaymentResponse handle(Payment payment);
    
    // Return the method name the handler supports, e.g. "card", "upi", "wallet"

    String getMethod();
}
