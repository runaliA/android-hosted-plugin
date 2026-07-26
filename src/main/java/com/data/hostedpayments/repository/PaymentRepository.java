package com.data.hostedpayments.repository;

import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.data.hostedpayments.Interface.SupportedPaymentCallback;
import com.data.hostedpayments.Network.ApiClient;
import com.data.hostedpayments.model.Request.AuthorizationRequest;
import com.data.hostedpayments.model.Request.PaymentRequest;
import com.data.hostedpayments.model.Request.PaymentDetailsRequest;
import com.data.hostedpayments.model.Request.SupportedPaymentRequest;
import com.data.hostedpayments.model.Response.AuthorizationResponse;
import com.data.hostedpayments.model.Response.CardBrandResponse;
import com.data.hostedpayments.model.Response.InitiateTransactionResponse;
import com.data.hostedpayments.model.Response.PaymentDetailsResponse;
import com.data.hostedpayments.model.Response.SupportedPaymentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {

    public MutableLiveData<InitiateTransactionResponse>
    initiateTransaction(
            PaymentRequest request) {

        MutableLiveData<InitiateTransactionResponse>
                liveData = new MutableLiveData<>();

        ApiClient.getApiService()
                .initiateTransaction(request)
                .enqueue(
                        new Callback<InitiateTransactionResponse>() {

                            @Override
                            public void onResponse(
                                    Call<InitiateTransactionResponse> call,
                                    Response<InitiateTransactionResponse> response) {

                                liveData.setValue(
                                        response.body()
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<InitiateTransactionResponse> call,
                                    Throwable t) {

                                liveData.setValue(null);
                            }
                        });

        return liveData;
    }

    public MutableLiveData<String> submitPayment(
            PaymentDetailsRequest request,
            boolean isTokenized) {

        MutableLiveData<String> liveData = new MutableLiveData<>();

        Call<ResponseBody> call;

        if (isTokenized) {
            call = ApiClient.getApiService().confirmCvvTransaction(request);
        } else {
            call = ApiClient.getApiService().submitPaymentDetails(request);
        }

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                Log.e("API", "Response Code = " + response.code());

                try {

                    if (response.isSuccessful() && response.body() != null) {

                        String json = response.body().string();

                        Log.e("PG_RAW_RESPONSE", json);

                        liveData.setValue(json);

                    } else {

                        if (response.errorBody() != null) {
                            Log.e("API_ERROR",
                                    response.errorBody().string());
                        }

                        liveData.setValue(null);
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call,
                                  Throwable t) {

                Log.e("API_ERROR", t.getMessage());

                liveData.setValue(null);
            }
        });

        return liveData;
    }
    public void getSupportedPaymentMethods(
            String terminalId,
            String password,
            SupportedPaymentCallback callback) {

        JSONObject json = new JSONObject();

        try {
            json.put("terminalId", terminalId);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onFailure(e.getMessage());
            return;
        }
        Log.d("SUPPORTED_API", "Request JSON : " + json.toString());
        String requestHeader = Base64.encodeToString(
                json.toString().getBytes(StandardCharsets.UTF_8),
                Base64.NO_WRAP);
        Log.d("SUPPORTED_API", "Encoded Header : " + requestHeader);
        ApiClient.getApiService()
                .getSupportedPaymentMethods(requestHeader)
                .enqueue(new Callback<SupportedPaymentResponse>() {

                    @Override
                    public void onResponse(
                            Call<SupportedPaymentResponse> call,
                            Response<SupportedPaymentResponse> response) {
                        Log.d("SUPPORTED_API", "HTTP Code : " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                            Log.d("SUPPORTED_API",
                                    "Response : " + new Gson().toJson(response.body()));
                        } else {
                            callback.onFailure("Empty response");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<SupportedPaymentResponse> call,
                            Throwable t) {

                        callback.onFailure(t.getMessage());
                    }
                });
    }
//    public LiveData<SupportedPaymentResponse> getSupportedPaymentMethods(
//            String terminalId,
//            String password) {
//
//        MutableLiveData<SupportedPaymentResponse> liveData =
//                new MutableLiveData<>();
//
//        JSONObject json = new JSONObject();
//
//        try {
//            json.put("terminalId", terminalId);
//            json.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.d("SUPPORTED_API", "Request JSON : " + json.toString());
//        String requestHeader = Base64.encodeToString(
//                json.toString().getBytes(StandardCharsets.UTF_8),
//                Base64.NO_WRAP);
//        Log.d("SUPPORTED_API", "Request Header : " + requestHeader);
//        ApiClient.getApiService().getSupportedPaymentMethods(requestHeader)
//                .enqueue(new Callback<SupportedPaymentResponse>() {
//
//                    @Override
//                    public void onResponse(
//                            Call<SupportedPaymentResponse> call,
//                            Response<SupportedPaymentResponse> response) {
//                        Log.d("SUPPORTED_API",
//                                "Response : " + new Gson().toJson(response.body()));
//
//                        liveData.setValue(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(
//                            Call<SupportedPaymentResponse> call,
//                            Throwable t) {
//                        Log.e("SUPPORTED_API",
//                                "Failure : " + t.getMessage(), t);
//                        liveData.setValue(null);
//                    }
//                });
//
//        return liveData;
//    }
    public MutableLiveData<String> submitPaymentDetails(
            PaymentDetailsRequest request) {

        MutableLiveData<String> liveData = new MutableLiveData<>();

        ApiClient.getApiService()
                .submitPaymentDetails(request)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {

                        Log.e("API", "Response Code = " + response.code());
                        Log.e("API", "Success = " + response.isSuccessful());

                        try {

                            if (response.isSuccessful() && response.body() != null) {

                                String jsonResponse = response.body().string();

                                Log.e("PG_RAW_RESPONSE", jsonResponse);

                                liveData.setValue(jsonResponse);

                            } else {

                                if (response.errorBody() != null) {
                                    Log.e("API_ERROR",
                                            response.errorBody().string());
                                }

                                liveData.setValue(null);
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            liveData.setValue(null);

                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t) {

                        Log.e("API_ERROR", t.getMessage());

                        liveData.setValue(null);
                    }
                });

        return liveData;
    }
   public LiveData<CardBrandResponse> getCardBrandDetails(String cardBin) {

        MutableLiveData<CardBrandResponse> result =
                new MutableLiveData<>();

        ApiClient.getApiService().getCardBrandDetails(cardBin)
                .enqueue(new Callback<CardBrandResponse>() {

                    @Override
                    public void onResponse(Call<CardBrandResponse> call,
                                           Response<CardBrandResponse> response) {
                        Log.d("CARD_BRAND_API",
                                "HTTP Code : " + response.code());

                        Log.d("CARD_BRAND_API",
                                "Response : " + new Gson().toJson(response.body()));

                        result.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<CardBrandResponse> call,
                                          Throwable t) {
                        Log.e("CARD_BRAND_API",
                                "Error : " + t.getMessage());

                        result.setValue(null);
                    }
                });

        return result;
    }
}