package com.github.client;

import com.github.client.utils.Global;

import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class TestUtils {

    public static <T> retrofit2.Response<T> mockServerError() {
        return mockError(500, new Headers.Builder().build());
    }

    public static <T> retrofit2.Response<T> mockUnauthorizedError() {
        return mockError(401, new Headers.Builder().build());
    }

    public static <T> retrofit2.Response<T> mockUnauthorizedError2fa() {
        return mockError(401, new Headers.Builder().add(Global.HEADER_TWO_FACTOR_AUTH, "required; sms").build());
    }

    private static <T> retrofit2.Response<T> mockError(int errorCode, Headers headers) {
        Request request = new Request.Builder()
                .url("http://localhost")
                .build();
        return retrofit2.Response.error(ResponseBody.create(null, ""),
                new okhttp3.Response.Builder()
                        .protocol(Protocol.HTTP_1_1)
                        .code(errorCode)
                        .message("Server error")
                        .request(request)
                        .headers(headers)
                        .build());
    }
}
