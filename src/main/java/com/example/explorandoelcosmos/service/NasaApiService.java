package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.MoonPhaseRequest;
import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaAsset;
import com.example.explorandoelcosmos.model.NasaLibraryResponse;
import com.example.explorandoelcosmos.model.NasaMetadata;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NasaApiService {

    // Mars Rover Photos - ELIMINADO

    // NASA Image and Video Library (Public API - No API key required)
    @GET("search")
    Call<NasaLibraryResponse> searchNasaLibrary(
            @Query("q") String query,
            @Query("media_type") String mediaType,
            @Query("year_start") Integer yearStart,
            @Query("year_end") Integer yearEnd);

    // Astronomy API - Moon Phase
    @POST("api/v2/studio/moon-phase")
    Call<MoonPhaseResponse> getMoonPhase(@Body MoonPhaseRequest request);

    @GET("asset/{nasa_id}")
    Call<NasaAsset> getAsset(@retrofit2.http.Path("nasa_id") String nasaId);

    @GET("metadata/{nasa_id}")
    Call<NasaMetadata> getMetadata(@retrofit2.http.Path("nasa_id") String nasaId);

    @GET("captions/{nasa_id}")
    Call<String> getCaptions(@retrofit2.http.Path("nasa_id") String nasaId);
}
