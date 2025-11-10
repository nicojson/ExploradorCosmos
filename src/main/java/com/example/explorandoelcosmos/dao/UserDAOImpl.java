package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.connection.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public void save(User user) {
        // La consulta SQL para insertar un nuevo usuario.
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        // Usamos un try-with-resources para asegurar que la conexión y el statement se cierren solos.
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignamos los valores a los placeholders (?) de la consulta.
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getHashedPassword());

            // Ejecutamos la inserción.
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Aquí podrías manejar la excepción de forma más específica,
            // por ejemplo, si el nombre de usuario ya existe (violación de clave primaria).
            System.err.println("Error al guardar el usuario: " + e.getMessage());
            // Podrías lanzar una excepción personalizada si lo prefieres.
            // throw new DataAccessException("Error al guardar usuario", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // La consulta SQL para buscar un usuario por su nombre.
        String sql = "SELECT * FROM user WHERE username = ?";
        User user = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            // Ejecutamos la consulta y obtenemos el resultado.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Si hay un resultado, creamos el objeto User.
                if (rs.next()) {
                    String foundUsername = rs.getString("username");
                    String hashedPassword = rs.getString("password");
                    user = new User(foundUsername, hashedPassword);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el usuario: " + e.getMessage());
        }

        // Devolvemos un Optional. Si el usuario no se encontró, será un Optional.empty().
        return Optional.ofNullable(user);
    }
}
