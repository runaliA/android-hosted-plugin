package com.data.hostedpayments.Network;



import com.data.hostedpayments.model.Request.AuthorizationRequest;
import com.data.hostedpayments.model.Request.PaymentRequest;
import com.data.hostedpayments.model.Request.PaymentDetailsRequest;
import com.data.hostedpayments.model.Response.AuthorizationResponse;
import com.data.hostedpayments.model.Response.CardBrandResponse;
import com.data.hostedpayments.model.Response.InitiateTransactionResponse;
import com.data.hostedpayments.model.Response.SupportedPaymentResponse;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentApiService {

    @POST("v2/payments/pay-request")
    Call<InitiateTransactionResponse> initiateTransaction(
            @Body PaymentRequest request
    );

    @POST("api/v1/sdk/processInAppTransaction.htm")
    Call<ResponseBody> submitPaymentDetails(
            @Body PaymentDetailsRequest request
    );

//    @POST("authorize")
//    Call<AuthorizationResponse> authorizeTransaction(
//            @Body AuthorizationRequest request
//    );
    @POST("api/v1/sdk/confirmCvvTran.htm")
    Call<ResponseBody> confirmCvvTransaction(
            @Body PaymentDetailsRequest request
    );
    @POST("api/v1/sdk/getSupportedPaymentMethod.htm")
    Call<SupportedPaymentResponse> getSupportedPaymentMethods(
            @Header("requestHeader") String requestHeader
    );
    @POST("api/v1/sdk/getCardBrandDetails.htm")
    Call<CardBrandResponse> getCardBrandDetails(
            @Query("cardBin") String cardBin
    );
}