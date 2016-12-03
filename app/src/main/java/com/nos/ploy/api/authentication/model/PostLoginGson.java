package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 29/11/2559.
 */

public class PostLoginGson {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public PostLoginGson(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
