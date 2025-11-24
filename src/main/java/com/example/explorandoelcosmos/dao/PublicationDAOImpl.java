package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.connection.DatabaseManager;
import com.example.explorandoelcosmos.model.Publication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublicationDAOImpl implements PublicationDAO {

    @Override
    public void save(Publication publication) {
        String sql = "INSERT INTO Publications (source_api_id, original_id_from_api, content_type, title, description, main_image_url, published_date, fetched_at, is_favorite, local_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, publication.getSourceApiId());
            pstmt.setString(2, publication.getOriginalIdFromApi());
            pstmt.setString(3, publication.getContentType());
            pstmt.setString(4, publication.getTitle());
            pstmt.setString(5, publication.getDescription());
            pstmt.setString(6, publication.getMainImageUrl());
            pstmt.setObject(7, publication.getPublishedDate());
            pstmt.setObject(8, publication.getFetchedAt());
            pstmt.setInt(9, publication.isFavorite() ? 1 : 0);
            pstmt.setString(10, publication.getLocalPath());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publication.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Publication publication) {
        String sql = "UPDATE Publications SET source_api_id = ?, original_id_from_api = ?, content_type = ?, title = ?, description = ?, main_image_url = ?, published_date = ?, fetched_at = ?, is_favorite = ?, local_path = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, publication.getSourceApiId());
            pstmt.setString(2, publication.getOriginalIdFromApi());
            pstmt.setString(3, publication.getContentType());
            pstmt.setString(4, publication.getTitle());
            pstmt.setString(5, publication.getDescription());
            pstmt.setString(6, publication.getMainImageUrl());
            pstmt.setObject(7, publication.getPublishedDate());
            pstmt.setObject(8, publication.getFetchedAt());
            pstmt.setInt(9, publication.isFavorite() ? 1 : 0);
            pstmt.setString(10, publication.getLocalPath());
            pstmt.setInt(11, publication.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Publications WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Publication> findById(int id) {
        String sql = "SELECT * FROM Publications WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPublication(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Publication> findAll() {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM Publications";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                publications.add(mapResultSetToPublication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    @Override
    public List<Publication> findFavorites() {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM Publications WHERE is_favorite = 1";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                publications.add(mapResultSetToPublication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    @Override
    public Optional<Publication> findByOriginalId(String originalId, int sourceApiId) {
        String sql = "SELECT * FROM Publications WHERE original_id_from_api = ? AND source_api_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, originalId);
            pstmt.setInt(2, sourceApiId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPublication(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Publication mapResultSetToPublication(ResultSet rs) throws SQLException {
        Publication publication = new Publication();
        publication.setId(rs.getInt("id"));
        publication.setSourceApiId(rs.getInt("source_api_id"));
        publication.setOriginalIdFromApi(rs.getString("original_id_from_api"));
        publication.setContentType(rs.getString("content_type"));
        publication.setTitle(rs.getString("title"));
        publication.setDescription(rs.getString("description"));
        publication.setMainImageUrl(rs.getString("main_image_url"));

        // Handle potential null dates or different types
        Object pubDateObj = rs.getObject("published_date");
        if (pubDateObj instanceof String) {
            publication.setPublishedDate(LocalDateTime.parse((String) pubDateObj));
        } else if (pubDateObj instanceof Timestamp) {
            publication.setPublishedDate(((Timestamp) pubDateObj).toLocalDateTime());
        }

        Object fetchedAtObj = rs.getObject("fetched_at");
        if (fetchedAtObj instanceof String) {
            publication.setFetchedAt(LocalDateTime.parse((String) fetchedAtObj));
        } else if (fetchedAtObj instanceof Timestamp) {
            publication.setFetchedAt(((Timestamp) fetchedAtObj).toLocalDateTime());
        }

        publication.setFavorite(rs.getInt("is_favorite") == 1);
        publication.setLocalPath(rs.getString("local_path"));
        return publication;
    }
}
