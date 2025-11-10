package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName("patch")
    private Patch patch;

    public Patch getPatch() {
        return patch;
    }
}