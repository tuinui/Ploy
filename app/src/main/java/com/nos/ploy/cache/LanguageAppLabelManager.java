package com.nos.ploy.cache;

import android.content.Context;

import com.nos.ploy.api.base.RetrofitManager;
import com.nos.ploy.api.masterdata.MasterApi;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;
import com.nos.ploy.api.masterdata.model.LanguageAppLabelGson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by Saran on 21/1/2560.
 */

public class LanguageAppLabelManager {

    public static void forceRefreshLanguageLabel(final Context context, final Action1<LanguageAppLabelGson.Data> onFinish) {
        final String lgCode = SharePreferenceUtils.getCurrentActiveAppLanguageCode(context);
        MasterApi api = RetrofitManager.getRetrofit(context).create(MasterApi.class);
        api.getLanguageAppLabel(lgCode).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && null != response.body()) {
                    try {
                        SharePreferenceUtils.saveAppLanguageLabelGson(context, response.body().string(), lgCode);
                        LanguageAppLabelGson result = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
                        if(null != result && null != result.getData()){
                            if(null != onFinish){
                                onFinish.call(result.getData());
                            }

                        }else{
                            if(null != onFinish){
                                onFinish.call(null);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    LanguageAppLabelGson result = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
                    if(null != result && null != result.getData()){
                        if(null != onFinish){
                            onFinish.call(result.getData());
                        }
                    }else{
                        if(null != onFinish){
                            onFinish.call(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LanguageAppLabelGson result = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
                if(null != result && null != result.getData()){
                    if(null != onFinish){
                        onFinish.call(result.getData());
                    }
                }else{
                    if(null != onFinish){
                        onFinish.call(null);
                    }
                }
            }
        });
//        RetrofitCallUtils.with(api.getLanguageAppLabel(lgCode), new RetrofitCallUtils.RetrofitCallback<ResponseBody>() {
//            @Override
//            public void onDataSuccess(ResponseBody data) {
//                if (null != data) {
//                    try {
//                        SharePreferenceUtils.saveAppLanguageLabelGson(context, data.string(), lgCode);
//                        LanguageAppLabelGson result = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
//                        if(null != result && null != result.getData()){
//                            if(null != onFinish){
//                                onFinish.call(result.getData());
//                            }
//
//                        }else{
//                            if(null != onFinish){
//                                onFinish.call(null);
//                            }
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onDataFailure(String failCause) {
//                LanguageAppLabelGson result = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
//                if(null != result && null != result.getData()){
//                    if(null != onFinish){
//                        onFinish.call(result.getData());
//                    }
//                }else{
//                    if(null != onFinish){
//                        onFinish.call(null);
//                    }
//                }
//            }
//        });
    }

    public static void getLanguageLabel(final Context context,final Action1<LanguageAppLabelGson.Data> onFinish){
        final String lgCode = SharePreferenceUtils.getCurrentActiveAppLanguageCode(context);
        LanguageAppLabelGson data = SharePreferenceUtils.getAppLanguageLabelGson(context, lgCode);
        if(null != data && null != data.getData()){
            onFinish.call(data.getData());
        }else{
            forceRefreshLanguageLabel(context,onFinish);
        }
    }
}
