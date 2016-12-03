package com.nos.ploy.api.ployee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 1/12/2559.
 */

public class PostPloyeeServiceDetailGson {
    /*
    {
	"serviceId":1,
	"userId":1,
	"lgCode":"en"
}
     */
    @SerializedName("serviceId")
    private Long serviceId;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("lgCode")
    private String lgCode;

    public PostPloyeeServiceDetailGson(Long serviceId, Long userId, String lgCode) {
        this.serviceId = serviceId;
        this.userId = userId;
        this.lgCode = lgCode;
    }

}
