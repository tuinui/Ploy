package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

/**
 * Created by Saran on 25/12/2559.
 */

public class PostFacebookMapUser {
    @SerializedName("fbUserId")
    private String fbUserId;
    @SerializedName("userId")
    private long userId;

    public PostFacebookMapUser() {
    }

    public PostFacebookMapUser(String fbUserId, long userId) {
        this.fbUserId = fbUserId;
        this.userId = userId;
    }

}
