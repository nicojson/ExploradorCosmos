package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.AuthService;
import com.example.explorandoelcosmos.service.NavigationService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class EditProfileController {

    @FXML
    private TextField usernameFld;
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

    private User currentUser;
    private final AuthService authService = new AuthService();

    public void setup(User user) {
        this.currentUser = user;
        if (user != null) {
            usernameFld.setText(user.getUsername());
            emailFld.setText(user.getEmail());
            dobFld.setValue(user.getDob());
            countryFld.setText(user.getCountry());
            usernameFld.setDisable(true); // Username cannot be changed
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String email = emailFld.getText();
        LocalDate dob = dobFld.getValue();
        String country = countryFld.getText();
        String password = newPasswordFld.getText();
        String confirm = confirmPasswordFld.getText();

        if (email.isEmpty() || dob == null || country.isEmpty()) {
            NotificationManager.showError("Campos Incompletos", "Por favor, rellena los campos obligatorios.");
            return;
        }

        if (!password.isEmpty() && !password.equals(confirm)) {
            NotificationManager.showError("Error de Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        // Update user object
        currentUser.setEmail(email);
        currentUser.setDob(dob);
        currentUser.setCountry(country);
        if (!password.isEmpty()) {
            currentUser.setHashedPassword(password); // In real app, hash this!
        }

        // In a real app, we would call a service to update the DB here
        // authService.updateUser(currentUser);

        NotificationManager.showSuccess("Perfil Actualizado", "Los cambios han sido guardados.");
        goBack(event);
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        goBack(event);
    }

    private void goBack(ActionEvent event) {
        NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml",
                "Explorador del Cosmos", currentUser);
    }
}
