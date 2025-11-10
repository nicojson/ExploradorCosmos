package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class Patch {
    @SerializedName("large")
    private String large;

    public String getLarge() {
        return large;
    }
}
