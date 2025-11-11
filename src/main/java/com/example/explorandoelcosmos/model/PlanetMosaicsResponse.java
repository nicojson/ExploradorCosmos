package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlanetMosaicsResponse {
    @SerializedName("mosaics")
    private List<PlanetMosaic> mosaics;

    public List<PlanetMosaic> getMosaics() {
        return mosaics;
    }

    public void setMosaics(List<PlanetMosaic> mosaics) {
        this.mosaics = mosaics;
    }
}
