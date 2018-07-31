package com.github.client.api;

import com.github.client.api.model.AuthQuery;
import com.github.client.api.model.AuthToken;
import com.github.client.api.model.User;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;



/**
 * Created by Sam on 2/18/17.
 */
public interface AuthService {

    @POST("/authorizations")
    Single<Response<AuthToken>> authenticate(@Body AuthQuery authQuery);

    @GET("/user")
    Single<Response<User>> getUser();
}
