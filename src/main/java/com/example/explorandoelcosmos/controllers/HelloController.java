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
import java.util.Optional;

public class HelloController {
    @FXML
    private TextField userFld;
    @FXML
    private PasswordField passwordFld;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    protected void onLoginButtonClick() {
        String username = userFld.getText();
        String password = passwordFld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de inicio de sesión", "Usuario y contraseña no pueden estar vacíos.");
            return;
        }

        // 1. Buscar al usuario en la base de datos
        Optional<User> userOptional = userDAO.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 2. Verificar la contraseña
            if (PasswordUtils.checkPassword(password, user.getHashedPassword())) {
                loadMainProgramView();
            } else {
                // Contraseña incorrecta
                showAlert(Alert.AlertType.ERROR, "Error de inicio de sesión", "Usuario o contraseña incorrectos.");
            }
        } else {
            // Usuario no encontrado
            showAlert(Alert.AlertType.ERROR, "Error de inicio de sesión", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    protected void onRegisterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Parent registerRoot = loader.load();
            Scene scene = registerBtn.getScene();
            scene.setRoot(registerRoot);
            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Registro");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCancelButtonClick() {
        // Cierra la aplicación o limpia los campos, según prefieras.
        // Por ejemplo, para limpiar:
        userFld.clear();
        passwordFld.clear();
    }

    private void loadMainProgramView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainProgram-view.fxml"));
            Parent mainRoot = loader.load();
            Scene scene = loginBtn.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(mainRoot));
            stage.setTitle("Explorando el Cosmos - Programa Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error crítico", "No se pudo cargar la ventana principal.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
