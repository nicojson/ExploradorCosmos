package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class NasaMetadata {
    @SerializedName("location")
    private String location;

    @SerializedName("AVAIL:Location")
    private String availLocation;

    @SerializedName("AVAIL:Photographer")
    private String photographer;

    @SerializedName("AVAIL:Description")
    private String description;

    public String getLocation() {
        return location != null ? location : availLocation;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getDescription() {
        return description;
    }
}
