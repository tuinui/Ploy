package com.nos.ploy.api.ployer;

import com.nos.ploy.api.account.model.MemberProfileGson;
import com.nos.ploy.api.base.response.BaseResponse;
import com.nos.ploy.api.ployer.model.PloyerServiceDetailGson;
import com.nos.ploy.api.ployer.model.PloyerServicesGson;
import com.nos.ploy.api.ployer.model.PloyerUserListGson;
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

    //{{endpoint_ploy}}/api/ployer/list?serviceId=1
    @GET("/api/ployer/list?pageNo=1&pageSize=9999")
    Call<PloyerUserListGson> getPloyerList(@Query("serviceId") long serviceId);

    ///api/ployer/profile?userId=1&serviceId=1&lgCode=en
    @GET("/api/ployer/profile")
    Call<MemberProfileGson> getMemberProfileGson(@Query("userId") long userId,@Query("serviceId") long serviceId,@Query("lgCode") String languageCode);
}
