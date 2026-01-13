
package com.paymenthandler.payment;

import com.paymenthandler.dto.response.PaymentResponse;
import com.paymenthandler.model.Payment;

public interface PaymentHandler {

    PaymentResponse handle(Payment payment);
    
    String getMethod(); //Returns method (card , upi or wallet)
}
