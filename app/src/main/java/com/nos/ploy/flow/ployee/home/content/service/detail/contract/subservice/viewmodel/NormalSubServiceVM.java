package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class NormalSubServiceVM implements PloyeeServiceDetailSubServiceItemBaseViewModel {
    private PloyeeServiceDetailGson.SubService.SubServiceLv2 data;
    private String name;
    private Boolean checked;

    public NormalSubServiceVM(PloyeeServiceDetailGson.SubService.SubServiceLv2 data) {
        this.data = data;
        name = data.getName();
        checked = data.getChecked();
    }

    public PloyeeServiceDetailGson.SubService.SubServiceLv2 getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Boolean getChecked() {
        return checked;
    }

    @Override
    public int getViewType() {
        return ITEM;
    }
}
