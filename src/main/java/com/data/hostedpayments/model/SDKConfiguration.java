package com.data.hostedpayments.model;

import com.data.hostedpayments.Theme.PaymentTheme;

public class SDKConfiguration {

    private String terminalId;
    private String password;
    private String merchantKey;
    private String baseUrl;
    private PaymentTheme theme;

    private SDKConfiguration(Builder builder) {
        this.terminalId = builder.terminalId;
        this.password = builder.password;
        this.merchantKey = builder.merchantKey;
        this.baseUrl = builder.baseUrl;
        this.theme = builder.theme;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public String getPassword() {
        return password;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public PaymentTheme getTheme() {
        return theme;
    }

    public static class Builder {

        private String terminalId;
        private String password;
        private String merchantKey;
        private String baseUrl;
        private PaymentTheme theme;

        public Builder setTerminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setMerchantKey(String merchantKey) {
            this.merchantKey = merchantKey;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setTheme(PaymentTheme theme) {
            this.theme = theme;
            return this;
        }

        public SDKConfiguration build() {
            return new SDKConfiguration(this);
        }
    }
}