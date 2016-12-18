package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 19/12/2559.
 */

public class PostLoginFacebookGson {

    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("fbUserId")
    private String fbUserId;

    public PostLoginFacebookGson(String email, String firstName, String lastName, String fbUserId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fbUserId = fbUserId;
    }
}
