package com.nos.ploy.api.ployer.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Saran on 3/1/2560.
 */

public class PloyerServicesGson extends BaseResponse<ArrayList<PloyerServicesGson.Data>> {
    public static class Data {
        /*
         "id": 1,
      "serviceName": "Children",
      "imgUrl": "http://localhost:8080/image/service_1481436000929.jpg",
      "ployeeCount": 5
         */
        @SerializedName("id")
        private long id;
        @SerializedName("serviceName")
        private String serviceName;
        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("ployeeCount")
        private long ployeeCount;

        public Data() {
        }

        public long getId() {
            return id;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public long getPloyeeCount() {
            return ployeeCount;
        }
    }
}
