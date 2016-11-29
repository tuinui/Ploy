package com.nos.ploy.api.base;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saran on 29/11/2559.
 */

public class RetrofitManager {

    public static final String HTTP_HOST_PRODUCTION = "http://52.220.224.43:8080";

    public static Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(HTTP_HOST_PRODUCTION);
        builder.client(getOkHttpClient());
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
    }
}
