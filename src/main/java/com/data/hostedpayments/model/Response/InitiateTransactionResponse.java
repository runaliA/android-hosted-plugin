package com.data.hostedpayments.model.Response;

import java.util.List;

public class InitiateTransactionResponse {
    private String responseCode;
    private String responseMessage;
    private String transactionId;

    private List<String> paymentMethods;

    public String getTransactionId() {
        return transactionId;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }
}
