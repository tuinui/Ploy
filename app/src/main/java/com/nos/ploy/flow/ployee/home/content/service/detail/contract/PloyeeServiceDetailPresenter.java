package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.ployer.PloyerApi;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostGetPloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostSavePloyerServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailPresenter implements PloyeeServiceDetailContract.Presenter {
    private PloyerApi mService;
    private Long mUserId;
    private String languageCode;
    private Long mServiceId;
    private PloyeeServiceDetailContract.View mView;
    private PloyerServiceDetailGson.Data mData;
    private RetrofitCallUtils.RetrofitCallback<PloyerServiceDetailGson> mCallbackGetServiceDetailGson = new RetrofitCallUtils.RetrofitCallback<PloyerServiceDetailGson>() {
        @Override
        public void onDataSuccess(PloyerServiceDetailGson data) {
            mView.setRefreshing(false);
            if (null != data && null != data.getData()) {
                mData = data.getData();
                mView.bindData(mData);
            }

        }

        @Override
        public void onDataFailure(String failCause) {
            mView.setRefreshing(false);
        }
    };

    private RetrofitCallUtils.RetrofitCallback<BaseResponse<Object>> mCallbackPostSaveServiceDetail = new RetrofitCallUtils.RetrofitCallback<BaseResponse<Object>>() {
        @Override
        public void onDataSuccess(BaseResponse<Object> data) {

            mView.saveServiceSuccess();
            if (null != data) {
                refreshData();
            }
        }

        @Override
        public void onDataFailure(String failCause) {
            mView.showLoadingDialog(false);
        }
    };

    private PloyeeServiceDetailPresenter(PloyerApi mService, Long mUserId, String languageCode, Long mServiceId, PloyeeServiceDetailContract.View mView) {
        this.mService = mService;
        this.mUserId = mUserId;
        this.languageCode = languageCode;
        this.mServiceId = mServiceId;
        this.mView = mView;
        mView.setPresenter(this);
    }

    public static PloyeeServiceDetailPresenter inject(PloyerApi mService, Long mUserId, String languageCode, Long mServiceId, PloyeeServiceDetailContract.View mView) {
        return new PloyeeServiceDetailPresenter(mService, mUserId, languageCode, mServiceId, mView);
    }

    @Override
    public void start() {
        if (mData == null) {
            refreshData();
        }
    }

    @Override
    public void refreshData() {
        mView.setRefreshing(true);
        RetrofitCallUtils
                .with(mService.getServiceDetail(new PostGetPloyerServiceDetailGson(mServiceId, mUserId, languageCode)), mCallbackGetServiceDetailGson)
                .enqueue(mView.getContext());

    }

    @Override
    public void requestSaveServiceDetail(PostSavePloyerServiceDetailGson data) {
        if (null == data) {
            return;
        }
        mView.showLoadingDialog(true);
        RetrofitCallUtils
                .with(mService.postSaveServiceDetail(data), mCallbackPostSaveServiceDetail).enqueue(mView.getContext());
    }
}
