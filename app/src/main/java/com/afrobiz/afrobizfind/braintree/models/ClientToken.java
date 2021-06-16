package com.afrobiz.afrobizfind.braintree.models;

import com.google.gson.annotations.SerializedName;

public class ClientToken
{
    @SerializedName("token")
    private String mClientToken;

    public String getClientToken() {
        return mClientToken;
    }
}
