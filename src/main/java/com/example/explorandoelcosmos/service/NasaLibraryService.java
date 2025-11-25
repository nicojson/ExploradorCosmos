package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.NasaLibraryResponse;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for interacting with NASA Image and Video Library API
 * API Documentation: https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf
 */
public class NasaLibraryService {

    // Limit results to prevent performance issues
    private static final int MAX_RESULTS_PER_TYPE = 12; // 12 images + 12 videos = 24 total

    private final NasaApiService nasaApiService;

    public NasaLibraryService() {
        this.nasaApiService = NasaApiClient.getNasaLibraryService();
    }

    /**
     * Search for images in the NASA Library
     * 
     * @param query     Search query (e.g., "mars", "apollo")
     * @param yearStart Starting year for filtering (null for no filter)
     * @param yearEnd   Ending year for filtering (null for no filter)
     * @return List of NASA items containing images
     */
    public List<NasaItem> searchImages(String query, Integer yearStart, Integer yearEnd) throws IOException {
        return search(query, "image", yearStart, yearEnd);
    }

    /**
     * Search for videos in the NASA Library
     * 
     * @param query     Search query (e.g., "mars", "apollo")
     * @param yearStart Starting year for filtering (null for no filter)
     * @param yearEnd   Ending year for filtering (null for no filter)
     * @return List of NASA items containing videos
     */
    public List<NasaItem> searchVideos(String query, Integer yearStart, Integer yearEnd) throws IOException {
        return search(query, "video", yearStart, yearEnd);
    }

    /**
     * Search for both images and videos in the NASA Library
     * 
     * @param query     Search query (e.g., "mars", "apollo")
     * @param yearStart Starting year for filtering (null for no filter)
     * @param yearEnd   Ending year for filtering (null for no filter)
     * @return List of NASA items containing both images and videos
     */
    public List<NasaItem> searchImagesAndVideos(String query, Integer yearStart, Integer yearEnd) throws IOException {
        List<NasaItem> allItems = new ArrayList<>();
        allItems.addAll(searchImages(query, yearStart, yearEnd));
        allItems.addAll(searchVideos(query, yearStart, yearEnd));
        return allItems;
    }

    /**
     * Generic search method
     */
    private List<NasaItem> search(String query, String mediaType, Integer yearStart, Integer yearEnd)
            throws IOException {
        // Handle null or 0 values for year filtering
        Integer start = (yearStart != null && yearStart > 0) ? yearStart : null;
        Integer end = (yearEnd != null && yearEnd > 0) ? yearEnd : null;

        // If query is empty, use a default popular search term
        String searchQuery = (query == null || query.trim().isEmpty()) ? "nasa" : query;

        Response<NasaLibraryResponse> response = nasaApiService.searchNasaLibrary(
                searchQuery,
                mediaType,
                start,
                end).execute();

        if (response.isSuccessful() && response.body() != null && response.body().getCollection() != null) {
            List<NasaItem> items = response.body().getCollection().getItems();

            // Limit results to prevent performance issues
            int originalSize = items.size();
            if (items.size() > MAX_RESULTS_PER_TYPE) {
                items = items.subList(0, MAX_RESULTS_PER_TYPE);
                System.out.println("NASA Library API: Limited results from " + originalSize + " to "
                        + MAX_RESULTS_PER_TYPE + " for performance");
            }

            System.out.println(
                    "NASA Library API: Returning " + items.size() + " " + mediaType + "(s) for query: " + searchQuery);
            return items;
        } else {
            System.err.println("Error searching NASA Library: " + response.code() + " - " + response.message());
            if (response.errorBody() != null) {
                System.err.println("Error body: " + response.errorBody().string());
            }
            return Collections.emptyList();
        }
    }

    /**
     * Search for images and videos as Publication objects for UI display
     */
    public List<com.example.explorandoelcosmos.model.Publication> searchImagesAsPublications(String query,
            Integer yearStart, Integer yearEnd) throws IOException {
        // Search for both images and videos
        List<NasaItem> items = searchImagesAndVideos(query, yearStart, yearEnd);
        return items.stream()
                .map(this::mapNasaItemToPublication)
                .filter(pub -> pub != null)
                .collect(Collectors.toList());
    }

    /**
     * Maps a NasaItem to a Publication object for consistent UI display
     */
    /**
     * Get asset manifest (video URLs)
     */
    public com.example.explorandoelcosmos.model.NasaAsset getAsset(String nasaId) throws IOException {
        Response<com.example.explorandoelcosmos.model.NasaAsset> response = nasaApiService.getAsset(nasaId).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        return null;
    }

    /**
     * Get asset metadata
     */
    public com.example.explorandoelcosmos.model.NasaMetadata getMetadata(String nasaId) throws IOException {
        Response<com.example.explorandoelcosmos.model.NasaMetadata> response = nasaApiService.getMetadata(nasaId)
                .execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        return null;
    }

    private com.example.explorandoelcosmos.model.Publication mapNasaItemToPublication(NasaItem item) {
        if (item.getData() == null || item.getData().isEmpty()) {
            return null;
        }
        com.example.explorandoelcosmos.model.NasaData data = item.getData().get(0);
        String imageUrl = item.getPreviewImageUrl();

        java.time.LocalDateTime date = java.time.LocalDateTime.now();
        if (data.getDateCreated() != null) {
            try {
                date = java.time.ZonedDateTime.parse(data.getDateCreated()).toLocalDateTime();
            } catch (Exception e) {
                // Fallback to now if parsing fails
                System.err.println("Failed to parse date: " + data.getDateCreated());
            }
        }

        return new com.example.explorandoelcosmos.model.Publication(
                2, // ID 2 for NASA Library
                data.getNasaId(),
                data.getMediaType(),
                data.getTitle(),
                data.getDescription(),
                imageUrl,
                date);
    }
}
