package com.nos.ploy.api.base.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saran on 30/11/2559.
 */

public class BaseResponseArray<E> {
    //    @SerializedName("responseMsg")
//    private ResponseMessage responseMessage;
    @SerializedName("isSucesss")
    private Boolean isSuccess;

    @SerializedName("userMessage")
    private String userMessage;

    @SerializedName("data")
    private List<E> data;


    public BaseResponseArray() {
    }



    public List<E> getData() {
        return data;
    }

    public boolean isSuccess() {
        return null != isSuccess && isSuccess;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
