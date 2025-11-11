package com.example.explorandoelcosmos.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

/**
 * A generic interceptor that adds a specified header to every request.
 */
public class HeaderInterceptor implements Interceptor {

    private final String headerName;
    private final String headerValue;

    public HeaderInterceptor(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header(headerName, headerValue);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
