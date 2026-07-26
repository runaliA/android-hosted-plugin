package com.data.hostedpayments.model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardBrandResponse {

    @SerializedName("1")
    private List<String> values;

    public List<String> getValues() {
        return values;
    }

    public String getPaymentMethod() {

        if (values != null && values.size() > 10) {
            return values.get(10);
        }

        return "CCI";
    }
}