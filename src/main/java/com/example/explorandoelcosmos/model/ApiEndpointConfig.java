package com.example.explorandoelcosmos.model;

public class ApiEndpointConfig {
    private String endpointName;
    private String apiKey;
    private String baseUrl;

    // Constructor completo
    public ApiEndpointConfig(String endpointName, String apiKey, String baseUrl) {
        this.endpointName = endpointName;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    // Constructor para endpoints que solo usan apiKey
    public ApiEndpointConfig(String endpointName, String apiKey) {
        this(endpointName, apiKey, null);
    }

    // Getters
    public String getEndpointName() {
        return endpointName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    // Setters (para permitir la modificación de valores)
    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String toString() {
        return endpointName; // Esto es útil para mostrar en ComboBoxes
    }
}
