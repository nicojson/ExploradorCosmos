package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.connection.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean adminExists() {
        String sql = "SELECT 1 FROM Users WHERE role_id = (SELECT role_id FROM Roles WHERE role_name = 'admin') LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            return rs.next(); // Devuelve true si encuentra al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Asumir que no existe si hay un error
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT u.*, r.role_name FROM Users u JOIN Roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT u.*, r.role_name FROM Users u JOIN Roles r ON u.role_id = r.role_id WHERE u.username = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name FROM Users u JOIN Roles r ON u.role_id = r.role_id";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean save(User user) {
        System.out.println("=== DEBUG UserDAO.save() ===");
        System.out.println("Attempting to save user: " + user.getUsername());

        String sql = "INSERT INTO Users (username, hashed_password, email, dob, country, role_id) VALUES (?, ?, ?, ?, ?, ?)";

        // Usamos try-with-resources para asegurar el cierre de la conexión
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("Connection autocommit: " + conn.getAutoCommit());

            // Pasamos la conexión existente para evitar deadlock
            int roleId = getRoleId(conn, user.getRole());
            System.out.println("Resolved role_id: " + roleId);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getHashedPassword());
                pstmt.setString(3, user.getEmail());
                pstmt.setObject(4, user.getDob());
                pstmt.setString(5, user.getCountry());
                pstmt.setInt(6, roleId);

                System.out.println("Executing SQL: " + sql);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected > 0) {
                    System.out.println("✓ User saved successfully to database");
                    return true;
                } else {
                    System.err.println("✗ WARNING: No rows were inserted!");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ ERROR saving user to database:");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE Users SET username = ?, hashed_password = ?, email = ?, dob = ?, country = ?, role_id = ? WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getHashedPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setObject(4, user.getDob());
            pstmt.setString(5, user.getCountry());
            // Pasamos la conexión existente
            pstmt.setInt(6, getRoleId(conn, user.getRole()));
            pstmt.setInt(7, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        LocalDate dob = null;
        String dobStr = rs.getString("dob");
        if (dobStr != null) {
            dob = LocalDate.parse(dobStr);
        }

        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("hashed_password"),
                rs.getString("email"),
                dob,
                rs.getString("country"),
                rs.getString("role_name"));
    }

    // Modificado para aceptar una conexión existente
    private int getRoleId(Connection conn, String roleName) throws SQLException {
        String sql = "SELECT role_id FROM Roles WHERE role_name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roleName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("role_id");
                }
            }
        }

        // Fallback: Si es 'admin' y no se encuentra, asumir ID 1
        if ("admin".equalsIgnoreCase(roleName)) {
            System.err.println("Advertencia: Rol 'admin' no encontrado en DB. Usando ID 1 por defecto.");
            return 1;
        }

        return 2; // Devuelve 'user' por defecto (ID 2)
    }
}
