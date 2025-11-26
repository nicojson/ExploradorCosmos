package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NasaItem {


    @SerializedName("href")
    private String href;


    @SerializedName("data")
    private List<NasaData> data;

    @SerializedName("links")
    private List<NasaLink> links;


    public String getHref() { return href; }


    public List<NasaData> getData() { return data; }
    public List<NasaLink> getLinks() { return links; }

    public String getPreviewImageUrl() {
        if (links != null && !links.isEmpty()) {
            return links.stream()
                    .filter(link -> "preview".equals(link.getRel()) || "image".equals(link.getRender()))
                    .map(NasaLink::getHref)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}