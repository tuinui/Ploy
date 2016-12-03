package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;
import com.nos.ploy.base.contract.BasePresenter;
import com.nos.ploy.base.contract.BaseView;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailContract {
    public interface View extends BaseView<Presenter> {

        void bindData(PloyeeServiceDetailGson data);
    }

    public interface Presenter extends BasePresenter {

    }

    public interface ViewModel{
        String getDescription();
        String getCertificate();
        String getEquipmentNeeded();
        Long getPriceMin();
        Long getPriceMax();
        PloyeeServiceDetailGson getData();
    }
}
