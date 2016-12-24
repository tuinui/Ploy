package com.nos.ploy.api.account.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 24/12/2559.
 */

public class PostDeleteProfileImageGson {

    @SerializedName("imgIdList")
    private List<Long> imgIdList = new ArrayList<>();

    public PostDeleteProfileImageGson(List<Long> imgIdList) {
        this.imgIdList = imgIdList;
    }

    public PostDeleteProfileImageGson(long imgId) {
        imgIdList.clear();
        imgIdList.add(imgId);
    }
}
