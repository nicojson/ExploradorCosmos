package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.NasaApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApi {
    @GET("search")
    Call<NasaApiResponse> searchImages(@Query("q") String query, @Query("media_type") String mediaType);
}
