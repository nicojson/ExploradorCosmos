package com.example.explorandoelcosmos;

import com.example.explorandoelcosmos.connection.DatabaseManager;
import java.sql.*;

public class QueryUsers {
    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Users")) {

            System.out.println("\n=== USERS TABLE ===");
            System.out.println("user_id | username | email | dob | country | role_id | password_hash");
            System.out.println("-------------------------------------------------------------------");

            int count = 0;
            while (rs.next()) {
                count++;
                String hash = rs.getString("hashed_password");
                String hashPreview = (hash != null && hash.length() > 20) ? hash.substring(0, 20) + "..." : hash;

                System.out.printf("%d | %s | %s | %s | %s | %d | %s%n",
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("dob"),
                        rs.getString("country"),
                        rs.getInt("role_id"),
                        hashPreview);
            }

            System.out.println("-------------------------------------------------------------------");
            System.out.println("Total users: " + count);
            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
