package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import android.content.Context;
import android.text.TextUtils;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailVM implements PloyeeServiceDetailContract.ViewModel {
    private PloyeeServiceDetailGson data;
    private String description;
    private String certificate;
    private String equipmentNeeded;
    private Long priceMin;
    private Long priceMax;


    public PloyeeServiceDetailVM(PloyeeServiceDetailGson data) {
        this.data = data;
        description = data.getDescription();
        certificate = data.getCertificate();
        equipmentNeeded = data.getEquipment();
        priceMin = data.getPriceMin();
        priceMax = data.getPriceMax();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCertificate() {
        return certificate;
    }

    @Override
    public String getEquipmentNeeded() {
        return equipmentNeeded;
    }

    @Override
    public Long getPriceMin() {
        return priceMin;
    }

    @Override
    public Long getPriceMax() {
        return priceMax;
    }

    @Override
    public PloyeeServiceDetailGson getData() {
        return data;
    }
}
