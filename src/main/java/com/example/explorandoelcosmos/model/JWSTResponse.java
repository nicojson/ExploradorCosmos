package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JWSTResponse {
    @SerializedName("body")
    private List<JWSTImage> body;

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("error")
    private String error;

    public List<JWSTImage> getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }
}
