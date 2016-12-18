package com.nos.ploy.api.account;

import com.nos.ploy.api.account.model.PostUploadProfileImageGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.model.AccountGson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Saran on 8/12/2559.
 */

public interface AccountApi {
    @GET("/api/user/account")
    Call<AccountGson> getAccountGson(@Query("userId") String userId);

    @POST("/api/user/profileImg/upload")
    Call<ProfileImageGson> postUploadProfileImage(@Body PostUploadProfileImageGson data);
}
