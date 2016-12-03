package com.nos.ploy.api.ployee;

import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;
import com.nos.ploy.api.ployee.model.PostPloyeeServiceDetailGson;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Saran on 30/11/2559.
 */

public interface PloyeeApi {


    //api/user/account?userId=1
    @GET("/api/ployee/service")
    Call<PloyeeServiceListGson> getPloyeeServiceList(@Query("lgCode") String language);

    //{{endpoint_ploy}}/api/ployee/service/mapping
    @POST("/api/ployee/service/mapping")
    Call<PloyeeServiceDetailGson> getPloyeeServiceDetail(@Body PostPloyeeServiceDetailGson data);
}
