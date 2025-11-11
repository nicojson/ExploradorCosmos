package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.NasaApiResponse;
import com.example.explorandoelcosmos.model.NasaImageItem;
import com.example.explorandoelcosmos.model.NasaImageLink;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class NasaDataService {

    private static final String API_BASE_URL = "https://images-api.nasa.gov/";
    private final NasaApi nasaApi;
    private final Random random = new Random();

    public NasaDataService() {
        this.nasaApi = RetrofitClient.getClient(API_BASE_URL).create(NasaApi.class);
    }

    public String getRandomImageUrl(String query) throws IOException {
        Call<NasaApiResponse> call = nasaApi.searchImages(query, "image");
        Response<NasaApiResponse> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error en la API de NASA. Código de estado: " + response.code());
        }

        NasaApiResponse apiResponse = response.body();
        if (apiResponse == null || apiResponse.getCollection() == null || apiResponse.getCollection().getItems() == null || apiResponse.getCollection().getItems().isEmpty()) {
            throw new IOException("No se encontraron imágenes para la consulta: " + query);
        }

        List<NasaImageItem> items = apiResponse.getCollection().getItems();
        NasaImageItem randomItem = items.get(random.nextInt(items.size()));

        if (randomItem.getLinks() == null || randomItem.getLinks().isEmpty()) {
            throw new IOException("El item seleccionado no tiene imágenes.");
        }

        for (NasaImageLink link : randomItem.getLinks()) {
            if (link.getHref() != null && link.getHref().toLowerCase().endsWith(".jpg")) {
                return link.getHref();
            }
        }

        throw new IOException("No se encontró una imagen JPG en el item seleccionado.");
    }
}
