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


    private static final String DB_URL = "jdbc:mysql://localhost:3306/explorandoelcosmos";
    private static final String DB_USER = "topicos_demo";
    private static final String DB_PASSWORD = "newPassword";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        // Configuraciones optimizadas para MySQL
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10); // MySQL soporta más conexiones simultáneas que SQLite
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000); // 5 minutos
        config.setConnectionTimeout(20000); // 20 segundos

        // Propiedades de caché para mejorar rendimiento en MySQL
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        try {
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error fatal al inicializar la base de datos MySQL.");
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {


            InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("schema.sql");
            String schemaSql = "";
            if (inputStream != null) {
                schemaSql = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            } else {
                System.err.println("Advertencia: No se pudo encontrar schema.sql.");
                return;
            }

            String[] statements = schemaSql.split(";");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    try {
                        stmt.execute(statement);
                    } catch (SQLException ex) {
                        System.out.println("Nota SQL: " + ex.getMessage());
                    }
                }
            }
            System.out.println("Esquema de MySQL verificado/inicializado.");

        } catch (SQLException e) {
            System.err.println("Error al inicializar el esquema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}