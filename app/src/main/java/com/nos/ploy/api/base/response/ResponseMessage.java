package com.nos.ploy.api.base.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 30/11/2559.
 */

public class ResponseMessage{
    @SerializedName("msgCode")
    private String messageCode;//"error","success"
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
        return TextUtils.equals("success",messageCode);
    }
}
