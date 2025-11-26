package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.ApodItem;
import com.example.explorandoelcosmos.model.Publication;
import retrofit2.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ApodService {

    private final NasaPlanetaryApi api;
    private final String apiKey;

    public ApodService() {
        this.api = NasaApiClient.getNasaPlanetaryApiService();
        String key = NasaApiClient.getApiKey();
        this.apiKey = (key != null && !key.isEmpty()) ? key : "LEerXFKL30N6ntb1JmhqeBXgrhFRsBzNe2ic0Wo8";
    }

    public Publication getTodaysApodAsPublication() throws IOException {
        Response<ApodItem> response = api.getTodaysApod(apiKey).execute();
        if (response.isSuccessful() && response.body() != null) {
            return mapApodToPublication(response.body());
        } else {
            throw new IOException("Error obteniendo APOD: " + response.code());
        }
    }

    public List<Publication> getApodGallery(int count) throws IOException {
        Response<List<ApodItem>> response = api.getApodList(apiKey, count).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().stream().map(this::mapApodToPublication).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Publication mapApodToPublication(ApodItem item) {
        String imageUrl;
        String videoUrl = null;
        String contentType = "image";

        if ("video".equals(item.getMediaType())) {
            contentType = "video";
            videoUrl = item.getUrl();
            imageUrl = "https://www.nasa.gov/sites/default/files/thumbnails/image/nasa-logo-web-rgb.png";

            if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
                try {
                    String videoId = "";
                    if (videoUrl.contains("embed/")) {
                        videoId = videoUrl.split("embed/")[1].split("\\?")[0];
                    }
                    if (!videoId.isEmpty()) {
                        imageUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
                    }
                } catch (Exception ignored) {}
            }
        } else {
            imageUrl = item.getUrl();
        }

        java.time.LocalDateTime date = java.time.LocalDateTime.now();
        try {
            date = LocalDate.parse(item.getDate(), DateTimeFormatter.ISO_DATE).atStartOfDay();
        } catch (Exception ignored) {}

        Publication pub = new Publication(
                3,
                item.getDate(),
                contentType,
                item.getTitle(),
                item.getExplanation(),
                imageUrl,
                date
        );

        if (videoUrl != null) {
            pub.setContentUrl(videoUrl);
        }

        pub.setServiceVersion(item.getServiceVersion());

        return pub;
    }
}