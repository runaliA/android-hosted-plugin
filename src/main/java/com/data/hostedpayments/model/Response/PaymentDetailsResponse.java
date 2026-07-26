package com.data.hostedpayments.model.Response;

import com.google.gson.annotations.SerializedName;

public class PaymentDetailsResponse {

    @SerializedName("3dsChanllengeResponse")
    private ThreeDSChallengeResponse threeDSChallengeResponse;

    private String identifierFlag;

    private String transactionId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ThreeDSChallengeResponse getThreeDSChallengeResponse() {
        return threeDSChallengeResponse;
    }

    public void setThreeDSChallengeResponse(ThreeDSChallengeResponse threeDSChallengeResponse) {
        this.threeDSChallengeResponse = threeDSChallengeResponse;
    }

    public String getIdentifierFlag() {
        return identifierFlag;
    }

    public void setIdentifierFlag(String identifierFlag) {
        this.identifierFlag = identifierFlag;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private String status;

}
