package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import com.nos.ploy.api.ployee.PloyeeApi;
import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;
import com.nos.ploy.api.ployee.model.PostPloyeeServiceDetailGson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailPresenter implements PloyeeServiceDetailContract.Presenter {
    private PloyeeApi mService;
    private Long mUserId;
    private String languageCode;
    private Long mServiceId;
    private PloyeeServiceDetailContract.View mView;
    private PloyeeServiceDetailGson.Data mData;
    private Callback<PloyeeServiceDetailGson> mCallbackPloyeeServiceDetailGson = new Callback<PloyeeServiceDetailGson>() {
        @Override
        public void onResponse(Call<PloyeeServiceDetailGson> call, Response<PloyeeServiceDetailGson> response) {
            mView.setRefreshing(false);
            if (response.isSuccessful() && response.body().isSuccess()) {
                mData = response.body().getData();
                mView.bindData(mData);
            } else {
                mView.toast("isNotSuccessful");
            }
        }

        @Override
        public void onFailure(Call<PloyeeServiceDetailGson> call, Throwable t) {
            mView.setRefreshing(false);
            mView.toast("onFailure");
        }
    };

    private PloyeeServiceDetailPresenter(PloyeeApi mService, Long mUserId, String languageCode, Long mServiceId, PloyeeServiceDetailContract.View mView) {
        this.mService = mService;
        this.mUserId = mUserId;
        this.languageCode = languageCode;
        this.mServiceId = mServiceId;
        this.mView = mView;
        mView.setPresenter(this);
    }

    public static PloyeeServiceDetailPresenter inject(PloyeeApi mService, Long mUserId, String languageCode, Long mServiceId, PloyeeServiceDetailContract.View mView) {
        return new PloyeeServiceDetailPresenter(mService, mUserId, languageCode, mServiceId, mView);
    }

    @Override
    public void start() {
        if(mData == null){
            refreshData();
        }
    }

    @Override
    public void refreshData() {
        mView.setRefreshing(true);
        mService.getPloyeeServiceDetail(new PostPloyeeServiceDetailGson(mServiceId, mUserId, languageCode))
                .enqueue(mCallbackPloyeeServiceDetailGson);
    }
}
