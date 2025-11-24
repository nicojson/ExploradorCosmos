package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.controllers.MainProgramController;
import com.example.explorandoelcosmos.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationService {

    /**
     * Método centralizado y robusto para cambiar de escena.
     * 
     * @param event    El evento de acción (de un botón) que disparó la navegación.
     *                 Se usa para obtener el Stage.
     * @param fxmlPath La ruta al nuevo archivo FXML.
     * @param title    El nuevo título de la ventana.
     * @param userData El usuario para la sesión (puede ser null para invitados o
     *                 vistas no autenticadas).
     */
    public static void navigateTo(ActionEvent event, String fxmlPath, String title, User userData) {
        navigateTo((Node) event.getSource(), fxmlPath, title, userData);
    }

    /**
     * Sobrecarga para aceptar un Node directamente (útil cuando no hay ActionEvent,
     * e.g., Platform.runLater)
     */
    public static void navigateTo(Node sourceNode, String fxmlPath, String title, User userData) {
        try {
            Stage stage = (Stage) sourceNode.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Si la nueva vista es la principal, configurar la sesión
            if (fxmlPath.contains("mainProgram-view.fxml")) {
                MainProgramController controller = loader.getController();
                controller.setupSession(userData);
            }

            stage.setScene(new Scene(root));
            stage.setTitle(title);

        } catch (IOException e) {
            e.printStackTrace();
            NotificationManager.showError("Error de Navegación", "No se pudo cargar la vista: " + title);
        }
    }
}
