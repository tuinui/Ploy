package com.nos.ploy.flow.ployee.home.content.service.detail.contract.subservice.viewmodel;

import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class NormalSubServiceVM implements PloyeeServiceDetailSubServiceItemBaseViewModel {
    private PloyerServiceDetailGson.Data.SubService.SubServiceLv2 data;
    private String name;
    private Boolean checked;

    public NormalSubServiceVM(PloyerServiceDetailGson.Data.SubService.SubServiceLv2 data) {
        this.data = data;
        name = data.getName();
        checked = data.isChecked();
    }

    public PloyerServiceDetailGson.Data.SubService.SubServiceLv2 getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Boolean isChecked() {
        return null != checked &&checked;
    }


    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public int getViewType() {
        return ITEM;
    }
}
