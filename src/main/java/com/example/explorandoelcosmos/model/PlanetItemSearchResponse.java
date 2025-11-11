package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlanetItemSearchResponse {
    @SerializedName("features")
    private List<PlanetFeature> features;

    public List<PlanetFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<PlanetFeature> features) {
        this.features = features;
    }
}
