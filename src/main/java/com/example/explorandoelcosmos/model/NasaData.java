package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaData {
    @SerializedName("nasa_id")
    private String nasaId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("date_created")
    private String dateCreated;

    @SerializedName("keywords")
    private List<String> keywords;

    @SerializedName("media_type")
    private String mediaType;

    public String getNasaId() { return nasaId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDateCreated() { return dateCreated; }
    public List<String> getKeywords() { return keywords; }
    public String getMediaType() { return mediaType; }
}
