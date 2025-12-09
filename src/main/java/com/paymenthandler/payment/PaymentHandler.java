
package com.paymenthandler.payment;

import com.paymenthandler.model.Payment;
import com.paymenthandler.model.PaymentResponse;

public interface PaymentHandler {
    /**
     * Process the payment.
     * Return a PaymentResponse (success/failure + message).
     */
    PaymentResponse handle(Payment payment);

    /**
     * Return the method name the handler supports, e.g. "card", "upi", "wallet"
     */
    String getMethod();
}
