package com.afrobiz.afrobizfind;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.afrobiz.afrobizfind.braintree.Settings;
import com.afrobiz.afrobizfind.braintree.internal.ApiClient;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class DemoApplication extends Application implements UncaughtExceptionHandler
{
    private static ApiClient sApiClient;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    @Override
    public void onCreate()
    {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/GT-Walsheim-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        super.onCreate();

        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        Log.e("Exception", "Uncaught Exception", ex);
        mDefaultExceptionHandler.uncaughtException(thread, ex);
    }

    public static ApiClient getClient_withAuth(final String token, Context context)
    {
        Log.e("token" , ""+token);
        if (sApiClient == null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor()
                    {
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

            sApiClient = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Settings.getEnvironmentUrl(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(ApiClient.class);
        }
        return sApiClient;
    }
    public static void resetApiClient()
    {
        sApiClient = null;
    }
}
