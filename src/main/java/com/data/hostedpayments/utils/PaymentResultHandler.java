package com.data.hostedpayments.utils;


import android.app.Activity;
import android.content.Intent;

import com.data.hostedpayments.ResponseConfig;

public final class PaymentResultHandler {

    private PaymentResultHandler() {}

    public static void success(Activity activity, String response) {
        ResponseConfig.startTrxn = false;
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", response);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static void failure(Activity activity, String response) {
        ResponseConfig.startTrxn = false;
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", response);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        activity.finish();
    }

    public static void cancel(Activity activity, String response) {
        ResponseConfig.startTrxn = false;
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", response);
        activity.setResult(Activity.RESULT_CANCELED, intent);
        activity.finish();
    }
}
