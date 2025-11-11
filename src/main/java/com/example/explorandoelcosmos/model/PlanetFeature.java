package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class PlanetFeature {
    @SerializedName("_links")
    private PlanetFeatureLinks links;

    public PlanetFeatureLinks getLinks() {
        return links;
    }

    public void setLinks(PlanetFeatureLinks links) {
        this.links = links;
    }
}
