package com.github.client.api;

import com.github.client.api.model.Repository;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sam on 2/18/17.
 */
public interface AccountService {

    @GET("/user/repos")
    Single<Response<List<Repository>>> getRepositories();

    @GET("/repos/{owner}/{name}")
    Single<Response<Repository>> getRepoDetails(@Path("owner") String owner, @Path("name") String name);
}
