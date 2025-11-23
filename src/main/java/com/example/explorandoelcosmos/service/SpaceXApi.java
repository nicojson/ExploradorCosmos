package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.Launch;
import com.example.explorandoelcosmos.model.Rocket;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface SpaceXApi {
    @GET("rockets")
    Call<List<Rocket>> getRockets();

    @GET("launches/latest")
    Call<Launch> getLatestLaunch();

    @GET("launches")
    Call<List<Launch>> getAllLaunches();
}
