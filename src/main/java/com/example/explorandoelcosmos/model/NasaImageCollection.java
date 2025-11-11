package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaImageCollection {
    @SerializedName("items")
    private List<NasaImageItem> items;

    public List<NasaImageItem> getItems() {
        return items;
    }

    public void setItems(List<NasaImageItem> items) {
        this.items = items;
    }
}
