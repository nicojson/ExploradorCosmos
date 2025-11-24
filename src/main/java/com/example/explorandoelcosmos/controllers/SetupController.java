package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.service.AuthService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class SetupController {

    @FXML
    private TextField adminUsernameField;
    @FXML
    private TextField adminEmailField;
    @FXML
    private DatePicker adminDobField;
    @FXML
    private TextField adminCountryField;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button createAdminButton;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleCreateAdmin() {
        String username = adminUsernameField.getText();
        String email = adminEmailField.getText();
        LocalDate dob = adminDobField.getValue();
        String country = adminCountryField.getText();
        String password = adminPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || dob == null || country.isEmpty()) {
            NotificationManager.showError("Campos Incompletos", "Por favor, rellena todos los campos.");
            return;
        }
        if (!password.equals(confirm)) {
            NotificationManager.showError("Error de Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        createAdminButton.setDisable(true); // Disable button

        Task<Boolean> registrationTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return authService.registerUser(username, password, email, dob, country, "admin");
            }
        };

        registrationTask.setOnSucceeded(e -> {
            createAdminButton.setDisable(false);
            if (registrationTask.getValue()) {
                NotificationManager.showSuccess("Administrador Creado",
                        "La cuenta de administrador ha sido creada. Por favor, inicia sesión.");

                // Redirigir a la pantalla de login
                try {
                    Parent loginRoot = FXMLLoader
                            .load(getClass().getResource("/com/example/explorandoelcosmos/hello-view.fxml"));
                    Stage stage = (Stage) createAdminButton.getScene().getWindow();
                    stage.setScene(new Scene(loginRoot));
                    stage.setTitle("Acceso al Observatorio");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                NotificationManager.showError("Error",
                        "No se pudo crear el administrador. El usuario podría ya existir.");
            }
        });

        registrationTask.setOnFailed(e -> {
            createAdminButton.setDisable(false);
            Throwable ex = registrationTask.getException();
            ex.printStackTrace();
            NotificationManager.showError("Error de Sistema", "Ocurrió un error al registrar: " + ex.getMessage());
        });

        new Thread(registrationTask).start();
    }
}
