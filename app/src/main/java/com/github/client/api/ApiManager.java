package com.github.client.api;

import com.github.client.api.network.HttpHeadersInterceptor;
import com.github.client.storage.Storage;
import com.github.client.utils.Global;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiManager instance;
    private Retrofit retrofit;
    private Storage storage;

    private ApiManager(Storage storage) {
        this.storage = storage;
        retrofit = setupRetrofit();
    }

    public static ApiManager getInstance(Storage storage) {
        if (instance == null) {
            instance = new ApiManager(storage);
        }
        return instance;
    }

    public AuthService getAccountService() {
        return retrofit.create(AuthService.class);
    }

    public AccountService getRepositoryService() {
        return retrofit.create(AccountService.class);
    }

    private Retrofit setupRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new HttpHeadersInterceptor(storage))
                .addNetworkInterceptor(loggingInterceptor)
                .connectTimeout(3, TimeUnit.SECONDS)
                .followRedirects(true);
        retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(Global.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }


}
