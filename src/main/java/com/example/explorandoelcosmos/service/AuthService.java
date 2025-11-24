package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import com.example.explorandoelcosmos.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO;
    private static User currentUser;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    public boolean registerUser(String username, String password, String email, LocalDate dob, String country,
            String role) {
        System.out.println("=== DEBUG REGISTRATION ===");
        System.out.println("Registering user: " + username);
        System.out.println("Password to hash: [" + password + "]");
        System.out.println("Password length: " + password.length());

        if (userDAO.findByUsername(username).isPresent()) {
            System.out.println("User already exists!");
            return false; // Usuario ya existe
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Generated hash: " + hashedPassword);
        System.out.println("Hash length: " + hashedPassword.length());

        User newUser = new User(username, hashedPassword, email, dob, country, role);
        boolean saved = userDAO.save(newUser);
        if (saved) {
            System.out.println("User saved successfully");
            return true;
        } else {
            System.err.println("Failed to save user to database");
            return false;
        }
    }

    public boolean login(String username, String password) {
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Attempting login for username: " + username);

        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found in database");
            System.out.println("Stored hash length: " + user.getHashedPassword().length());
            System.out.println("Password to check: [" + password + "]");
            System.out.println("Password length: " + password.length());

            boolean passwordMatches = BCrypt.checkpw(password, user.getHashedPassword());
            System.out.println("Password matches: " + passwordMatches);

            if (passwordMatches) {
                currentUser = user;
                return true;
            }
        } else {
            System.out.println("User NOT found in database");
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public boolean isAdminExists() {
        return userDAO.adminExists();
    }
}
