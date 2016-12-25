package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 25/12/2559.
 */

public class PostForgotPasswordGson {

    @SerializedName("email")
    private String email;
    @SerializedName("newPassword")
    private String newPassword;

    public PostForgotPasswordGson(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
