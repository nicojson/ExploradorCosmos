package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.ApodItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public interface NasaPlanetaryApi {
    @GET("planetary/apod")
    Call<List<ApodItem>> getApodList(@Query("api_key") String apiKey, @Query("count") int count);

    @GET("planetary/apod")
    Call<ApodItem> getTodaysApod(@Query("api_key") String apiKey);
}