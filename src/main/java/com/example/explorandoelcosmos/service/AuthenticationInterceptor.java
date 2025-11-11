package com.example.explorandoelcosmos.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {

    private final String apiKey;

    public AuthenticationInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", "api-key " + apiKey);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
