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

public class registerController {
    @FXML private TextField newUserFld;
    @FXML private PasswordField newPasswordFld;
    @FXML private PasswordField confirmPasswordFld;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = newUserFld.getText();
        String password = newPasswordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            NotificationManager.showError("Error de Registro", "Usuario y contraseña no pueden estar vacíos.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            NotificationManager.showError("Error de Registro", "Las contraseñas no coinciden.");
            return;
        }
        if (userDAO.findByUsername(username).isPresent()) {
            NotificationManager.showError("Error de Registro", "El nombre de usuario ya existe.");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword, username + "@example.com", "user");
        userDAO.save(newUser);

        NotificationManager.showSuccess("Registro Exitoso", "Usuario '" + username + "' registrado. Ahora puedes iniciar sesión.");
        goToLogin(event);
    }

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        goToLogin(event);
    }

    private void goToLogin(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/hello-view.fxml", "Acceso al Observatorio", null);
    }
}
