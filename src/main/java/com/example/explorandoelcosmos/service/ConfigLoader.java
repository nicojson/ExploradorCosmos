package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigLoader {

    private static final AppConfigDAO configDAO = new AppConfigDAOImpl();
    private static final Map<String, ApiEndpointConfig> apiConfigs = new HashMap<>();

    public static String getApiKey(String key) {
        // Busca la clave en la base de datos y devuelve el valor, o una cadena vacía si
        // no se encuentra.
        return configDAO.findByKey(key).orElse("");
    }

    public static void loadApiEndpoints() {
        System.out.println("Cargando configuraciones de API...");
        List<ApiEndpointConfig> configs = configDAO.findAllEndpointConfigs();
        apiConfigs.clear();
        for (ApiEndpointConfig config : configs) {
            apiConfigs.put(config.getEndpointName(), config);
            System.out.println("Configuración cargada para: " + config.getEndpointName());
        }
    }

    public static Optional<ApiEndpointConfig> getEndpointConfig(String name) {
        if (apiConfigs.containsKey(name)) {
            return Optional.of(apiConfigs.get(name));
        }
        // Fallback: intentar cargar desde DB si no está en caché (por si acaso)
        return configDAO.findEndpointConfigByName(name);
    }
}
