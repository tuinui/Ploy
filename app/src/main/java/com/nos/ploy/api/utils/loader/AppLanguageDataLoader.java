package com.nos.ploy.api.utils.loader;

import android.content.Context;

import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.AppLanguageGson;
import com.nos.ploy.cache.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Saran on 3/1/2560.
 */

public class AppLanguageDataLoader {

    public static void getAppLanguageList(final Context context, boolean forceRefresh, final Action1<ArrayList<AppLanguageGson.Data>> onFinish) {
        ArrayList<AppLanguageGson.Data> data = SharePreferenceUtils.getAppLanguageList(context);
        Call<AppLanguageGson> call = RetrofitManager.getRetrofit(context).create(MasterApi.class).getAppLanguageActiveList();
        final Callback<AppLanguageGson> callbackSaveCache = new Callback<AppLanguageGson>() {
            @Override
            public void onResponse(Call<AppLanguageGson> call, Response<AppLanguageGson> response) {
                if (response.isSuccessful() && null != response.body()) {
                    SharePreferenceUtils.saveAppLanguageGson(context, response.body());
                }
            }

            @Override
            public void onFailure(Call<AppLanguageGson> call, Throwable t) {

            }
        };
        if (data != null && !forceRefresh) {
            onFinish.call(data);
            call.enqueue(callbackSaveCache);
        } else {
            call.enqueue(new Callback<AppLanguageGson>() {
                @Override
                public void onResponse(Call<AppLanguageGson> call, Response<AppLanguageGson> response) {
                    callbackSaveCache.onResponse(call, response);
                    if (null != onFinish) {
                        ArrayList<AppLanguageGson.Data> data = SharePreferenceUtils.getAppLanguageList(context);
                        if (null == data) {
                            data = response.body().getData();
                        }
                        onFinish.call(data);
                    }
                }

                @Override
                public void onFailure(Call<AppLanguageGson> call, Throwable t) {
                    callbackSaveCache.onFailure(call, t);
                }
            });
        }
    }
}
