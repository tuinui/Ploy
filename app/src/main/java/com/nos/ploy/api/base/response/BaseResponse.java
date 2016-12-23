package com.nos.ploy.api.base.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Saran on 29/11/2559.
 */

public class BaseResponse<T> {
    @SerializedName("responseMsg")
    private ResponseMessage responseMessage;

    @SerializedName("data")
    private T data;


    public BaseResponse() {
    }

    public boolean isSuccess() {
        return null != responseMessage && responseMessage.isSuccess();
    }

    public String getErrorMessage() {
        if (null != responseMessage && null != responseMessage.getMessageDescription()) {
            return responseMessage.getMessageDescription();
        } else {
            return "error";
        }
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public T getData() {
        return data;
    }

}
