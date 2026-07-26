package com.data.hostedpayments.model.Response;

public class AuthorizationResponse {
    private String transactionId;
    private String responseCode;
    private String result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getResult() {
        return result;
    }

    public String getResponseCode() {
        return responseCode;
    }
}
