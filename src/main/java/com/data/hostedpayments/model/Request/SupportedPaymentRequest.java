package com.data.hostedpayments.model.Request;

public class SupportedPaymentRequest {

    private String terminalId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    // getters & setters
}
