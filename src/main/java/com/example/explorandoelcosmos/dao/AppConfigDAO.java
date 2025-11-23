package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.ApiEndpointConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppConfigDAO {
    Optional<String> findByKey(String key);
    Map<String, String> findAllApiKeys();
    void save(String key, String value);

    // Nuevos métodos para gestionar ApiEndpointConfig
    void saveEndpointConfig(ApiEndpointConfig config);
    Optional<ApiEndpointConfig> findEndpointConfigByName(String endpointName);
    List<ApiEndpointConfig> findAllEndpointConfigs();
    void deleteEndpointConfig(String endpointName);
}
