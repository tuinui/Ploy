package com.nos.ploy.api.authentication;

import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.PostSignupGson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Saran on 29/11/2559.
 */

public interface AuthenticationService {
    @POST("/api/auth/signup")
    Call<AccountGson> getAccountData(@Body PostSignupGson data);


}
