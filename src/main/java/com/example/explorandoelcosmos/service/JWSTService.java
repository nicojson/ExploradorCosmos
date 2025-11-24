package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.JWSTImage;
import com.example.explorandoelcosmos.model.JWSTResponse;
import com.example.explorandoelcosmos.model.Publication;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JWSTService {

    private final JWSTApi jwstApi;

    public JWSTService() {
        this.jwstApi = NasaApiClient.getJwstApiService();
    }

    public List<JWSTImage> getImages(int page, int perPage) throws IOException {
        Response<JWSTResponse> response = jwstApi.getAllImages(page, perPage).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getBody();
        } else {
            throw new IOException("Error al obtener imágenes de JWST: " + response.code());
        }
    }

    public List<Publication> getImagesAsPublications(int page, int perPage) throws IOException {
        List<JWSTImage> images = getImages(page, perPage);
        if (images == null)
            return Collections.emptyList();

        return images.stream().map(this::mapToPublication).collect(Collectors.toList());
    }

    private Publication mapToPublication(JWSTImage image) {
        String description = "Sin descripción";
        String title = "JWST Image " + image.getId();

        if (image.getDetails() != null) {
            if (image.getDetails().getDescription() != null) {
                description = image.getDetails().getDescription();
            }
            if (image.getDetails().getMission() != null) {
                title = image.getDetails().getMission() + " - " + image.getId();
            }
        }

        return new Publication(
                3, // Assuming ID 3 for JWST
                image.getId(),
                "image",
                title,
                description,
                image.getLocation(),
                LocalDateTime.now() // JWST API might not return date in this endpoint
        );
    }
}
