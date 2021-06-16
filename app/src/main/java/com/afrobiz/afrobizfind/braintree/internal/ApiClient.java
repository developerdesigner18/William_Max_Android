package com.afrobiz.afrobizfind.braintree.internal;

import com.afrobiz.afrobizfind.braintree.models.ClientToken;
import com.afrobiz.afrobizfind.ui.modal.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient
{
    @GET("gettoken")
    Call<ClientToken> getClientToken();

    @POST("make")
    Call<Response> CreateTransaction(@Query("nonce") String nonce, @Query("id") int companyId);
}
