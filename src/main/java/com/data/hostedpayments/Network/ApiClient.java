package com.data.hostedpayments.Network;

import android.util.Log;

import com.data.hostedpayments.utils.ConstantsVar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static PaymentApiService getApiService() {

        String baseUrl = ConstantsVar.appUrl;

        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        Log.e("BASE_URL", baseUrl);

        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PaymentApiService.class);
    }
}