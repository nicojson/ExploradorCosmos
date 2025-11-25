package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaAsset {
    @SerializedName("collection")
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public static class Collection {
        @SerializedName("items")
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }
    }

    public static class Item {
        @SerializedName("href")
        private String href;

        public String getHref() {
            return href;
        }
    }
}
