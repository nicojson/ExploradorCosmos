package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class OfflineVideoController {

    @FXML private MediaView mediaView;
    @FXML private StackPane mediaContainer;
    @FXML private Label titleLabel;
    @FXML private Label detailsLabel;
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

        String localPath = publication.getLocalPath();

        if (localPath != null && !localPath.isEmpty()) {
            try {
                File videoFile = new File(localPath);
                if (videoFile.exists()) {
                    Media media = new Media(videoFile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);

                    mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
                    mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());
                    mediaView.setMediaPlayer(mediaPlayer);

                    mediaPlayer.setOnError(() -> {
                        System.err.println("MediaPlayer Error: " + mediaPlayer.getError());
                        mediaPlayer.getError().printStackTrace();
                    });

                    mediaPlayer.setOnReady(() -> mediaPlayer.play());
                } else {
                    System.err.println("Error: El archivo de video no existe en la ruta: " + localPath);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar el video local: " + localPath);
                e.printStackTrace();
            }
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
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
