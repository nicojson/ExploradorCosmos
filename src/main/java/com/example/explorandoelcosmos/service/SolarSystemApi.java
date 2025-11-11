package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.SolarSystemApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SolarSystemApi {
    /**
     * Fetches all bodies from the Solar System API.
     */
    @GET("bodies")
    Call<SolarSystemApiResponse> getBodies();
}
