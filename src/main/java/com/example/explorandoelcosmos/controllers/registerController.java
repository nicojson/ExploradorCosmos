package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.PasswordUtils;
import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import com.example.explorandoelcosmos.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class registerController {
    @FXML
    private TextField newUserFld;
    @FXML
    private PasswordField newPasswordFld;
    @FXML
    private PasswordField confirmPasswordFld;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button registerBtn;

    @FXML
    protected void onCancelButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/explorandoelcosmos/hello-view.fxml"));
            Parent loginRoot = loader.load();
            Scene scene = cancelBtn.getScene();
            scene.setRoot(loginRoot);
            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRegisterButtonClick() {
        String username = newUserFld.getText();
        String password = newPasswordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de registro", "Por favor, rellene todos los campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error de registro", "Las contraseñas no coinciden.");
            return;
        }

        UserDAO userDAO = new UserDAOImpl();
        if (userDAO.findByUsername(username).isPresent()) {
            showAlert(Alert.AlertType.ERROR, "Error de registro", "El nombre de usuario ya existe.");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword);
        userDAO.save(newUser);

        showAlert(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente.");

        // Volver a la vista de login
        onCancelButtonClick();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}
