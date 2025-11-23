package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.PasswordUtils;
import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SetupController {

    @FXML private TextField adminUsernameField;
    @FXML private TextField adminEmailField;
    @FXML private PasswordField adminPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button createAdminButton;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    private void handleCreateAdmin() {
        String username = adminUsernameField.getText();
        String email = adminEmailField.getText();
        String password = adminPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            NotificationManager.showError("Campos Incompletos", "Por favor, rellena todos los campos.");
            return;
        }
        if (!password.equals(confirm)) {
            NotificationManager.showError("Error de Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User adminUser = new User(username, hashedPassword, email, "admin");
        userDAO.save(adminUser);

        NotificationManager.showSuccess("Administrador Creado", "La cuenta de administrador ha sido creada. Por favor, inicia sesión.");
        
        // Redirigir a la pantalla de login
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/explorandoelcosmos/hello-view.fxml"));
            Stage stage = (Stage) createAdminButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Acceso al Observatorio");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
