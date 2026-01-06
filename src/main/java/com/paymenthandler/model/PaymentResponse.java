
package com.paymenthandler.model;

public class PaymentResponse {
    private final boolean success;
    private final String message;
    private final Long transactionId;
    private final Double feeAmount;
    private final Double totalAmount;

    public PaymentResponse(boolean success, String message, Long transactionId,
                          Double feeAmount, Double totalAmount){
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.feeAmount = feeAmount;
        this.totalAmount = totalAmount;
    }

    public boolean isSuccess(){ return success; }
    public String getMessage(){ return message; }
    public Long getTransactionId(){ return transactionId; }
    public Double getFeeAmount(){ return feeAmount; }
    public Double getTotalAmount(){ return totalAmount; }

    @Override
    public String toString(){
        return "PaymentResponse{success=" + success + ", message='" + message + "', txId=" + transactionId +
               ", fee=" + feeAmount + ", total=" + totalAmount + "}";
    }
}
