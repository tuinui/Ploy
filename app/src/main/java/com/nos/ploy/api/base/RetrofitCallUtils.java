package com.nos.ploy.api.base;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.base.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saran on 18/12/2559.
 */

public abstract class RetrofitCallUtils<T> {
    private Call<T> mCall;

    private RetrofitCallUtils() {
    }

    public RetrofitCallUtils(Call<T> call) {
        this.mCall = call;
    }

    public static <T> RetrofitCallUtils<T> with(final Call<T> call, final RetrofitCallback<T> callback) {
        return new RetrofitCallUtils<T>(call) {

            @Override
            public void onDataSuccess(T data) {
                callback.onDataSuccess(data);
            }

            @Override
            public void onDataFailure(ResponseMessage failCause) {
                callback.onDataFailure(failCause);
            }
        };
    }

    private static boolean isReadForUiThread(Context context) {

        if (context instanceof BaseActivity) {
            return ((BaseActivity) context).isReady();
        } else {
            return context != null;
        }
    }

    public abstract void onDataSuccess(T data);

    public abstract void onDataFailure(ResponseMessage failCause);

    public void enqueue(final Context context) {
        if (null != mCall) {
            mCall.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (null != response
                            && response.isSuccessful()
                            && null != response.body()) {

                        if (response.body() instanceof BaseResponse) {
                            BaseResponse<T> baseResponse = (BaseResponse<T>) response.body();
                            if (baseResponse.isSuccess()) {

                                if (null != baseResponse.getData()) {
                                    T result = (T) response.body();
                                    if (null != result) {
                                        onDataSuccess(result);
                                    }

                                } else {
                                    onDataFailure(new ResponseMessage("null data"));
                                    showToast(context, "null data");
                                }
                            } else {
                                onDataFailure(baseResponse.getResponseMessage());
                                showToast(context, baseResponse.getErrorMessage());
                            }
                        } else if (response.body() instanceof LinkedTreeMap) {
                            LinkedTreeMap data = (LinkedTreeMap) response.body();
                            if (null != data.get("responseMsg") && data.get("responseMsg") instanceof LinkedTreeMap && ((LinkedTreeMap) data.get("responseMsg")).get("msgCode") instanceof String) {
                                String messageCode = (String) ((LinkedTreeMap) data.get("responseMsg")).get("msgCode");
                                if (TextUtils.equals(messageCode, "000")) {
                                    onDataSuccess(null);
                                } else {
                                    showToast(context, "isNotSuccessful");
                                    onDataFailure(new ResponseMessage("isNotSuccessful"));
                                }
                            }
                        }

                    } else {
                        showToast(context, "isNotSuccessful");
                        onDataFailure(new ResponseMessage("isNotSuccessful"));
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    if (null != t && null != t.getMessage()) {
                        onDataFailure(new ResponseMessage());
                        showToast(context, t.getMessage());
                    }
                }
            });
        }
    }

    private void showToast(Context context, String message) {
        if (isReadForUiThread(context)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }


    public static interface RetrofitCallback<T> {
        void onDataSuccess(T data);

        void onDataFailure(ResponseMessage failCause);
    }


}
