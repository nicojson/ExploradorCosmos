package com.example.explorandoelcosmos.model;

public class ApiEndpointConfig {
    private String endpointName;
    private String apiKey;
    private String appId;
    private String appSecret;

    // Constructor completo
    public ApiEndpointConfig(String endpointName, String apiKey, String appId, String appSecret) {
        this.endpointName = endpointName;
        this.apiKey = apiKey;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    // Constructor para endpoints que solo usan apiKey
    public ApiEndpointConfig(String endpointName, String apiKey) {
        this(endpointName, apiKey, null, null);
    }

    // Constructor para endpoints que solo usan appId/appSecret
    public ApiEndpointConfig(String endpointName, String appId, String appSecret) {
        this(endpointName, null, appId, appSecret);
    }

    // Getters
    public String getEndpointName() { return endpointName; }
    public String getApiKey() { return apiKey; }
    public String getAppId() { return appId; }
    public String getAppSecret() { return appSecret; }

    // Setters (para permitir la modificación de valores)
    public void setEndpointName(String endpointName) { this.endpointName = endpointName; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setAppId(String appId) { this.appId = appId; }
    public void setAppSecret(String appSecret) { this.appSecret = appSecret; }

    @Override
    public String toString() {
        return endpointName; // Esto es útil para mostrar en ComboBoxes
    }
}
