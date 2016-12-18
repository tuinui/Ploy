package com.nos.ploy.api.base.response;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.nos.ploy.base.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Saran on 18/12/2559.
 */

public abstract class CallbackUtils<T> implements Callback<BaseResponse<T>> {


    private final Context mContext;

    public CallbackUtils(Context context) {
        this.mContext = context;
    }

    private boolean isReadForUiThread() {

        if (mContext instanceof BaseActivity) {
            return ((BaseActivity) mContext).isReady();
        } else {
            return mContext != null;
        }
    }

    public abstract void onDataSuccess(BaseResponse<T> data);

    public abstract void onDataFailure(String failCause);

    protected void showToast(String message) {
        if (isReadForUiThread()) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (null != response && response.isSuccessful() && null != response.body() && response.body() != null) {
            BaseResponse<T> baseResponse = response.body();
            if (baseResponse.isSuccess()) {
                if (null != ((BaseResponse) response.body()).getData()) {
                    onDataSuccess(baseResponse);
                } else {
                    onDataFailure("null data");
                    if (isReadForUiThread()) {
                        showToast("null data");
                    }
                }
            } else {
                onDataFailure(baseResponse.getErrorMessage());
            }
        } else {
            onDataFailure("isNotSuccessful");
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        if (null != t && null != t.getCause()) {
            onDataFailure(t.getMessage());
        }
    }


}
