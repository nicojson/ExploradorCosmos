package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.PlanetItemSearchResponse;
import com.example.explorandoelcosmos.model.PlanetSearchRequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlanetApi {
    /**
     * Searches for the latest satellite imagery items using a POST request.
     */
    @POST("quick-search")
    Call<PlanetItemSearchResponse> searchItems(@Body PlanetSearchRequestBody body);
}
