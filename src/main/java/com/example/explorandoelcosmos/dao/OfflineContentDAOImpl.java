package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.connection.DatabaseManager;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.service.AuthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfflineContentDAOImpl implements OfflineContentDAO {

    @Override
    public void save(Publication publication) {
        // Guarda la información del contenido descargado en la tabla Offline_Content
        String sql = "INSERT INTO Offline_Content (user_id, publication_id, local_path) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, AuthService.getCurrentUser().getId());
            pstmt.setInt(2, publication.getId());
            // Asumimos que la publicación tiene la ruta local donde se guardó el contenido
            pstmt.setString(3, publication.getLocalPath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Publication> findAll() {
        List<Publication> publications = new ArrayList<>();
        // La consulta ahora une Publications con Offline_Content para obtener las publicaciones descargadas por el usuario
        String sql = "SELECT p.*, oc.local_path AS offline_local_path FROM Publications p JOIN Offline_Content oc ON p.publication_id = oc.publication_id WHERE oc.user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, AuthService.getCurrentUser().getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Publication pub = new Publication();
                pub.setId(rs.getInt("publication_id"));
                pub.setTitle(rs.getString("title"));
                pub.setDescription(rs.getString("description"));
                pub.setMainImageUrl(rs.getString("main_image_url"));

                Timestamp ts = rs.getTimestamp("published_date");
                if (ts != null) {
                    pub.setPublishedDate(ts.toLocalDateTime());
                }

                pub.setSourceApiId(rs.getInt("source_api_id"));
                pub.setFavorite(rs.getBoolean("is_favorite"));
                // Se establece la ruta local desde la tabla Offline_Content
                pub.setLocalPath(rs.getString("offline_local_path"));
                publications.add(pub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    @Override
    public void delete(int publicationId) {
        // Elimina el registro de contenido descargado de la tabla Offline_Content
        String sql = "DELETE FROM Offline_Content WHERE publication_id = ? AND user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, publicationId);
            pstmt.setInt(2, AuthService.getCurrentUser().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
