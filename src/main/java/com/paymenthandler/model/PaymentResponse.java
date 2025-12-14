
package com.paymenthandler.model;

public class PaymentResponse {
    private final boolean success;
    private final String message;
    private final Long transactionId; 

    public PaymentResponse(boolean success, String message, Long transactionId){
        this.success = success; this.message = message; this.transactionId = transactionId;
    }

    public boolean isSuccess(){ return success; }
    public String getMessage(){ return message; }
    public Long getTransactionId(){ return transactionId; }

    @Override public String toString(){
        return "PaymentResponse{success=" + success + ", message='" + message + "', txId=" + transactionId + "}";
    }
}
