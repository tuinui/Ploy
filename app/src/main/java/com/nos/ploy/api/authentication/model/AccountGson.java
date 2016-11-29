package com.nos.ploy.api.authentication.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

/**
 * Created by Saran on 29/11/2559.
 */

public class AccountGson extends BaseResponse<AccountGson>{

    /* 29/11/229
      "data": {
    "userId": 10,
    "firstName": "test",
    "lastName": "test",
    "birthDay": 1454346000000,
    "email": "test4@gmail.com",
    "password": "2dCpxnfLev8fsQSrT6afPw==",
    "userType": "ROLE_USER",
    "phone": null
  }
     */
    @SerializedName("userId")
    private Long userId;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthDay")
    private String birthDay;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("userType")
    private String userType;
    @SerializedName("phone")
    private String phone;

    public AccountGson() {
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

    public String getPhone() {
        return phone;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }
}
