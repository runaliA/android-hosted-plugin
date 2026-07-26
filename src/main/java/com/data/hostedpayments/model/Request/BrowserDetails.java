package com.data.hostedpayments.model.Request;


import com.google.gson.annotations.SerializedName;

public class BrowserDetails {

    private String transactionUuid;

    private String browserLanguage;

    private String browserScreenHeight;

    @SerializedName("browserJavascriptEnabled")
    private boolean browserJavascriptEnabled;

    private String browserColorDepth;

    @SerializedName("browserJavaEnabled")
    private boolean browserJavaEnabled;

    private String browserScreenWidth;

    private String browserAcceptHeader;

    private String browserTZ;

    private String browserUserAgent;

    public String getTransactionUuid() {
        return transactionUuid;
    }

    public void setTransactionUuid(String transactionUuid) {
        this.transactionUuid = transactionUuid;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    public void setBrowserLanguage(String browserLanguage) {
        this.browserLanguage = browserLanguage;
    }

    public String getBrowserScreenHeight() {
        return browserScreenHeight;
    }

    public void setBrowserScreenHeight(String browserScreenHeight) {
        this.browserScreenHeight = browserScreenHeight;
    }

    public boolean isBrowserJavascriptEnabled() {
        return browserJavascriptEnabled;
    }

    public void setBrowserJavascriptEnabled(boolean browserJavascriptEnabled) {
        this.browserJavascriptEnabled = browserJavascriptEnabled;
    }

    public String getBrowserColorDepth() {
        return browserColorDepth;
    }

    public void setBrowserColorDepth(String browserColorDepth) {
        this.browserColorDepth = browserColorDepth;
    }

    public boolean isBrowserJavaEnabled() {
        return browserJavaEnabled;
    }

    public void setBrowserJavaEnabled(boolean browserJavaEnabled) {
        this.browserJavaEnabled = browserJavaEnabled;
    }

    public String getBrowserScreenWidth() {
        return browserScreenWidth;
    }

    public void setBrowserScreenWidth(String browserScreenWidth) {
        this.browserScreenWidth = browserScreenWidth;
    }

    public String getBrowserAcceptHeader() {
        return browserAcceptHeader;
    }

    public void setBrowserAcceptHeader(String browserAcceptHeader) {
        this.browserAcceptHeader = browserAcceptHeader;
    }

    public String getBrowserTZ() {
        return browserTZ;
    }

    public void setBrowserTZ(String browserTZ) {
        this.browserTZ = browserTZ;
    }

    public String getBrowserUserAgent() {
        return browserUserAgent;
    }

    public void setBrowserUserAgent(String browserUserAgent) {
        this.browserUserAgent = browserUserAgent;
    }
}