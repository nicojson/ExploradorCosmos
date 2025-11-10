package com.example.explorandoelcosmos.dao;

import com.example.explorandoelcosmos.model.User;

import java.util.Optional;

public interface UserDAO {
    /**
     * Guarda un nuevo usuario en la base de datos.
     * La contraseña ya debe estar hasheada.
     * @param user El objeto User a guardar.
     */
    void save(User user);

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return Un Optional que contiene el User si se encuentra, o un Optional vacío si no.
     */
    Optional<User> findByUsername(String username);
}
