package com.example.explorandoelcosmos.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; // ¡Importante!
import com.example.explorandoelcosmos.model.Launch;
import com.example.explorandoelcosmos.model.Rocket; // ¡Importante!

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList; // ¡Importante!
import java.util.List;      // ¡Importante!
import java.util.Random;    // ¡Importante!

public class SpaceXDataService {

    private static final SpaceXDataService INSTANCE = new SpaceXDataService();

    // Actualizamos las URLs base para V4 y V5
    private static final String API_V5_URL = "https://api.spacexdata.com/v5";
    private static final String API_V4_URL = "https://api.spacexdata.com/v4";

    private final HttpClient httpClient;
    private final Gson gson;
    private final Random random = new Random(); // Añadimos un generador aleatorio

    private SpaceXDataService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    public static SpaceXDataService getInstance() {
        return INSTANCE;
    }

    // Este método lo conservamos como estaba
    public Launch getLatestLaunch() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_V5_URL + "/launches/latest")) //
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Error en la API. Código de estado: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Launch.class);
    }

    // --- ¡NUEVO MÉTODO! ---
    /**
     * Obtiene la URL de una imagen aleatoria de la galería de todos los cohetes.
     * Esta es una llamada BLOQUEANTE.
     *
     * @return String con la URL de una imagen.
     * @throws IOException
     * @throws InterruptedException
     */
    public String getRandomRocketImage() throws IOException, InterruptedException {
        // 1. Llamamos al endpoint de cohetes
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_V4_URL + "/rockets"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error en la API. Código de estado: " + response.statusCode());
        }

        // 2. Parseamos la respuesta. Como es una lista (List<Rocket>), usamos un TypeToken
        java.lang.reflect.Type rocketListType = new TypeToken<List<Rocket>>() {}.getType();
        List<Rocket> rockets = gson.fromJson(response.body(), rocketListType);

        if (rockets == null || rockets.isEmpty()) {
            throw new IOException("No se encontraron cohetes.");
        }

        // 3. Creamos una gran lista con TODAS las imágenes de TODOS los cohetes
        List<String> allImages = new ArrayList<>();
        for (Rocket rocket : rockets) {
            if (rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty()) {
                allImages.addAll(rocket.getFlickrImages()); //
            }
        }

        if (allImages.isEmpty()) {
            throw new IOException("No se encontraron imágenes en la galería de cohetes.");
        }

        // 4. Seleccionamos y devolvemos una URL aleatoria de esa lista
        return allImages.get(random.nextInt(allImages.size()));
    }
}