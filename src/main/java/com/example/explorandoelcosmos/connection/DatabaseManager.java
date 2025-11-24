package com.example.explorandoelcosmos.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:explorador_del_cosmos.db";
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);

        // Configuraciones específicas para SQLite
        config.setAutoCommit(true); // CRÍTICO: Habilitar autocommit para SQLite
        config.setConnectionTestQuery("SELECT 1");
        config.setMaximumPoolSize(1); // SQLite funciona mejor con pool size = 1
        config.setMinimumIdle(1);
        config.setIdleTimeout(600000); // 10 minutos
        config.setConnectionTimeout(30000); // 30 segundos

        // Propiedades adicionales para SQLite
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("synchronous", "NORMAL");

        dataSource = new HikariDataSource(config);

        try {
            // Crear la base de datos y el esquema si no existen
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error fatal al inicializar la base de datos.");
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        // Usamos try-with-resources para asegurar que la conexión se cierre y devuelva
        // al pool
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Leer el archivo schema.sql desde los recursos
            InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("schema.sql");
            String schemaSql = "";
            if (inputStream != null) {
                schemaSql = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            } else {
                System.err.println(
                        "Advertencia: No se pudo encontrar el archivo schema.sql en los recursos. Se intentará crear solo la tabla api_endpoints.");
            }

            // Separar y ejecutar sentencias del schema.sql
            for (String statement : schemaSql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }

            // La tabla api_endpoints es redundante con Api_Sources definida en schema.sql.
            // Se ha eliminado su creación manual para evitar conflictos.

            // Asegurar que los roles existen
            stmt.execute(
                    "INSERT OR IGNORE INTO Roles (role_id, role_name) VALUES (1, 'admin'), (2, 'user'), (3, 'guest')");

            // Asegurar que las fuentes de API existen
            stmt.execute(
                    "INSERT OR IGNORE INTO Api_Sources (source_id, source_name, base_url) VALUES (1, 'SpaceX', 'https://api.spacexdata.com/v4'), (2, 'JWST', 'https://api.jwstapi.com'), (3, 'NASA', 'https://images-api.nasa.gov'), (4, 'Planet', 'https://api.planet.com'), (5, 'SolarSystem', 'https://api.le-systeme-solaire.net/rest')");

            System.out.println("Base de datos inicializada o ya existente, incluyendo la tabla api_endpoints.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar el esquema de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado.");
        }
    }
}
