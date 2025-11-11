package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.JamesWebbApiResponse;
import com.example.explorandoelcosmos.model.JamesWebbImage;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class JamesWebbDataService {

    private static final String API_BASE_URL = "https://api.jwstapi.com/";
    // TODO: Replace with your actual James Webb API key.
    private static final String API_KEY = "6ea791ef-225a-48b6-bd02-1b21603c3b41";

    private final JamesWebbApi jamesWebbApi;
    private final Random random = new Random();

    public JamesWebbDataService() {
        this.jamesWebbApi = RetrofitClient.getClient(API_BASE_URL, "X-API-KEY", API_KEY).create(JamesWebbApi.class);
    }

    public String getRandomImageUrl() throws IOException {
        Call<JamesWebbApiResponse> call = jamesWebbApi.getAllImages();
        Response<JamesWebbApiResponse> response = call.execute();

        if (!response.isSuccessful()) {
            if (response.code() == 401) {
                throw new IOException("API Key inválida o no proporcionada para James Webb. Verifique su clave.");
            }
            throw new IOException("Error en la API de James Webb. Código: " + response.code());
        }

        JamesWebbApiResponse apiResponse = response.body();
        if (apiResponse == null || apiResponse.getBody() == null || apiResponse.getBody().isEmpty()) {
            throw new IOException("No se encontraron imágenes de James Webb.");
        }

        List<JamesWebbImage> images = apiResponse.getBody();

        // Filter for images that actually have a location
        List<JamesWebbImage> imagesWithLocation = images.stream()
                .filter(img -> img.getLocation() != null && !img.getLocation().isEmpty())
                .collect(Collectors.toList());

        if (imagesWithLocation.isEmpty()) {
            throw new IOException("No se encontraron imágenes con una ubicación (URL).");
        }

        JamesWebbImage randomImage = imagesWithLocation.get(random.nextInt(imagesWithLocation.size()));

        return randomImage.getLocation();
    }
}
