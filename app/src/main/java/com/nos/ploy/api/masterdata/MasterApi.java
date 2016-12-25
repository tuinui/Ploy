package com.nos.ploy.api.masterdata;

import com.nos.ploy.api.account.model.ProfileGson;
import com.nos.ploy.api.masterdata.model.HtmlAppGson;
import com.nos.ploy.api.masterdata.model.LanguageGson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Saran on 25/12/2559.
 */

public interface MasterApi {

    @GET("/api/spoken")
    Call<LanguageGson> getLanguageList();

    //api/getHtmlApp?lgCode=en&dataId=5
    @GET("/api/getHtmlApp")
    Call<HtmlAppGson> getHtmlApp(@Query("lgCode") String languageCode,@Query("dataId") long dataId);
}
