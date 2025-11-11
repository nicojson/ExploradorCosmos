package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JamesWebbApiResponse {
    @SerializedName("body")
    private List<JamesWebbImage> body;

    public List<JamesWebbImage> getBody() {
        return body;
    }

    public void setBody(List<JamesWebbImage> body) {
        this.body = body;
    }
}
