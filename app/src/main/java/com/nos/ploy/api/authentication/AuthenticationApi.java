package com.nos.ploy.api.authentication;

import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.PostLoginFacebookGson;
import com.nos.ploy.api.authentication.model.PostLoginGson;
import com.nos.ploy.api.authentication.model.PostSignupGson;
import com.nos.ploy.api.authentication.model.UserTokenGson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Saran on 29/11/2559.
 */

public interface AuthenticationApi {
    @POST("/api/auth/signup")
    Call<AccountGson> postSignup(@Body PostSignupGson data);

    @POST("/api/auth/login")
    Call<UserTokenGson> postLogin(@Body PostLoginGson data);

    //    /api/auth/facebook
    @POST("/api/auth/facebook")
    Call<UserTokenGson> postLoginFacebook(@Body PostLoginFacebookGson data);
}
