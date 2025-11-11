package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaImageItem {
    @SerializedName("links")
    private List<NasaImageLink> links;

    public List<NasaImageLink> getLinks() {
        return links;
    }

    public void setLinks(List<NasaImageLink> links) {
        this.links = links;
    }
}
