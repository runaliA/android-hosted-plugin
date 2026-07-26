package com.data.hostedpayments.model.Request;



public class Card {

    private String number;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;

    public String getCardToken() {
        return CardToken;
    }

    public void setCardToken(String cardToken) {
        CardToken = cardToken;
    }

    private String CardToken;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }
}