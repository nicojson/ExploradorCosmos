package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(int id);
    boolean adminExists(); // Nuevo método
}
