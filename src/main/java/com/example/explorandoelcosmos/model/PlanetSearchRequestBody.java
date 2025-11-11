package com.example.explorandoelcosmos.model;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/**
 * Represents the body for a Planet API Quick Search POST request.
 */
public class PlanetSearchRequestBody {

    // For now, we will use a simple filter to get any images.
    @SerializedName("item_types")
    private final List<String> itemTypes = Collections.singletonList("PSScene");

    @SerializedName("filter")
    private final EmptyFilter filter = new EmptyFilter();

    /**
     * An empty class to represent an empty JSON object "{}" for the filter.
     */
    private static class EmptyFilter {
    }
}
