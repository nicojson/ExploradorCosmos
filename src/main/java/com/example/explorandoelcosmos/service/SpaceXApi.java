package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.Rocket;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface SpaceXApi {
    @GET("rockets")
    Call<List<Rocket>> getRockets();
}
