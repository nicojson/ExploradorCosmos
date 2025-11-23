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
        // Configuraciones recomendadas para SQLite con HikariCP
        config.setConnectionTestQuery("SELECT 1");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(600000); // 10 minutos
        config.setConnectionTimeout(30000); // 30 segundos

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
        // Usamos try-with-resources para asegurar que la conexión se cierre y devuelva al pool
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Leer el archivo schema.sql desde los recursos
            InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("schema.sql");
            String schemaSql = "";
            if (inputStream != null) {
                schemaSql = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            } else {
                System.err.println("Advertencia: No se pudo encontrar el archivo schema.sql en los recursos. Se intentará crear solo la tabla api_endpoints.");
            }

            // Separar y ejecutar sentencias del schema.sql
            for (String statement : schemaSql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }

            // Crear la tabla api_endpoints si no existe
            String createApiEndpointsTableSql = """
                CREATE TABLE IF NOT EXISTS api_endpoints (
                    endpoint_name TEXT PRIMARY KEY NOT NULL,
                    api_key TEXT,
                    app_id TEXT,
                    app_secret TEXT
                );
                """;
            stmt.execute(createApiEndpointsTableSql);
            
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
