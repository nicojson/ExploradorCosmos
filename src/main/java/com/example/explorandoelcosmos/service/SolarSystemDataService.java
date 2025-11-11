package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.CelestialBody;
import com.example.explorandoelcosmos.model.SolarSystemApiResponse;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SolarSystemDataService {

    private static final String API_BASE_URL = "https://api.le-systeme-solaire.net/rest/";
    // TODO: Replace with your actual Solar System API key.
    private static final String API_KEY = "c3556964-7d6f-4e43-81ab-125cf8edc6b5";

    private final SolarSystemApi solarSystemApi;
    private final Random random = new Random();

    public SolarSystemDataService() {
        String authHeaderValue = "Bearer " + API_KEY;
        this.solarSystemApi = RetrofitClient.getClient(API_BASE_URL, "Authorization", authHeaderValue).create(SolarSystemApi.class);
    }

    public String getRandomBodyName() throws IOException {
        Call<SolarSystemApiResponse> call = solarSystemApi.getBodies();
        Response<SolarSystemApiResponse> response = call.execute();

        if (!response.isSuccessful()) {
            if (response.code() == 401) {
                throw new IOException("API Key inválida o no proporcionada para The Solar System. Verifique su clave y el formato del header.");
            }
            throw new IOException("Error en la API de The Solar System. Código: " + response.code());
        }

        SolarSystemApiResponse apiResponse = response.body();
        if (apiResponse == null || apiResponse.getBodies() == null || apiResponse.getBodies().isEmpty()) {
            throw new IOException("No se encontraron cuerpos celestes.");
        }

        // CORRECTED: Filter for major bodies that are more likely to have images.
        List<CelestialBody> majorBodies = apiResponse.getBodies().stream()
                .filter(body -> body.isPlanet() || "Moon".equals(body.getEnglishName()) || "Titan".equals(body.getEnglishName()) || "Europa".equals(body.getEnglishName()) || "Io".equals(body.getEnglishName()))
                .collect(Collectors.toList());

        if (majorBodies.isEmpty()) {
            throw new IOException("No se encontraron cuerpos celestes con nombre en inglés.");
        }

        CelestialBody randomBody = majorBodies.get(random.nextInt(majorBodies.size()));
        return randomBody.getEnglishName();
    }
}
