package com.example.explorandoelcosmos.model;

public class User {
    private int id;
    private String username;
    private String hashedPassword;
    private String email;
    private String role;

    // Constructor para crear un nuevo usuario (el ID es autogenerado)
    public User(String username, String hashedPassword, String email, String role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.role = role;
    }

    // Constructor para leer un usuario de la BD (incluye el ID)
    public User(int id, String username, String hashedPassword, String email, String role) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}
