package com.example.explorandoelcosmos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

public class DetailedCardController {

    @FXML private Pane imagePane;
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private HBox actions; // El contenedor del include
    @FXML private ActionsController actionsController; // El controlador inyectado

    private MainProgramController mainController;

    public void setMainController(MainProgramController mainController) {
        this.mainController = mainController;
    }

    public void setData(String imageUrl, String title, String details) {
        String style = String.format("-fx-background-image: url('%s');", imageUrl);
        imagePane.setStyle(style);
        titleLabel.setText(title);
        detailsLabel.setText(details);

        // Pasar los datos al controlador de acciones
        actionsController.setData(title, imageUrl);
    }

    @FXML
    private void handleCloseClick() {
        if (mainController != null) {
            mainController.hideDetailedView();
        }
    }
}
