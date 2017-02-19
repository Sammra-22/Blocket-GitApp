package com.schibsted.blockapp.service;

import com.schibsted.blockapp.model.AuthQuery;
import com.schibsted.blockapp.model.AuthToken;
import com.schibsted.blockapp.model.Repository;
import com.schibsted.blockapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Sam on 2/18/17.
 * API interface
 */
public interface GitApiService {

    @POST("/authorizations")
    Call<AuthToken> authenticate(@Body AuthQuery authQuery);

    @GET("/user")
    Call<User> getUser();

    @GET("/user/repos")
    Call<List<Repository>> getRepositories();

    @GET("/repos/{owner}/{name}")
    Call<Repository> getRepoDetails(@Path("owner") String owner, @Path("name") String name);

}
