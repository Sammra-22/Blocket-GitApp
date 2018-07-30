package com;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class TestUtils {

    public static <T> retrofit2.Response<T> mockServerError() {
        return mockError(500);
    }

    public static <T> retrofit2.Response<T> mockUnauthorizedError() {
        return mockError(401);
    }

    private static <T> retrofit2.Response<T> mockError(int errorCode) {
        Request request = new Request.Builder()
                .url("http://localhost")
                .build();
        return retrofit2.Response.error(ResponseBody.create(null, ""),
                new okhttp3.Response.Builder()
                        .protocol(Protocol.HTTP_1_1)
                        .code(errorCode)
                        .message("Server error")
                        .request(request)
                        .build());
    }
}
