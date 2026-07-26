package com.data.hostedpayments;

import android.app.Activity;
import android.util.Log;

import com.data.hostedpayments.Interface.PaymentCallback;
import com.data.hostedpayments.Theme.PaymentTheme;
import com.data.hostedpayments.model.Request.PaymentRequest;
import com.data.hostedpayments.model.SDKConfiguration;

public class PaymentSDK {

    private static SDKConfiguration configuration;
    private static PaymentSDK instance;
    private PaymentCallback callback;

    private PaymentSDK() {}

    public static synchronized PaymentSDK getInstance() {
        if (instance == null) {
            instance = new PaymentSDK();
        }
        return instance;
    }

    public void setCallback(PaymentCallback callback) {
        this.callback = callback;
    }

    public PaymentCallback getCallback() {
        return callback;
    }
    public static void initialize(SDKConfiguration configuration){

        instance.configuration = configuration;

    }
    public static SDKConfiguration getConfiguration() {
        return configuration;
    }


    public void startPayment(
            Activity activity,
            PaymentRequest request,
            PaymentCallback callback
    ) throws Exception {
        this.callback = callback;

        TrxnPayments.makepaymentService(
                request.getAmount(),
                activity,
                request.getTransactionType(),
                request.getCurrency(),
                request.getEmail(),
                request.getAddress(),
                request.getCity(),
                request.getState(),
                request.getZip(),
                request.getCountryCode(),
                request.getTrackId(),
                request.getCardOperation(),
                request.getCardToken(),
                request.getTokenType(),
                request.getTransactionId(),
                request.getMetadata()
        );
    }


    public void notifyResult(String response) {

        Log.e("SDK_FLOW", "notifyResult called");

        if (callback == null) {
            Log.e("SDK_FLOW", "Callback is NULL");
        } else {
            Log.e("SDK_FLOW", "Callback is NOT NULL");
            callback.onResult(response);
        }
    }
}
