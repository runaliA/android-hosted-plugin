package com.data.hostedpayments.Interface;

import com.data.hostedpayments.model.Response.AuthorizationResponse;

public interface PaymentCallback {

    void onResult(String response);
}

