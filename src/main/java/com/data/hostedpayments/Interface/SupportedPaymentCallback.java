package com.data.hostedpayments.Interface;

import com.data.hostedpayments.model.Response.SupportedPaymentResponse;

public interface SupportedPaymentCallback {
    void onSuccess(SupportedPaymentResponse response);
    void onFailure(String error);
}
