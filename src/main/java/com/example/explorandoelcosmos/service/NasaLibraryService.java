package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.NasaLibraryResponse;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NasaLibraryService {

    private final NasaApiService nasaApiService;

    public NasaLibraryService() {
        this.nasaApiService = NasaApiClient.getNasaLibraryService();
    }

    public List<NasaItem> searchImages(String query, Integer yearStart, Integer yearEnd) throws IOException {
        Response<NasaLibraryResponse> response = nasaApiService.searchNasaLibrary(
                query,
                "image", // Solo buscar imágenes por ahora
                yearStart,
                yearEnd,
                NasaApiClient.getApiKey()
        ).execute();

        if (response.isSuccessful() && response.body() != null && response.body().getCollection() != null) {
            return response.body().getCollection().getItems();
        } else {
            System.err.println("Error al buscar en la biblioteca de la NASA: " + response.message());
            return Collections.emptyList();
        }
    }
}
