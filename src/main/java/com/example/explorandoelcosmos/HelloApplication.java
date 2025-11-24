package com.example.explorandoelcosmos;

import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserDAO userDAO = new UserDAOImpl();

        // Cargar configuraciones de API al inicio
        com.example.explorandoelcosmos.service.ConfigLoader.loadApiEndpoints();

        // DEBUG: Mostrar todos los usuarios en la base de datos
        System.out.println("\n========== DATABASE DEBUG ==========");
        System.out.println("Listing all users in database:");
        java.util.List<com.example.explorandoelcosmos.model.User> allUsers = userDAO.findAll();
        if (allUsers.isEmpty()) {
            System.out.println("  (No users found in database)");
        } else {
            for (com.example.explorandoelcosmos.model.User u : allUsers) {
                System.out.printf("  - User ID: %d, Username: %s, Email: %s, Role: %s%n",
                        u.getId(), u.getUsername(), u.getEmail(), u.getRole());
            }
        }
        System.out.println("====================================\n");

        String initialView;
        String initialTitle;

        if (!userDAO.adminExists()) {
            initialView = "setup-view.fxml";
            initialTitle = "Configuración Inicial";
        } else {
            initialView = "hello-view.fxml";
            initialTitle = "Acceso al Observatorio";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(initialView));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle(initialTitle);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
