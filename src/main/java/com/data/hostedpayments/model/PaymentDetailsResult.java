package com.data.hostedpayments.model;

public class PaymentDetailsResult {

    private final boolean successful;
    private final int httpCode;
    private final String responseBody;
    private final String errorMessage;

    private PaymentDetailsResult(boolean successful, int httpCode, String responseBody, String errorMessage) {
        this.successful = successful;
        this.httpCode = httpCode;
        this.responseBody = responseBody;
        this.errorMessage = errorMessage;
    }

    public static PaymentDetailsResult success(int httpCode, String responseBody) {
        return new PaymentDetailsResult(true, httpCode, responseBody, "");
    }

    public static PaymentDetailsResult httpError(int httpCode, String responseBody) {
        return new PaymentDetailsResult(false, httpCode, responseBody, "HTTP " + httpCode);
    }

    public static PaymentDetailsResult networkError(String errorMessage) {
        return new PaymentDetailsResult(false, 0, "", errorMessage);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
