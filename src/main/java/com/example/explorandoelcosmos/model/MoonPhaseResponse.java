package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class MoonPhaseResponse {
    @SerializedName("data")
    private MoonPhaseData data;

    public MoonPhaseData getData() {
        return data;
    }

    public static class MoonPhaseData {
        @SerializedName("imageUrl")
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
