package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

import okhttp3.Response;

/**
 * Created by Saran on 29/11/2559.
 */

public class UserTokenGson extends BaseResponse<UserTokenGson.Data> {


    public static class Data {
        @SerializedName("userId")
        private Long userId;
        @SerializedName("token")
        private String token;

        public Data() {
        }

        public String getToken() {
            return token;
        }

        public Long getUserId() {
            return userId;
        }
    }
}
