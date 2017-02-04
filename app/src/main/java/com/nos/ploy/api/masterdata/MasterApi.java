package com.nos.ploy.api.masterdata;

import com.nos.ploy.api.account.model.TransportGson;
import com.nos.ploy.api.masterdata.model.AppLanguageGson;
import com.nos.ploy.api.masterdata.model.HtmlAppGson;
import com.nos.ploy.api.masterdata.model.LanguageGson;
import com.nos.ploy.api.ployee.model.PloyeeAvailiabilityGson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Saran on 25/12/2559.
 */

public interface MasterApi {

    @GET("/api/spoken")
    Call<LanguageGson> getSpokenLanguageList();

    //api/getHtmlApp?lgCode=en&dataId=5
    @GET("/api/getHtmlApp")
    Call<HtmlAppGson> getHtmlApp(@Query("lgCode") String languageCode, @Query("dataId") long dataId);

    ///api/transport
    @GET("/api/transport")
    Call<TransportGson> getTransportList();



    //{{endpoint_ploy}}/api/avai?userId=1
    @GET("api/avai")
    Call<PloyeeAvailiabilityGson> getAvailability(@Query("userId") long userId);

    ///api/appLanguage/getAppLanguageActiveList
    @GET("/api/appLanguage/getAppLanguageActiveList")
    Call<AppLanguageGson> getAppLanguageActiveList();

    //{{endpoint_ploy}}/api/appLabel/getByLgCode?lgCode=en
    @GET("api/appLabel/getByLgCode")
    Call<ResponseBody> getLanguageAppLabel(@Query("lgCode") String lgCode);
}
