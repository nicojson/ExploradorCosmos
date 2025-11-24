package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.JWSTResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JWSTApi {
    @GET("all/type/jpg")
    Call<JWSTResponse> getAllImages(@Query("page") int page, @Query("perPage") int perPage);
}
