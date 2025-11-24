package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    @Test
    public void testUserPersistence() {
        UserDAO userDAO = new UserDAOImpl();
        String username = "test_user_" + System.currentTimeMillis();
        User user = new User(username, "hashed_password", "test@example.com", LocalDate.now(), "TestCountry", "user");

        System.out.println("Testing registration for: " + username);
        boolean saved = userDAO.save(user);
        assertTrue(saved, "User should be saved successfully");

        Optional<User> retrieved = userDAO.findByUsername(username);
        assertTrue(retrieved.isPresent(), "User should be found in database");
        assertEquals(username, retrieved.get().getUsername(), "Username should match");

        // Cleanup
        if (retrieved.isPresent()) {
            userDAO.delete(retrieved.get().getId());
            System.out.println("Test user deleted.");
        }
    }
}
