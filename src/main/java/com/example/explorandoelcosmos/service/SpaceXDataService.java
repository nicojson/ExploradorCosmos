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

    private static final String[] GENERIC_IMAGES = {
            "https://live.staticflickr.com/65535/49673373182_93a517e140_b.jpg",
            "https://live.staticflickr.com/65535/51702654584_13a4b39655_b.jpg",
            "https://live.staticflickr.com/65535/50065947263_e1a6ea1e22_b.jpg",
            "https://live.staticflickr.com/65535/51818737408_435196f856_b.jpg",
            "https://live.staticflickr.com/65535/51981688502_0584ac5658_b.jpg",
            "https://live.staticflickr.com/65535/51136761295_edb4d3ba1d_b.jpg"
    };

    private final SpaceXApi spaceXApi;

    public SpaceXDataService() {
        this.spaceXApi = RetrofitClient.getClient(API_BASE_URL).create(SpaceXApi.class);
    }

    public List<Rocket> getRockets() throws IOException {
        Call<List<Rocket>> call = spaceXApi.getRockets();
        Response<List<Rocket>> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code());
        }
        return response.body();
    }

    public Launch getLatestLaunch() throws IOException {
        Response<Launch> response = spaceXApi.getLatestLaunch().execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Error: " + response.code());
        }
        return response.body();
    }

    public List<Launch> getAllLaunches() throws IOException {
        Response<List<Launch>> response = spaceXApi.getAllLaunches().execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Error: " + response.code());
        }
        return response.body();
    }

    public List<com.example.explorandoelcosmos.model.Publication> getRocketsAsPublications() throws IOException {
        List<Rocket> rockets = getRockets();
        if (rockets == null) return java.util.Collections.emptyList();
        return rockets.stream().map(this::mapRocketToPublication).collect(Collectors.toList());
    }

    public List<com.example.explorandoelcosmos.model.Publication> getLaunchesAsPublications() throws IOException {
        List<Launch> launches = getAllLaunches();
        if (launches == null) return java.util.Collections.emptyList();
        return launches.stream().map(this::mapLaunchToPublication).collect(Collectors.toList());
    }

    private com.example.explorandoelcosmos.model.Publication mapRocketToPublication(Rocket rocket) {
        String imageUrl = null;

        if (rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty()) {
            imageUrl = rocket.getFlickrImages().get(0);
        }

        if ("Falcon 1".equals(rocket.getName())) {
            imageUrl = "https://live.staticflickr.com/65535/49673373182_93a517e140_b.jpg";
        }

        if (imageUrl == null) {
            imageUrl = GENERIC_IMAGES[0];
        }

        return new com.example.explorandoelcosmos.model.Publication(
                1,
                rocket.getName(),
                "image",
                rocket.getName(),
                rocket.getDescription(),
                imageUrl,
                java.time.LocalDateTime.now()
        );
    }

    private com.example.explorandoelcosmos.model.Publication mapLaunchToPublication(Launch launch) {
        java.time.LocalDateTime date = launch.getDateUtc() != null
                ? launch.getDateUtc().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                : java.time.LocalDateTime.now();

        String imageUrl = null;

        if (launch.getLinks() != null
                && launch.getLinks().getFlickr() != null
                && launch.getLinks().getFlickr().getOriginal() != null
                && !launch.getLinks().getFlickr().getOriginal().isEmpty()) {

            imageUrl = launch.getLinks().getFlickr().getOriginal().get(0);
        }

        if (imageUrl == null
                && launch.getLinks() != null
                && launch.getLinks().getPatch() != null) {

            imageUrl = launch.getLinks().getPatch().getLarge();
        }

        if ("Falcon 1".equals(launch.getName())) {
            imageUrl = "https://live.staticflickr.com/65535/49673373182_93a517e140_b.jpg";
        }

        if (imageUrl == null) {
            int index = Math.abs(launch.getId().hashCode()) % GENERIC_IMAGES.length;
            imageUrl = GENERIC_IMAGES[index];
        }

        com.example.explorandoelcosmos.model.Publication pub = new com.example.explorandoelcosmos.model.Publication(
                1,
                launch.getId(),
                "image",
                launch.getName(),
                launch.getDetails() != null ? launch.getDetails() : "Lanzamiento sin descripción detallada.",
                imageUrl,
                date);

        if (launch.getLinks() != null && launch.getLinks().getWebcast() != null) {
            pub.setContentType("video");
            String videoUrl = launch.getLinks().getWebcast();
            if (videoUrl.contains("watch?v=")) {
                videoUrl = videoUrl.replace("watch?v=", "embed/");
            }
            pub.setContentUrl(videoUrl);
        }

        return pub;
    }
}