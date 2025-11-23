package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class MoonPhaseRequest {
    @SerializedName("format")
    private String format; // "png" o "svg"
    @SerializedName("style")
    private MoonPhaseStyle style;
    @SerializedName("observer")
    private MoonPhaseObserver observer;
    @SerializedName("view")
    private String view; // "topocentric" o "geocentric"
    @SerializedName("date")
    private String date; // Formato YYYY-MM-DD

    public MoonPhaseRequest(String date) {
        this.format = "png";
        this.style = new MoonPhaseStyle();
        this.observer = new MoonPhaseObserver();
        this.view = "topocentric";
        this.date = date;
    }

    // Getters (no setters necesarios si se construye con constructor)
    public String getFormat() { return format; }
    public MoonPhaseStyle getStyle() { return style; }
    public MoonPhaseObserver getObserver() { return observer; }
    public String getView() { return view; }
    public String getDate() { return date; }

    // Clases internas para anidar el JSON
    public static class MoonPhaseStyle {
        @SerializedName("moonStyle")
        private String moonStyle; // "default", "sketch", "shaded"
        @SerializedName("backgroundStyle")
        private String backgroundStyle; // "default", "stars", "transparent"
        @SerializedName("headingColor")
        private String headingColor; // Color hexadecimal
        @SerializedName("textColor")
        private String textColor; // Color hexadecimal

        public MoonPhaseStyle() {
            this.moonStyle = "default";
            this.backgroundStyle = "stars";
            this.headingColor = "#ffffff";
            this.textColor = "#ffffff";
        }
    }

    public static class MoonPhaseObserver {
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("longitude")
        private double longitude;
        @SerializedName("date")
        private String date; // Puede ser diferente al de la solicitud principal, o el mismo

        public MoonPhaseObserver() {
            this.latitude = 34.0; // Latitud de ejemplo (Celaya, GTO)
            this.longitude = -100.0; // Longitud de ejemplo
            this.date = null; // Usará la fecha de la solicitud principal si es null
        }
    }
}
