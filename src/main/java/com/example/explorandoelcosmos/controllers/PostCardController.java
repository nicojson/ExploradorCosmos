package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import java.time.Duration;
import java.time.LocalDateTime;

public class PostCardController {

    @FXML
    private Pane previewPane;
    @FXML
    private Label contentLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private HBox actions;
    @FXML
    private ActionsController actionsController;

    private MainProgramController mainController;
    private Publication publication;

    public void setup(Publication publication, MainProgramController mainController) {
        this.publication = publication;
        this.mainController = mainController;

        contentLabel
                .setText(publication.getDescription() != null ? publication.getDescription() : publication.getTitle());

        if (publication.getPublishedDate() != null) {
            timeLabel.setText(getRelativeTime(publication.getPublishedDate()));
        } else {
            timeLabel.setText("");
        }

        // Set preview image if available
        if (publication.getMainImageUrl() != null && !publication.getMainImageUrl().isEmpty()) {
            previewPane.setStyle("-fx-background-image: url('" + publication.getMainImageUrl() + "');");
            previewPane.setVisible(true);
            previewPane.setManaged(true);
        } else {
            previewPane.setVisible(false);
            previewPane.setManaged(false);
        }

        // Setup actions
        actionsController.setData(publication.getTitle(), publication.getMainImageUrl());
    }

    private String getRelativeTime(LocalDateTime date) {
        Duration duration = Duration.between(date, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60)
            return seconds + "s";
        if (seconds < 3600)
            return (seconds / 60) + "m";
        if (seconds < 86400)
            return (seconds / 3600) + "h";
        return (seconds / 86400) + "d";
    }

    @FXML
    private void handleCardClick() {
        if (mainController != null) {
            mainController.showDetailedView(publication.getMainImageUrl(), publication.getTitle(),
                    publication.getDescription(), publication);
        }
    }
}
