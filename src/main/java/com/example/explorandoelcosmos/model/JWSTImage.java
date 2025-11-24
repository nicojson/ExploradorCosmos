package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class JWSTImage {
    @SerializedName("id")
    private String id;

    @SerializedName("location")
    private String location;

    @SerializedName("details")
    private JWSTDetails details;

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public JWSTDetails getDetails() {
        return details;
    }

    public static class JWSTDetails {
        @SerializedName("mission")
        private String mission;

        @SerializedName("description")
        private String description;

        public String getMission() {
            return mission;
        }

        public String getDescription() {
            return description;
        }
    }
}
