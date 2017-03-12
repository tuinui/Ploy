package com.nos.ploy.api.ployee;

import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.ployee.model.DeleteServiceGson;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;
import com.nos.ploy.api.ployee.model.PloyeeServiceListGson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Saran on 30/11/2559.
 */

public interface PloyeeApi {


    //api/user/account?userId=1
    @GET("/api/ployee/service")
    Call<PloyeeServiceListGson> getServiceList(@Query("lgCode") String language);



    @POST("api/avai/save")
    Call<PloyeeAvailiabilityGson> postSaveAvailability(@Body PloyeeAvailiabilityGson.Data data);

    //{{endpoint_ploy}}/api/service/mapping/delete
//    @DELETE("api/service/mapping/delete")
//    Call<BaseResponse> deleteServiceMapping(@Body DeleteServiceGson data);

    @HTTP(method = "DELETE", path = "api/service/mapping/delete", hasBody = true)
    Call<BaseResponse> deleteServiceMapping(@Body DeleteServiceGson data);
}
