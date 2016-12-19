package com.nos.ploy.api.base;

import android.content.Context;

import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.cache.UserTokenManager;

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

    public static final String HTTP_HOST_PRODUCTION = "http://138.68.72.222:8080";

    public static Retrofit getRetrofit(Context context) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(HTTP_HOST_PRODUCTION);
        builder.client(getOkHttpClient(context,true));
        builder.addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
    }

    private static OkHttpClient getOkHttpClient(final Context context, final boolean withAuthen) {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .method(original.method(), original.body());
                        UserTokenGson.Data data = UserTokenManager.getToken(context);
                        if(null != data){
                            builder.header("Authorization",data.getToken());
                        }


                        Response response = chain.proceed(builder.build());




                        return response;
                    }
                })
                .build();
    }
}
