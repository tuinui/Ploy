package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 29/11/2559.
 */

public class PostSignupGson {

    /*
    29/11/2016
    {
	"email":"nuitest1@gmail.com",
	"firstName":"test",
	"lastName":"test",
	"birthDay":"2016-02-02",
	"password":"passw0rd"
}
     */
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthDay")
    private String birthDay;
    @SerializedName("password")
    private String password;

    public PostSignupGson() {
    }

    public PostSignupGson(String birthDay, String email, String firstName, String lastName, String password) {
        this.birthDay = birthDay;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
