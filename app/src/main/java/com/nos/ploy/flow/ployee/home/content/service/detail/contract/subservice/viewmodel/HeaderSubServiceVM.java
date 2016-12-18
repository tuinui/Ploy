package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class HeaderSubServiceVM implements PloyeeServiceDetailSubServiceItemBaseViewModel {


    private String name;
    private PloyeeServiceDetailGson.Data.SubService.SubServiceLv1 data;

    public HeaderSubServiceVM(PloyeeServiceDetailGson.Data.SubService.SubServiceLv1 data) {
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

    public PloyeeServiceDetailGson.Data.SubService.SubServiceLv1 getData() {
        return data;
    }
}
