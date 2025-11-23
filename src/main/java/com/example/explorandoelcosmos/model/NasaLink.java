package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class NasaLink {
    @SerializedName("href")
    private String href;

    @SerializedName("rel")
    private String rel; // e.g., "preview"

    @SerializedName("render")
    private String render; // e.g., "image"

    public String getHref() { return href; }
    public String getRel() { return rel; }
    public String getRender() { return render; }
}
