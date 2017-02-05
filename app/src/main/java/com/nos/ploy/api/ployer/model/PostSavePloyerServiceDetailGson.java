package com.nos.ploy.api.ployer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 23/12/2559.
 */

public class PostSavePloyerServiceDetailGson {
    //    "serviceId": 1,
//            "userId": 1,
//            "serviceNameOthers": "xx",
//            "description": "Service Description",
//            "priceMin": 50,
//            "priceMax": 500,
//            "certificate": "test en",
//            "equipment": "test en",
//            "subServiceLV2IdList": [1,3]
    @SerializedName("serviceId")
    private long serviceId;
    @SerializedName("userId")
    private long userId;
    @SerializedName("serviceNameOthers")
    private String serviceNameOthers;
    @SerializedName("description")
    private String description;
    @SerializedName("priceMin")
    private long priceMin;
    @SerializedName("priceMax")
    private long priceMax;
    @SerializedName("certificate")
    private String certificate;
    @SerializedName("equipment")
    private String equipment;


    @SerializedName("subServiceLV2IdList")
    private List<Long> subServiceLv2IdList = new ArrayList<>();

    public PostSavePloyerServiceDetailGson() {

    }

    public PostSavePloyerServiceDetailGson(PloyerServiceDetailGson.Data data) {
        serviceId = data.getServiceId();
        userId = data.getUserId();
        serviceNameOthers = data.getServiceNameOthers();
        description = data.getDescription();
        priceMin = data.getPriceMin();
        priceMin = data.getPriceMax();
        certificate = data.getCertificate();
        equipment = data.getEquipment();

    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setServiceNameOthers(String serviceNameOthers) {
        this.serviceNameOthers = serviceNameOthers;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriceMin(long priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(long priceMax) {
        this.priceMax = priceMax;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setSubServiceLv2IdList(List<Long> ids) {
        this.subServiceLv2IdList.clear();
        this.subServiceLv2IdList.addAll(ids);
    }

    public List<Long> getSubServiceLv2IdList(){
        return subServiceLv2IdList;
    }
}
