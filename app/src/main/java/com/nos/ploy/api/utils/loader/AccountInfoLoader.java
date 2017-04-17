package com.nos.ploy.api.utils.loader;

import android.content.Context;

import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;
import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.cache.SharePreferenceUtils;
import com.nos.ploy.cache.UserTokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Saran on 8/12/2559.
 */

public class AccountInfoLoader {

    public static void getAccountGson(final Context context, long userId, final Action1<AccountGson.Data> onFinish) {
        getAccountGson(context, userId, false, onFinish);
    }

    public static void getAccountGson(final Context context, long userId, boolean forceRefresh, final Action1<AccountGson.Data> onFinish) {
        AccountGson.Data data = SharePreferenceUtils.getAccountGson(context);
        Call<AccountGson> call = RetrofitManager.getRetrofit(context).create(AccountApi.class).getAccountGson(userId);
        final Callback<AccountGson> callbackSaveCache = new Callback<AccountGson>() {
            @Override
            public void onResponse(Call<AccountGson> call, Response<AccountGson> response) {
                if (response.isSuccessful() && null != response.body()) {
                    SharePreferenceUtils.saveAccountGson(context, response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<AccountGson> call, Throwable t) {

            }
        };
        if (data != null && !forceRefresh) {
            onFinish.call(data);
            call.enqueue(callbackSaveCache);
        } else {
            call.enqueue(new Callback<AccountGson>() {
                @Override
                public void onResponse(Call<AccountGson> call, Response<AccountGson> response) {
                    callbackSaveCache.onResponse(call, response);
                    if (null != onFinish) {
                        AccountGson.Data data = SharePreferenceUtils.getAccountGson(context);
                        if (null == data && null != response.body() && null != response.body().getData()) {
                            data = response.body().getData();
                        }
                        onFinish.call(data);
                    }
                }

                @Override
                public void onFailure(Call<AccountGson> call, Throwable t) {
                    callbackSaveCache.onFailure(call, t);
                }
            });
        }
    }

    public static void getProfileImage(final Context context, long userId, final Action1<List<ProfileImageGson.Data>> onFinish) {
        getProfileImage(context, userId, false, onFinish);
    }

    public static void getProfileImage(final Context context, final long userId, boolean forceRefresh, final Action1<List<ProfileImageGson.Data>> onFinish) {
        List<ProfileImageGson.Data> results = SharePreferenceUtils.getProfileImages(context);
        Call<ProfileImageGson> call = RetrofitManager.getRetrofit(context).create(AccountApi.class).getProfileImage(userId);
        final Callback<ProfileImageGson> callbackSaveCache = new Callback<ProfileImageGson>() {
            @Override
            public void onResponse(Call<ProfileImageGson> call, Response<ProfileImageGson> response) {
                if (response.isSuccessful() && null != response.body()) {
                    UserTokenGson.Data token = UserTokenManager.getToken(context);
                    if (token != null && null != token.getUserId() && token.getUserId() == userId) {
                        SharePreferenceUtils.saveProfileImageGson(context, response.body());
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileImageGson> call, Throwable t) {

            }
        };
        if (results != null && !results.isEmpty() && !forceRefresh) {
            onFinish.call(results);
            call.enqueue(callbackSaveCache);
        } else {
            call.enqueue(new Callback<ProfileImageGson>() {
                @Override
                public void onResponse(Call<ProfileImageGson> call, Response<ProfileImageGson> response) {
                    callbackSaveCache.onResponse(call, response);
                    if (null != onFinish) {
                        List<ProfileImageGson.Data> data = SharePreferenceUtils.getProfileImages(context);
                        if (null == data) {
                            data = response.body().getData();
                        }
                        onFinish.call(data);
                    }
                }

                @Override
                public void onFailure(Call<ProfileImageGson> call, Throwable t) {
                    callbackSaveCache.onFailure(call, t);
                }
            });
        }
    }

}
