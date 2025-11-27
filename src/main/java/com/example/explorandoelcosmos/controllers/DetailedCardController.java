package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;

import java.io.File;

public class DetailedCardController {

    @FXML private Pane imagePane;
    @FXML private WebView videoWebView;
    @FXML public MediaView mediaView; // ¡Asegúrate de que este fx:id exista en tu FXML!
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
    @FXML private HBox actions;
    @FXML private ActionsController actionsController;

    private MainProgramController mainController;
    private MediaPlayer mediaPlayer;

    public void setMainController(MainProgramController mainController) {
        this.mainController = mainController;
    }

    public void setData(Publication publication) {
        titleLabel.setText(publication.getTitle());
        detailsLabel.setText(publication.getDescription());

        if (actionsController != null) {
            actionsController.setData(publication, mainController);
        }

        boolean isVideo = "video".equals(publication.getContentType());
        String localPath = publication.getLocalPath();
        String videoUrl = publication.getContentUrl();

        // Resetear vistas
        imagePane.setVisible(false);
        videoWebView.setVisible(false);
        if (mediaView != null) { // Comprobación de nulidad
            mediaView.setVisible(false);
        }
        stopPlayback();

        if (isVideo && localPath != null && !localPath.isEmpty() && mediaView != null) { // Comprobación de nulidad
            // MODO VIDEO OFFLINE
            try {
                File videoFile = new File(localPath);
                if (videoFile.exists()) {
                    mediaView.setVisible(true);
                    Media media = new Media(videoFile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaPlayer.play();
                } else {
                    // El archivo no existe, mostrar imagen de respaldo
                    showImage(publication.getMainImageUrl());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showImage(publication.getMainImageUrl());
            }
        } else if (isVideo && videoUrl != null && !videoUrl.isEmpty()) {
            // MODO VIDEO ONLINE
            videoWebView.setVisible(true);
            if (videoUrl.contains(".mp4") || videoUrl.contains("images-assets.nasa.gov")) {
                String htmlContent = "<!DOCTYPE html><html style='height:100%; margin:0; padding:0; background:black;'><body style='height:100%; margin:0; padding:0; display:flex; align-items:center; justify-content:center;'><video width='100%' height='100%' controls autoplay name='media'><source src='" + videoUrl + "' type='video/mp4'></video></body></html>";
                videoWebView.getEngine().loadContent(htmlContent);
            } else {
                videoWebView.getEngine().load(videoUrl);
            }
        } else {
            // MODO IMAGEN
            showImage(publication.getMainImageUrl());
        }
    }

    private void showImage(String imageUrl) {
        imagePane.setVisible(true);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String cleanUrl = imageUrl.replace(" ", "%20");
            String style = String.format("-fx-background-image: url('%s');", cleanUrl);
            imagePane.setStyle(style);
        } else {
            imagePane.setStyle("-fx-background-color: #2b2b2b;");
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        if (videoWebView != null) {
            videoWebView.getEngine().loadContent("");
        }
    }

    @FXML
    public void handleCloseClick() {
        stopPlayback();
        if (mainController != null) {
            mainController.hideDetailedView();
        }
    }
}
