package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class CelestialBody {
    @SerializedName("englishName")
    private String englishName;

    @SerializedName("bodyType")
    private String bodyType;

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isPlanet() {
        return "Planet".equalsIgnoreCase(this.bodyType);
    }
}
