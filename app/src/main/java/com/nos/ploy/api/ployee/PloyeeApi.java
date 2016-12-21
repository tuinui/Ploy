package com.nos.ploy.api.ployee;

import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployee.model.PloyeeServiceDetailGson;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;
import com.nos.ploy.api.ployee.model.PostPloyeeServiceDetailGson;

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
    Call<PloyeeServiceListGson> getServiceList(@Query("lgCode") String language);

    //{{endpoint_ploy}}/api/ployee/service/mapping
    @POST("/api/ployee/service/mapping")
    Call<PloyeeServiceDetailGson> getServiceDetail(@Body PostPloyeeServiceDetailGson data);

    //{{endpoint_ploy}}/api/avai?userId=1
    @GET("api/avai")
    Call<PloyeeAvailiabilityGson> getAvailability(@Query("userId") long userId);


}
