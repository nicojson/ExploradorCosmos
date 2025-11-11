package com.example.explorandoelcosmos.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static HttpLoggingInterceptor createLogger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // Set the desired log level. Use Level.BODY to see everything.
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public static Retrofit getClient(String baseUrl) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(createLogger());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static Retrofit getClient(String baseUrl, String headerName, String headerValue) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Add the auth header first
        httpClient.addInterceptor(new HeaderInterceptor(headerName, headerValue));
        // Then add the logger to see the final request
        httpClient.addInterceptor(createLogger());

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}
