package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.model.ApiEndpointConfig;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    @Test
    public void testLoadApiEndpoints() {
        // Force loading
        ConfigLoader.loadApiEndpoints();

        // Check for default endpoints that should be in the DB (inserted by schema.sql)
        Optional<ApiEndpointConfig> nasaConfig = ConfigLoader.getEndpointConfig("NASA");
        assertTrue(nasaConfig.isPresent(), "NASA endpoint config should be present");
        assertEquals("https://images-api.nasa.gov", nasaConfig.get().getBaseUrl());

        Optional<ApiEndpointConfig> spaceXConfig = ConfigLoader.getEndpointConfig("SpaceX");
        assertTrue(spaceXConfig.isPresent(), "SpaceX endpoint config should be present");
        assertEquals("https://api.spacexdata.com/v4", spaceXConfig.get().getBaseUrl());

        Optional<ApiEndpointConfig> jwstConfig = ConfigLoader.getEndpointConfig("JWST");
        assertTrue(jwstConfig.isPresent(), "JWST endpoint config should be present");
        assertEquals("https://api.jwstapi.com", jwstConfig.get().getBaseUrl());

        Optional<ApiEndpointConfig> astronomyConfig = ConfigLoader.getEndpointConfig("Astronomy API");
        assertTrue(astronomyConfig.isPresent(), "Astronomy API endpoint config should be present");
        assertEquals("https://api.astronomyapi.com", astronomyConfig.get().getBaseUrl());
    }
}
