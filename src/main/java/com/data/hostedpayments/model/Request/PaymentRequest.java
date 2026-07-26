package com.data.hostedpayments.model.Request;


public class PaymentRequest {

    private final String amount;
    private final String transactionType;
    private final String currency;
    private final String email;
    private final String address;
    private final String city;
    private final String state;
    private final String zip;
    private final String countryCode;
    private final String trackId;
    private final String cardOperation;
    private final String cardToken;
    private final String tokenType;
    private final String transactionId;
    private final String metadata;

    private PaymentRequest(Builder builder) {
        this.amount = builder.amount;
        this.transactionType = builder.transactionType;
        this.currency = builder.currency;
        this.email = builder.email;
        this.address = builder.address;
        this.city = builder.city;
        this.state = builder.state;
        this.zip = builder.zip;
        this.countryCode = builder.countryCode;
        this.trackId = builder.trackId;
        this.cardOperation = builder.cardOperation;
        this.cardToken = builder.cardToken;
        this.tokenType = builder.tokenType;
        this.transactionId = builder.transactionId;
        this.metadata = builder.metadata;
    }

    public String getAmount() { return amount; }
    public String getTransactionType() { return transactionType; }
    public String getCurrency() { return currency; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
    public String getCountryCode() { return countryCode; }
    public String getTrackId() { return trackId; }
    public String getCardOperation() { return cardOperation; }
    public String getCardToken() { return cardToken; }
    public String getTokenType() { return tokenType; }
    public String getTransactionId() { return transactionId; }
    public String getMetadata() { return metadata; }

    public static class Builder {
        private String amount = "";
        private String transactionType = "";
        private String currency = "";
        private String email = "";
        private String address = "";
        private String city = "";
        private String state = "";
        private String zip = "";
        private String countryCode = "";
        private String trackId = "";
        private String cardOperation = "";
        private String cardToken = "";
        private String tokenType = "0";
        private String transactionId = "";
        private String metadata = "{}";

        public Builder setAmount(String amount) {
            this.amount = safe(amount);
            return this;
        }

        public Builder setTransactionType(String transactionType) {
            this.transactionType = safe(transactionType);
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = safe(currency);
            return this;
        }

        public Builder setEmail(String email) {
            this.email = safe(email);
            return this;
        }

        public Builder setAddress(String address) {
            this.address = safe(address);
            return this;
        }

        public Builder setCity(String city) {
            this.city = safe(city);
            return this;
        }

        public Builder setState(String state) {
            this.state = safe(state);
            return this;
        }

        public Builder setZip(String zip) {
            this.zip = safe(zip);
            return this;
        }

        public Builder setCountryCode(String countryCode) {
            this.countryCode = safe(countryCode);
            return this;
        }

        public Builder setTrackId(String trackId) {
            this.trackId = safe(trackId);
            return this;
        }

        public Builder setCardOperation(String cardOperation) {
            this.cardOperation = safe(cardOperation);
            return this;
        }

        public Builder setCardToken(String cardToken) {
            this.cardToken = safe(cardToken);
            return this;
        }

        public Builder setTokenType(String tokenType) {
            this.tokenType = safe(tokenType);
            return this;
        }

        public Builder setTransactionId(String transactionId) {
            this.transactionId = safe(transactionId);
            return this;
        }

        public Builder setMetadata(String metadata) {
            this.metadata = safe(metadata);
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(this);
        }

        private static String safe(String value) {
            return value == null ? "" : value;
        }
    }
}
