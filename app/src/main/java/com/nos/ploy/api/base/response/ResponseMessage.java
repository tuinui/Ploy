package com.nos.ploy.api.base.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 30/11/2559.
 */

public class ResponseMessage{
    public static final String CODE_SUCCESS = "000";
    public static final String CODE_INTERNAL_ERROR = "001";
    public static final String CODE_DATA_NOT_FOUND = "002";
    public static final String CODE_EXISTING_EMAIL = "101";
    public static final String CODE_EXISTING_USER_ID= "102";
    public static final String CODE_EXISTING_SERVICE_ID = "103";
    public static final String CODE_EXISTING_FACEBOOK_USERID = "104";
    public static final String CODE_EXISTING_INVALID_EMAIL_OR_PASSWORD = "201";
    public static final String CODE_EXISTING_INVALID_ACCOUNT_ROLE_USER = "202";

    @SerializedName("msgCode")
    private String messageCode;
    @SerializedName("msgDesc")
    private String messageDescription;

    public ResponseMessage() {
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public boolean isSuccess(){
        return TextUtils.equals("000",messageCode);
    }
}
