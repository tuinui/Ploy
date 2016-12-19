package com.nos.ploy.api.utils.loader;

import android.content.Context;

import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.cache.SharePreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Saran on 8/12/2559.
 */

public class AccountGsonLoader {

    public static void getAccountGson(final Context context, String userId, final Action1<AccountGson.Data> onFinish) {
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
        if (data != null) {
            onFinish.call(data);
            call.enqueue(callbackSaveCache);
        } else {
            call.enqueue(new Callback<AccountGson>() {
                @Override
                public void onResponse(Call<AccountGson> call, Response<AccountGson> response) {
                    callbackSaveCache.onResponse(call, response);
                    if (null != onFinish) {
                        AccountGson.Data data = SharePreferenceUtils.getAccountGson(context);
                        if (null == data) {
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
}
