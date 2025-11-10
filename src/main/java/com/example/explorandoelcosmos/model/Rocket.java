package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// POJO para deserializar la info de un cohete
public class Rocket {

    @SerializedName("name")
    private String name;

    // La documentación
    // indica que 'flickr_images' es un array de Strings
    @SerializedName("flickr_images")
    private List<String> flickrImages;

    // --- Getters ---

    public String getName() {
        return name;
    }

    public List<String> getFlickrImages() {
        return flickrImages;
    }
}
