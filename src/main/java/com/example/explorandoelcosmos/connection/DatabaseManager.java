package com.example.explorandoelcosmos.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/explorandoelcosmos";
    private static final String DATABASE_USER = "ismael";
    private static final String DATABASE_PASSWORD = "Ismaelo22#";

    private static Connection connection = null;

    /**
      Obtiene una conexión a la base de datos.
      Si ya existe una conexión, la reutiliza. Si no, crea una nueva.

      @return Un objeto Connection para interactuar con la base de datos.
      @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Opcional: Cargar el driver explícitamente (ya no es tan necesario en JDBC 4.0+)
                // Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
                // Relanzar la excepción para que la capa superior la maneje
                throw e;
            }
        }
        return connection;
    }

    /**
      Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
