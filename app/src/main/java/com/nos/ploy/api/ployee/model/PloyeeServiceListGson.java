package com.nos.ploy.api.ployee.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponseArray;

/**
 * Created by Saran on 30/11/2559.
 */

public class PloyeeServiceListGson extends BaseResponseArray<PloyeeServiceListGson.PloyeeServiceItemGson> {

    public PloyeeServiceListGson() {
    }

    public static class PloyeeServiceItemGson {
        @SerializedName("id")
        private long id;
        @SerializedName("serviceName")
        private String serviceName;
        @SerializedName("imgUrl")
        private String imageUrl;

        public PloyeeServiceItemGson() {
        }

        public long getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getServiceName() {
            return serviceName;
        }
    }


}
