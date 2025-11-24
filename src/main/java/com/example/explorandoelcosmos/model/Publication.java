package com.example.explorandoelcosmos.model;

import java.time.LocalDateTime;

public class Publication {
    private int id;
    private int sourceApiId;
    private String originalIdFromApi;
    private String contentType; // 'image', 'video', 'article', 'report'
    private String title;
    private String description;
    private String mainImageUrl;
    private LocalDateTime publishedDate;
    private LocalDateTime fetchedAt;
    private boolean isFavorite;
    private String localPath;

    public Publication() {
    }

    public Publication(int sourceApiId, String originalIdFromApi, String contentType, String title, String description,
            String mainImageUrl, LocalDateTime publishedDate) {
        this.sourceApiId = sourceApiId;
        this.originalIdFromApi = originalIdFromApi;
        this.contentType = contentType;
        this.title = title;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.publishedDate = publishedDate;
        this.fetchedAt = LocalDateTime.now();
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(int sourceApiId) {
        this.sourceApiId = sourceApiId;
    }

    public String getOriginalIdFromApi() {
        return originalIdFromApi;
    }

    public void setOriginalIdFromApi(String originalIdFromApi) {
        this.originalIdFromApi = originalIdFromApi;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
