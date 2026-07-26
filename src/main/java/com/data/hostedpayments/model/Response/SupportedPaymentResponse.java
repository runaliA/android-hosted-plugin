package com.data.hostedpayments.model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupportedPaymentResponse {

    @SerializedName("supportedPaymentModes")
    private List<String> supportedPaymentModes;

    public List<String> getSupportedPaymentModes() {
        return supportedPaymentModes;
    }
}
