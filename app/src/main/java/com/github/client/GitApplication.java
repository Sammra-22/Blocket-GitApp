package com.github.client;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.github.client.storage.LocalCache;
import com.github.client.utils.Global;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.client.utils.Global.ACCOUNT_TOKEN;

/**
 * Created by Sam on 2/18/17.
 * Application class for global settings
 */
public class GitApplication extends Application {

    static GitApplication mInstance;
    Retrofit retrofit;
    Map<String, String> headers = new HashMap<>();
    Context context;

    public GitApplication() {
        super();
        mInstance = this;
    }

    public GitApplication(Context context) {
        mInstance = this;
        mInstance.context = context;
    }

    public static GitApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        // Setup a Retrofit if a token is available
        String token = getToken();
        if (!TextUtils.isEmpty(token))
            setupRetrofit("token " + token);
    }

    public String getToken() {
        return LocalCache.getInstance().get(context, ACCOUNT_TOKEN);
    }

    public void setToken(String token) {
        LocalCache.getInstance().set(context, ACCOUNT_TOKEN, token);
    }

    public void logout() {
        LocalCache.getInstance().clearAccount(context);
    }

    public Retrofit setupRetrofit(final String authentication) {
        setHeaders(authentication);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder reqBuilder = chain.request().newBuilder();
                for (String key : headers.keySet())
                    reqBuilder.addHeader(key, headers.get(key));
                return chain.proceed(reqBuilder.build());
            }
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addNetworkInterceptor(loggingInterceptor);
        clientBuilder.followRedirects(true);
        retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(Global.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    void setHeaders(String authentication) {
        headers.put(Global.HEADER_AUTH, authentication);
        headers.put(Global.HEADER_USER_AGENT, "BlocketGitApp");
    }
}
