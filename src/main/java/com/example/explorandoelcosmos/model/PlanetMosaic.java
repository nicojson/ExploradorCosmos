package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class PlanetMosaic {
    @SerializedName("name")
    private String name;

    @SerializedName("_links")
    private PlanetLinks links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanetLinks getLinks() {
        return links;
    }

    public void setLinks(PlanetLinks links) {
        this.links = links;
    }
}
