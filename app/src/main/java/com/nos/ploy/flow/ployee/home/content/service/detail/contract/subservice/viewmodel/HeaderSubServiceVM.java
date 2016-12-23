package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class HeaderSubServiceVM implements PloyeeServiceDetailSubServiceItemBaseViewModel {


    private String name;
    private PloyerServiceDetailGson.Data.SubService.SubServiceLv1 data;

    public HeaderSubServiceVM(PloyerServiceDetailGson.Data.SubService.SubServiceLv1 data) {
        this.data = data;
        this.name = data.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public int getViewType() {
        return HEADER;
    }

    public PloyerServiceDetailGson.Data.SubService.SubServiceLv1 getData() {
        return data;
    }
}
