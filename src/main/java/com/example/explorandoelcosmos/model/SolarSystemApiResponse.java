package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SolarSystemApiResponse {
    @SerializedName("bodies")
    private List<CelestialBody> bodies;

    public List<CelestialBody> getBodies() {
        return bodies;
    }

    public void setBodies(List<CelestialBody> bodies) {
        this.bodies = bodies;
    }
}
