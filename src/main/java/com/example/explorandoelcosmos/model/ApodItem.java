package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;

public class ApodItem {
    @SerializedName("date")
    private String date;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("service_version")
    private String serviceVersion;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("hdurl")
    private String hdUrl;

    public String getDate() { return date; }
    public String getExplanation() { return explanation; }
    public String getMediaType() { return mediaType; }
    public String getServiceVersion() { return serviceVersion; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getHdUrl() { return hdUrl; }
}