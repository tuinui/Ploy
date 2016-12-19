package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import android.content.Context;
import android.text.TextUtils;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailVM implements PloyeeServiceDetailContract.ViewModel {
    private PloyeeServiceDetailGson.Data data;
    private String description;
    private String certificate;
    private String equipmentNeeded;
    private long priceMin;
    private long priceMax = 2;


    public PloyeeServiceDetailVM(PloyeeServiceDetailGson.Data data) {
        this.data = data;
        if (null != data) {
            description = data.getDescription();
            certificate = data.getCertificate();
            equipmentNeeded = data.getEquipment();
            priceMin = data.getPriceMin();
            if (data.getPriceMax() > 2) {
                priceMax = data.getPriceMax();
            }

        }

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
    public long getPriceMin() {
        return priceMin;
    }

    @Override
    public long getPriceMax() {
        return priceMax;
    }

    @Override
    public PloyeeServiceDetailGson.Data getData() {
        return data;
    }
}
