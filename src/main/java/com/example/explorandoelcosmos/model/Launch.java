package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List; // Asegúrate de tener este import si usas List

public class Launch {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("flight_number")
    private int flightNumber;

    @SerializedName("date_utc")
    private Date dateUtc;

    @SerializedName("details")
    private String details;

    @SerializedName("success")
    private Boolean success;

    // === NUEVO CAMPO NECESARIO ===
    @SerializedName("rocket")
    private String rocket;   // <--- AGREGAR ESTO
    // =============================

    @SerializedName("links")
    private Links links;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getFlightNumber() { return flightNumber; }
    public Date getDateUtc() { return dateUtc; }
    public String getDetails() { return details; }
    public Boolean isSuccess() { return success; }

    // === NUEVO GETTER NECESARIO ===
    public String getRocket() { return rocket; } // <--- AGREGAR ESTO
    // ==============================

    public Links getLinks() { return links; }

    public static class Links {
        @SerializedName("patch")
        private Patch patch;

        @SerializedName("flickr")
        private Flickr flickr;

        @SerializedName("webcast")
        private String webcast;

        public Patch getPatch() { return patch; }
        public Flickr getFlickr() { return flickr; }
        public String getWebcast() { return webcast; }
    }

    public static class Patch {
        @SerializedName("small")
        private String small;
        @SerializedName("large")
        private String large;

        public String getSmall() { return small; }
        public String getLarge() { return large; }
    }

    public static class Flickr {
        @SerializedName("original")
        private List<String> original;

        public List<String> getOriginal() { return original; }
    }
}