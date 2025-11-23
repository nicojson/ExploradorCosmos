package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.MoonPhaseRequest;
import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AstronomyApiService {

    private final NasaApiService nasaApiService;

    public AstronomyApiService() {
        this.nasaApiService = NasaApiClient.getAstronomyApiService();
    }

    public MoonPhaseResponse getMoonPhase(LocalDate date) throws IOException {
        String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        MoonPhaseRequest request = new MoonPhaseRequest(formattedDate);
        
        Response<MoonPhaseResponse> response = nasaApiService.getMoonPhase(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
            throw new IOException("Error al obtener la fase lunar: " + response.code() + " - " + errorBody);
        }
    }
}
