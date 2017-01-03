package com.nos.ploy.api.ployer;

import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PostGetPloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PostSavePloyerServiceDetailGson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Saran on 23/12/2559.
 */

public interface PloyerApi {


    //{{endpoint_ploy}}/api/ployee/service/mapping
    @POST("/api/ployee/service/mapping")
    Call<PloyerServiceDetailGson> getServiceDetail(@Body PostGetPloyerServiceDetailGson data);

    //{{endpoint_ploy}}/api/ployee/service/mapping/save
    @POST("/api/ployee/service/mapping/save")
    Call<BaseResponse<Object>> postSaveServiceDetail(@Body PostSavePloyerServiceDetailGson data);

    //{{endpoint_ploy}}/api/ployer/home?lgCode=en
    @GET("/api/ployer/home")
    Call<PloyerServicesGson> getServiceList(@Query("lgCode") String languageCode);
}
