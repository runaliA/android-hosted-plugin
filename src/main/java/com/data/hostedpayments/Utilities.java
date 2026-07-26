package com.data.hostedpayments;



import android.app.Activity;
import android.content.res.AssetManager;

import com.data.hostedpayments.model.SDKConfiguration;
import com.data.hostedpayments.utils.ConstantsVar;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilities {
    public static void getAssetData(Activity context)  {
        try {
//            AssetManager am = context.getAssets();
//            InputStream is = am.open("appconfig.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            String jsonString = new String(buffer, "UTF-8");
//            JSONObject jsonObj = null;
//
//            jsonObj = new JSONObject(jsonString);


//            ConstantsVar.merchanKey = jsonObj.get("merchantKey").toString();
//            ConstantsVar.termId= jsonObj.get("terminalId").toString();
//            ConstantsVar.termPass = jsonObj.get("terminalPass").toString();
//            ConstantsVar.appUrl = jsonObj.get("requestUrl").toString();
//            ConstantsVar.paymentDetailsUrl = jsonObj.optString("paymentDetailsUrl", "");
//            if (ConstantsVar.paymentDetailsUrl.isEmpty() && ConstantsVar.appUrl != null) {
//                ConstantsVar.paymentDetailsUrl = ConstantsVar.appUrl.replace("pay-request", "payment-details");
//            }
//            ConstantsVar.appinitiateTrxn = jsonObj.get("requestUrl").toString();


            SDKConfiguration config = PaymentSDK.getConfiguration();
            ConstantsVar.merchanKey = config.getMerchantKey();
            ConstantsVar.termId= config.getTerminalId();
            ConstantsVar.termPass = config.getPassword();
            ConstantsVar.appUrl = config.getBaseUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBuildproperties(Activity context) {
        try {
            // Open the file from assets
            InputStream inputStream = context.getAssets().open("gradle-wrapper.properties");

            // Load properties
            Properties properties = new Properties();
            properties.load(inputStream);

            // Access properties
            String distributionUrl = properties.getProperty("distributionUrl");
            ConstantsVar.buildgradleversion = distributionUrl;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
