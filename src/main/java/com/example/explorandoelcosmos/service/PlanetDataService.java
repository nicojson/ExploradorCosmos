package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.PlanetFeature;
import com.example.explorandoelcosmos.model.PlanetItemSearchResponse;
import com.example.explorandoelcosmos.model.PlanetSearchRequestBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PlanetDataService {

    private static final String API_BASE_URL = "https://api.planet.com/data/v1/";
    // TODO: Replace with your actual Planet API key.
    private static final String API_KEY = "PLAKc630a6ed4fdf4a13826ae566f39a0566";

    private final PlanetApi planetApi;
    private final Random random = new Random();

    public PlanetDataService() {
        // Note the "api-key " prefix is part of the value now
        String authHeaderValue = "api-key " + API_KEY;
        this.planetApi = RetrofitClient.getClient(API_BASE_URL, "Authorization", authHeaderValue).create(PlanetApi.class);
    }

    public String getRandomItemThumbnail() throws IOException {
        PlanetSearchRequestBody requestBody = new PlanetSearchRequestBody();
        Call<PlanetItemSearchResponse> call = planetApi.searchItems(requestBody);
        Response<PlanetItemSearchResponse> response = call.execute();

        if (!response.isSuccessful()) {
            if (response.code() == 401) {
                throw new IOException("API Key inválida o no proporcionada para Planet. Verifique su clave.");
            }
            throw new IOException("Error en la API de Planet. Código: " + response.code() + " - " + response.message());
        }

        PlanetItemSearchResponse searchResponse = response.body();
        if (searchResponse == null || searchResponse.getFeatures() == null || searchResponse.getFeatures().isEmpty()) {
            throw new IOException("No se encontraron imágenes de satélite (features).");
        }

        List<PlanetFeature> features = searchResponse.getFeatures();
        PlanetFeature randomFeature = features.get(random.nextInt(features.size()));

        if (randomFeature.getLinks() == null || randomFeature.getLinks().getThumbnail() == null) {
            throw new IOException("La imagen de satélite seleccionada no tiene una miniatura.");
        }

        return randomFeature.getLinks().getThumbnail();
    }
}
