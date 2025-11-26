package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.NasaLibraryResponse;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.model.NasaData;
import com.example.explorandoelcosmos.model.NasaLink;
import retrofit2.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NasaLibraryService {

    private static final int MAX_RESULTS = 50; // Límite para no saturar la memoria
    private final NasaApiService nasaApiService;

    public NasaLibraryService() {
        this.nasaApiService = NasaApiClient.getNasaLibraryService();
    }


    public List<Publication> searchImagesAsPublications(String query, Integer yearStart, Integer yearEnd) throws IOException {

        String searchQuery = (query == null || query.trim().isEmpty()) ? "galaxy" : query;


        Integer start = (yearStart != null && yearStart > 0) ? yearStart : null;
        Integer end = (yearEnd != null && yearEnd > 0) ? yearEnd : null;

        Response<NasaLibraryResponse> response = nasaApiService.searchNasaLibrary(
                searchQuery,
                "image,video",
                start,
                end
        ).execute();

        if (response.isSuccessful() && response.body() != null && response.body().getCollection() != null) {
            List<NasaItem> items = response.body().getCollection().getItems();

            if (items.size() > MAX_RESULTS) {
                items = items.subList(0, MAX_RESULTS);
            }

            return items.stream()
                    .map(this::mapNasaItemToPublication)
                    .filter(p -> p != null)
                    .collect(Collectors.toList());
        } else {

            System.err.println("NASA API Error: " + response.code());
            return Collections.emptyList();
        }
    }

    private Publication mapNasaItemToPublication(NasaItem item) {
        try {
            if (item.getData() == null || item.getData().isEmpty()) return null;

            NasaData data = item.getData().get(0);
            String imageUrl = null;

            // 1. Buscar imagen de vista previa (thumbnail)
            if (item.getLinks() != null) {
                for (NasaLink link : item.getLinks()) {
                    if ("preview".equals(link.getRel()) || "image".equals(link.getRender())) {
                        imageUrl = link.getHref().replace(" ", "%20");
                        break;
                    }
                }
            }

            // 2. Si no hay imagen, poner una por defecto
            if (imageUrl == null) {
                if ("audio".equals(data.getMediaType())) {
                    imageUrl = "https://images-assets.nasa.gov/image/NASA-Logos-Swoosh/NASA-Logos-Swoosh~thumb.jpg";
                } else {
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/NASA_logo.svg/1200px-NASA_logo.svg.png";
                }
            }


            String videoUrl = null;

            if ("video".equals(data.getMediaType()) && item.getHref() != null) {

                String baseUrl = item.getHref();

                // Le quitamos la parte final para quedarnos con la carpeta
                if (baseUrl.endsWith("collection.json")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - "collection.json".length());
                }


                videoUrl = baseUrl + data.getNasaId() + "~orig.mp4";


                videoUrl = videoUrl.replace(" ", "%20");
            }

            LocalDateTime date = LocalDateTime.now();
            if (data.getDateCreated() != null) {
                try {
                    date = LocalDateTime.parse(data.getDateCreated(), DateTimeFormatter.ISO_DATE_TIME);
                } catch (Exception ignored) {}
            }


            Publication pub = new Publication(
                    2,
                    data.getNasaId(),
                    data.getMediaType(),
                    data.getTitle(),
                    data.getDescription(),
                    imageUrl,
                    date
            );


            if (videoUrl != null) {
                pub.setContentUrl(videoUrl);
            }

            return pub;

        } catch (Exception e) {
            return null;
        }
    }
}