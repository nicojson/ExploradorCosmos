package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.PasswordUtils;
import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.NavigationService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class HelloController {
    @FXML private TextField userFld;
    @FXML private PasswordField passwordFld;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = userFld.getText();
        String password = passwordFld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            NotificationManager.showError("Error de inicio de sesión", "Usuario y contraseña no pueden estar vacíos.");
            return;
        }

        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isPresent() && PasswordUtils.checkPassword(password, userOptional.get().getHashedPassword())) {
            User user = userOptional.get();
            if (user.isAdmin()) {
                NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/admin-view.fxml", "Panel de Administración", null);
            } else {
                NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml", "Explorando el Cosmos", user);
            }
        } else {
            NotificationManager.showError("Error de inicio de sesión", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    protected void onGuestModeClick(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml", "Explorando el Cosmos (Invitado)", null);
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/register-view.fxml", "Registro de Nuevo Operador", null);
    }
}
