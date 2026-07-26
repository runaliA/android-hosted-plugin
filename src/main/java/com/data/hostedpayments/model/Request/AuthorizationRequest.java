package com.data.hostedpayments.model.Request;

public class AuthorizationRequest {

    private String transactionId;
    private String authenticationStatus;
    private String eci;

    public AuthorizationRequest(
            String transactionId,
            String authenticationStatus,
            String eci) {

        this.transactionId = transactionId;
        this.authenticationStatus = authenticationStatus;
        this.eci = eci;
    }
}
