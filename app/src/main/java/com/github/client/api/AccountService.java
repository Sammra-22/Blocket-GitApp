package com.github.client.api;

import com.github.client.api.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

/**
 * Created by Sam on 2/18/17.
 */
public interface AccountService {

    @GET("/user/repos")
    Single<Response<List<Repository>>> getRepositories();

    @GET("/repos/{owner}/{name}")
    Single<Response<Repository>> getRepoDetails(@Path("owner") String owner, @Path("name") String name);
}
