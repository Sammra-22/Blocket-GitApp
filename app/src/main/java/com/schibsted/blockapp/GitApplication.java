package com.schibsted.blockapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.schibsted.blockapp.activity.AuthActivity;
import com.schibsted.blockapp.storage.LocalCache;
import com.schibsted.blockapp.utils.Global;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sam on 2/18/17.
 * Application class for global settings
 */
public class GitApplication extends Application {

    static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        // Setup a Retrofit if a token is available
        String token = LocalCache.getInstance().getToken(this);
        if(!TextUtils.isEmpty(token))
            setupRetrofit("token "+token);
    }

    public static void logout(Context context){
        LocalCache.getInstance().clearAccount(context);
    }

    public static Retrofit setupRetrofit(final String authentication){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader(Global.HEADER_AUTH, authentication)
                        .addHeader(Global.HEADER_USER_AGENT, "BlocketGitApp")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        clientBuilder.followRedirects(true);
        retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(Global.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
