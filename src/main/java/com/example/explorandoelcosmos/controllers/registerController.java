package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.service.AuthService;
import com.example.explorandoelcosmos.service.NavigationService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class registerController {
    @FXML
    private TextField newUserFld;
    @FXML
    private TextField emailFld;
    @FXML
    private DatePicker dobFld;
    @FXML
    private TextField countryFld;
    @FXML
    private PasswordField newPasswordFld;
    @FXML
    private PasswordField confirmPasswordFld;

    private final AuthService authService = new AuthService();

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = newUserFld.getText();
        String email = emailFld.getText();
        LocalDate dob = dobFld.getValue();
        String country = countryFld.getText();
        String password = newPasswordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || dob == null || country.isEmpty()) {
            NotificationManager.showError("Error de Registro", "Todos los campos son obligatorios.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            NotificationManager.showError("Error de Registro", "Las contraseñas no coinciden.");
            return;
        }

        boolean success = authService.registerUser(username, password, email, dob, country, "user");

        if (success) {
            NotificationManager.showSuccess("Registro Exitoso",
                    "Usuario '" + username + "' registrado. Ahora puedes iniciar sesión.");
            goToLogin(event);
        } else {
            NotificationManager.showError("Error de Registro", "El nombre de usuario ya existe.");
        }
    }

    @FXML
    protected void onCancelButtonClick(ActionEvent event) {
        goToLogin(event);
    }

    private void goToLogin(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/hello-view.fxml", "Acceso al Observatorio",
                null);
    }
}
