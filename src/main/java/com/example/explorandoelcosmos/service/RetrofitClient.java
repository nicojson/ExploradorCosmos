package com.example.explorandoelcosmos.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final long TIMEOUT_SECONDS = 15;

    private static HttpLoggingInterceptor createLogger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private static OkHttpClient.Builder createHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    public static Retrofit getClient(String baseUrl) {
        OkHttpClient.Builder httpClient = createHttpClientBuilder();
        httpClient.addInterceptor(createLogger());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static Retrofit getClient(String baseUrl, String headerName, String headerValue) {
        OkHttpClient.Builder httpClient = createHttpClientBuilder();
        httpClient.addInterceptor(new HeaderInterceptor(headerName, headerValue));
        httpClient.addInterceptor(createLogger());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
