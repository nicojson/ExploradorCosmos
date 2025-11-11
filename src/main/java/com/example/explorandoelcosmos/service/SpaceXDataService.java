package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.Rocket;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceXDataService {

    private static final String API_BASE_URL = "https://api.spacexdata.com/v4/";
    private final SpaceXApi spaceXApi;
    private final Random random = new Random();

    public SpaceXDataService() {
        this.spaceXApi = RetrofitClient.getClient(API_BASE_URL).create(SpaceXApi.class);
    }

    public String getRandomRocketImage() throws IOException {
        Call<List<Rocket>> call = spaceXApi.getRockets();
        Response<List<Rocket>> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error en la API. Código de estado: " + response.code());
        }

        List<Rocket> rockets = response.body();

        if (rockets == null || rockets.isEmpty()) {
            throw new IOException("No se encontraron cohetes.");
        }

        List<String> allImages = new ArrayList<>();
        for (Rocket rocket : rockets) {
            if (rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty()) {
                allImages.addAll(rocket.getFlickrImages());
            }
        }

        if (allImages.isEmpty()) {
            throw new IOException("No se encontraron imágenes en la galería de cohetes.");
        }

        return allImages.get(random.nextInt(allImages.size()));
    }
}
