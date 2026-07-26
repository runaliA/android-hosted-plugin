package com.data.hostedpayments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.data.hostedpayments.model.Request.AuthorizationRequest;
import com.data.hostedpayments.model.Request.PaymentRequest;
import com.data.hostedpayments.model.Request.PaymentDetailsRequest;
import com.data.hostedpayments.model.Response.AuthorizationResponse;
import com.data.hostedpayments.model.Response.CardBrandResponse;
import com.data.hostedpayments.model.Response.InitiateTransactionResponse;
import com.data.hostedpayments.model.Response.PaymentDetailsResponse;
import com.data.hostedpayments.model.Response.SupportedPaymentResponse;
import com.data.hostedpayments.repository.PaymentRepository;
import com.google.gson.JsonObject;


public class PaymentViewModel extends ViewModel {

    private final PaymentRepository repository =
            new PaymentRepository();

    public LiveData<InitiateTransactionResponse>
    initiateTransaction(
            PaymentRequest request) {

        return repository.initiateTransaction(
                request
        );
    }
    public LiveData<CardBrandResponse> getCardBrandDetails(String cardBin) {
        return repository.getCardBrandDetails(cardBin);
    }
    public LiveData<String> submitPaymentDetails(
            PaymentDetailsRequest request) {

        return repository.submitPaymentDetails(request);
    }

    public LiveData<String> submitPayment(
            PaymentDetailsRequest request,
            boolean isTokenized) {

        return repository.submitPayment(request, isTokenized);
    }



//    public LiveData<SupportedPaymentResponse> getSupportedPaymentMethods(
//            String terminalId,
//            String password) {
//
//        return repository.getSupportedPaymentMethods(
//                terminalId,
//                password);
//    }
}