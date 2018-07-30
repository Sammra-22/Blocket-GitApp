package com.github.client.api.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.github.client.storage.Storage;
import com.github.client.utils.Global;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeadersInterceptor implements Interceptor {

    private static final String HEADER_USER_AGENT_VALUE = "GitApp";
    private Storage storage;

    public HttpHeadersInterceptor(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder reqBuilder = chain.request().newBuilder();
        reqBuilder.addHeader(Global.HEADER_USER_AGENT, HEADER_USER_AGENT_VALUE);
        if (!TextUtils.isEmpty(storage.fetchToken())) {
            reqBuilder.addHeader(Global.HEADER_AUTH, String.format("token %s", storage.fetchToken()));
        } else if (!TextUtils.isEmpty(storage.fetchBasicCredentials())) {
            reqBuilder.addHeader(Global.HEADER_AUTH, storage.fetchBasicCredentials());
        }
        return chain.proceed(reqBuilder.build());
    }
}
