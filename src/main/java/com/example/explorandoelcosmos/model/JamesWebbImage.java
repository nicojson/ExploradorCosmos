package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class JamesWebbImage {
    @SerializedName("location")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
