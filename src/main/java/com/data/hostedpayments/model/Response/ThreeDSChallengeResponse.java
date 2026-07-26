package com.data.hostedpayments.model.Response;

public class ThreeDSChallengeResponse {

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }

    public String getRedirectHtml() {
        return redirectHtml;
    }

    public void setRedirectHtml(String redirectHtml) {
        this.redirectHtml = redirectHtml;
    }

    private String acsUrl;

    private String redirectHtml;

}
