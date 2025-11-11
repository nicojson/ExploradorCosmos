package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class NasaApiResponse {
    @SerializedName("collection")
    private NasaImageCollection collection;

    public NasaImageCollection getCollection() {
        return collection;
    }

    public void setCollection(NasaImageCollection collection) {
        this.collection = collection;
    }
}
