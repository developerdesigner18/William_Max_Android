package com.afrobiz.afrobizfind.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String TAG = "ApiClient";
    public static final String BASE_URL = "https://dev.afrobizfind.com/api/";
//    public static final String BASE_URL = "https://afrobizfind.com/api/";
//    public static final String BASE_URL = "http:192.168.1.6//:8080/api/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            Log.e(TAG, "getClient: base url " + retrofit.baseUrl());
        }
        return retrofit;
    }
    public static Retrofit getClient_withAuth(final String token)
    {
        Log.e("token" , ""+token);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException
                    {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }
    public static  ApiInterface create_InstanceAuth(String token)
    {
        Retrofit r = getClient_withAuth(token);
        if(r != null)
        {
            ApiInterface obj = r.create(ApiInterface.class);

            Log.e("objinter" , ""+obj.toString());
            return  obj;

        }
        return null;
    }
    public static  ApiInterface create_Istance()
    {
        Retrofit r = getClient();
        if(r != null)
        {
            ApiInterface obj = retrofit.create(ApiInterface.class);

            Log.e("objinter" , ""+obj.toString());
            return  obj;

        }
        return null;

    }

    public static boolean isNetworkAvailable(Context con)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
