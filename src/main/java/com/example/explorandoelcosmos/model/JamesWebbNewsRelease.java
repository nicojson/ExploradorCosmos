package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class JamesWebbNewsRelease {
    @SerializedName("thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
