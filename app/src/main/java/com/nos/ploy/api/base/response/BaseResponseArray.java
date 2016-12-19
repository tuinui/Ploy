package com.nos.ploy.api.base.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saran on 30/11/2559.
 */

public class BaseResponseArray<E> {
    @SerializedName("responseMsg")
    private ResponseMessage responseMessage;

    @SerializedName("data")
    private List<E> data;



    public BaseResponseArray() {
    }




    public boolean isSuccess(){
        return null != responseMessage && responseMessage.isSuccess();
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public List<E> getData() {
        return data;
    }
}
