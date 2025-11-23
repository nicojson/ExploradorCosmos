package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaLibraryResponse {
    @SerializedName("collection")
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public static class Collection {
        @SerializedName("items")
        private List<NasaItem> items;

        public List<NasaItem> getItems() {
            return items;
        }
    }
}
