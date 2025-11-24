package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.Launch;
import com.example.explorandoelcosmos.model.Rocket;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SpaceXDataService {

    private static final String API_BASE_URL = "https://api.spacexdata.com/v4/";
    private final SpaceXApi spaceXApi;

    public SpaceXDataService() {
        this.spaceXApi = RetrofitClient.getClient(API_BASE_URL).create(SpaceXApi.class);
    }

    public List<Rocket> getRockets() throws IOException {
        Call<List<Rocket>> call = spaceXApi.getRockets();
        Response<List<Rocket>> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error en la API de SpaceX. Código de estado: " + response.code());
        }

        List<Rocket> rockets = response.body();

        if (rockets == null || rockets.isEmpty()) {
            throw new IOException("No se encontraron cohetes.");
        }

        // Filtrar cohetes que no tienen imágenes para evitar tarjetas vacías
        return rockets.stream()
                .filter(rocket -> rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty())
                .collect(Collectors.toList());
    }

    public Launch getLatestLaunch() throws IOException {
        Response<Launch> response = spaceXApi.getLatestLaunch().execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Error al obtener el último lanzamiento: " + response.code());
        }
        return response.body();
    }

    public List<Launch> getAllLaunches() throws IOException {
        Response<List<Launch>> response = spaceXApi.getAllLaunches().execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Error al obtener el historial de lanzamientos: " + response.code());
        }
        return response.body();
    }

    // === Métodos de Mapeo a Publication ===

    public List<com.example.explorandoelcosmos.model.Publication> getRocketsAsPublications() throws IOException {
        List<Rocket> rockets = getRockets();
        return rockets.stream().map(this::mapRocketToPublication).collect(Collectors.toList());
    }

    public List<com.example.explorandoelcosmos.model.Publication> getLaunchesAsPublications() throws IOException {
        List<Launch> launches = getAllLaunches();
        return launches.stream().map(this::mapLaunchToPublication).collect(Collectors.toList());
    }

    private com.example.explorandoelcosmos.model.Publication mapRocketToPublication(Rocket rocket) {
        String imageUrl = (rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty())
                ? rocket.getFlickrImages().get(0)
                : null;

        return new com.example.explorandoelcosmos.model.Publication(
                1, // Assuming ID 1 for SpaceX
                rocket.getName(), // Using name as ID since API doesn't provide ID for rockets in this model
                "image",
                rocket.getName(),
                rocket.getDescription(),
                imageUrl,
                java.time.LocalDateTime.now() // Rockets don't have a specific date in this model
        );
    }

    private com.example.explorandoelcosmos.model.Publication mapLaunchToPublication(Launch launch) {
        java.time.LocalDateTime date = launch.getDateUtc() != null
                ? launch.getDateUtc().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                : java.time.LocalDateTime.now();

        return new com.example.explorandoelcosmos.model.Publication(
                1, // Assuming ID 1 for SpaceX
                launch.getId(),
                "article", // Launches are more like articles/reports
                launch.getName(),
                launch.getDetails(),
                null, // Launches might not have a direct image in this simple model, or we need to
                      // fetch it
                date);
    }
}
