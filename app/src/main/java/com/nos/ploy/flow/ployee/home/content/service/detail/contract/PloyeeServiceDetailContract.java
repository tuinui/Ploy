package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostSavePloyerServiceDetailGson;
import com.nos.ploy.base.contract.BasePresenter;
import com.nos.ploy.base.contract.BaseView;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailContract {
    public interface View extends BaseView<Presenter> {

        void bindData(PloyerServiceDetailGson.Data data);


    }

    public interface Presenter extends BasePresenter {

        void requestSaveServiceDetail(PostSavePloyerServiceDetailGson postSavePloyeeServiceDetailGson);
    }

    public interface ViewModel{
        String getDescription();
        String getCertificate();
        String getEquipmentNeeded();
        long getPriceMin();
        long getPriceMax();
        PloyerServiceDetailGson.Data getData();
        long getServiceId();
        String getServiceOthersName();
    }
}
