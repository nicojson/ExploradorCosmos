package com.example.explorandoelcosmos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

public class DetailedCardController {

    @FXML private Pane imagePane;
    @FXML private WebView videoWebView;
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private HBox actions;
    @FXML private ActionsController actionsController;

    private MainProgramController mainController;

    public void setMainController(MainProgramController mainController) {
        this.mainController = mainController;
    }

    public void setData(String imageUrl, String title, String details, String videoUrl) {
        titleLabel.setText(title);
        detailsLabel.setText(details);

        if (actionsController != null) {
            actionsController.setData(title, imageUrl);
        }

        if (videoUrl != null && !videoUrl.isEmpty()) {
            // MODO VIDEO
            imagePane.setVisible(false);
            videoWebView.setVisible(true);


            if (videoUrl.contains(".mp4") || videoUrl.contains("images-assets.nasa.gov")) {

                String htmlContent = "<!DOCTYPE html>" +
                        "<html style='height:100%; margin:0; padding:0; background:black;'>" +
                        "<body style='height:100%; margin:0; padding:0; display:flex; align-items:center; justify-content:center;'>" +
                        "<video width='100%' height='100%' controls autoplay name='media'>" +
                        "<source src='" + videoUrl + "' type='video/mp4'>" +
                        "Tu navegador no soporta la etiqueta de video." +
                        "</video>" +
                        "</body>" +
                        "</html>";
                videoWebView.getEngine().loadContent(htmlContent);
            } else {
                // Es un video de YouTube o enlace web (SpaceX)
                videoWebView.getEngine().load(videoUrl);
            }

        } else {
            // MODO IMAGEN
            videoWebView.setVisible(false);
            videoWebView.getEngine().load(null); // Detener cualquier video previo

            imagePane.setVisible(true);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Reemplazar espacios por %20 para evitar errores de URL en CSS
                String cleanUrl = imageUrl.replace(" ", "%20");
                String style = String.format("-fx-background-image: url('%s');", cleanUrl);
                imagePane.setStyle(style);
            } else {
                imagePane.setStyle("-fx-background-color: #2b2b2b;");
            }
        }
    }

    @FXML
    private void handleCloseClick() {
        if (videoWebView != null) {
            videoWebView.getEngine().loadContent("");
        }
        if (mainController != null) {
            mainController.hideDetailedView();
        }
    }
}