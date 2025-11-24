package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.connection.DatabaseManager;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppConfigDAOImpl implements AppConfigDAO {

    @Override
    public Optional<String> findByKey(String key) {
        String sql = "SELECT config_value FROM App_Config WHERE config_key = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("config_value"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Map<String, String> findAllApiKeys() {
        Map<String, String> apiKeys = new HashMap<>();
        String sql = "SELECT source_name, api_key FROM Api_Sources WHERE api_key IS NOT NULL";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                apiKeys.put(rs.getString("source_name"), rs.getString("api_key"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apiKeys;
    }

    @Override
    public void save(String key, String value) {
        // "UPSERT" logic: UPDATE if key exists, INSERT otherwise.
        String sql = "INSERT INTO App_Config (config_key, config_value) VALUES (?, ?) " +
                "ON CONFLICT(config_key) DO UPDATE SET config_value = excluded.config_value";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === Implementación de los nuevos métodos para ApiEndpointConfig ===

    @Override
    public void saveEndpointConfig(ApiEndpointConfig config) {
        String sql = "INSERT INTO Api_Sources (source_name, api_key, base_url) VALUES (?, ?, ?) " +
                "ON CONFLICT(source_name) DO UPDATE SET api_key = excluded.api_key, base_url = excluded.base_url";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, config.getEndpointName());
            pstmt.setString(2, config.getApiKey());
            pstmt.setString(3, config.getBaseUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<ApiEndpointConfig> findEndpointConfigByName(String endpointName) {
        String sql = "SELECT source_name, api_key, base_url FROM Api_Sources WHERE source_name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, endpointName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new ApiEndpointConfig(
                            rs.getString("source_name"),
                            rs.getString("api_key"),
                            rs.getString("base_url")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<ApiEndpointConfig> findAllEndpointConfigs() {
        List<ApiEndpointConfig> configs = new ArrayList<>();
        String sql = "SELECT source_name, api_key, base_url FROM Api_Sources ORDER BY source_name";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                configs.add(new ApiEndpointConfig(
                        rs.getString("source_name"),
                        rs.getString("api_key"),
                        rs.getString("base_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return configs;
    }

    @Override
    public void deleteEndpointConfig(String endpointName) {
        String sql = "DELETE FROM Api_Sources WHERE source_name = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, endpointName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
