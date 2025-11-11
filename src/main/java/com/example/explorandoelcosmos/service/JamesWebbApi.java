package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.JamesWebbApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface JamesWebbApi {
    /**
     * Fetches all images from the James Webb API.
     */
    @GET("api/v1/all/images")
    Call<JamesWebbApiResponse> getAllImages();
}
