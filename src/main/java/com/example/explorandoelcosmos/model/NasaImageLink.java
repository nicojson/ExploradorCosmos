package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class NasaImageLink {
    @SerializedName("href")
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
