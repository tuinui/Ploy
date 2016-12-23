package com.nos.ploy.flow.ployee.home.content.service.detail.contract;

import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;

/**
 * Created by Saran on 1/12/2559.
 */

public class PloyeeServiceDetailVM implements PloyeeServiceDetailContract.ViewModel {
    private String name;
    private PloyerServiceDetailGson.Data data;
    private String description;
    private String certificate;
    private String equipmentNeeded;
    private long priceMin = 0;
    private long priceMax = 2;
    private long serviceId =-1;


    public PloyeeServiceDetailVM(PloyerServiceDetailGson.Data data) {
        this.data = data;
        if (null != data) {
            description = data.getDescription();
            certificate = data.getCertificate();
            equipmentNeeded = data.getEquipment();
            if(data.getPriceMin() > 2){
                priceMin = data.getPriceMin();
            }

            if (data.getPriceMax() > 2) {
                priceMax = data.getPriceMax();
            }
            serviceId = data.getServiceId();
            name = data.getServiceNameOthers();
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
    public PloyerServiceDetailGson.Data getData() {
        return data;
    }

    @Override
    public long getServiceId() {
        return serviceId;
    }

    @Override
    public String getServiceOthersName() {
        return name;
    }
}
