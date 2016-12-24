package com.nos.ploy.api.account;

import com.nos.ploy.api.account.model.PostDeleteProfileImageGson;
import com.nos.ploy.api.account.model.PostUploadProfileImageGson;
import com.nos.ploy.api.account.model.ProfileGson;
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
    Call<AccountGson> getAccountGson(@Query("userId") long userId);

    @POST("/api/user/profileImg/upload")
    Call<ProfileImageGson> postUploadProfileImage(@Body PostUploadProfileImageGson data);

    @GET("/api/user/profileImg")
    Call<ProfileImageGson> getProfileImage(@Query("userId") long userId);

    @GET("/api/user/profile")
    Call<ProfileGson> getProfileGson(@Query("userId") long userId);

    @POST("/api/user/profile/save")
    Call<ProfileGson> postSaveProfileGson(@Body ProfileGson.Data data);

    @POST("/api/user/profile/update")
    Call<ProfileGson> postUpdateProfileGson(@Body ProfileGson.Data data);

    @POST("/api/user/profileImg/delete")
    Call<Object> postDeleteProfileImage(@Body PostDeleteProfileImageGson data);
}
