package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rocket {

    @SerializedName("name")
    private String name;

    @SerializedName("flickr_images")
    private List<String> flickrImages;

    public String getName() {
        return name;
    }

    public List<String> getFlickrImages() {
        return flickrImages;
    }
}
