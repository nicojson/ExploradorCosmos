package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.AuthService;
import com.example.explorandoelcosmos.service.NavigationService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField userFld;
    @FXML
    private PasswordField passwordFld;
    @FXML
    private Button loginBtn;

    private final AuthService authService = new AuthService();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = userFld.getText();
        String password = passwordFld.getText();

        if (username.isEmpty() || password.isEmpty()) {
            NotificationManager.showError("Error de inicio de sesión", "Usuario y contraseña no pueden estar vacíos.");
            return;
        }

        loginBtn.setDisable(true); // Disable button to prevent multiple clicks

        Task<Boolean> loginTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return authService.login(username, password);
            }
        };

        loginTask.setOnSucceeded(e -> {
            loginBtn.setDisable(false);
            if (loginTask.getValue()) {
                User user = AuthService.getCurrentUser();
                if (user.isAdmin()) {
                    NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/admin-view.fxml",
                            "Panel de Administración", null);
                } else {
                    NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml",
                            "Explorando el Cosmos", user);
                }
            } else {
                NotificationManager.showError("Error de inicio de sesión", "Usuario o contraseña incorrectos.");
            }
        });

        loginTask.setOnFailed(e -> {
            loginBtn.setDisable(false);
            Throwable ex = loginTask.getException();
            ex.printStackTrace();
            NotificationManager.showError("Error de Sistema",
                    "Ocurrió un error al intentar iniciar sesión: " + ex.getMessage());
        });

        new Thread(loginTask).start();
    }

    @FXML
    protected void onGuestModeClick(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml",
                "Explorando el Cosmos (Invitado)", null);
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/register-view.fxml",
                "Registro de Nuevo Operador", null);
    }
}
