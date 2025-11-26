package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.time.format.DateTimeFormatter;

public class VideoCardController {

    @FXML
    private Pane thumbnailPane;
    @FXML
    private Label durationLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private HBox actions;
    @FXML
    private ActionsController actionsController;

    private MainProgramController mainController;
    private Publication publication;

    public void setup(Publication publication, MainProgramController mainController) {
        this.publication = publication;
        this.mainController = mainController;

        titleLabel.setText(publication.getTitle());
        descriptionLabel.setText(publication.getDescription());

        if (publication.getPublishedDate() != null) {
            dateLabel.setText(publication.getPublishedDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        } else {
            dateLabel.setText("");
        }

        // Set thumbnail
        if (publication.getMainImageUrl() != null && !publication.getMainImageUrl().isEmpty()) {
            thumbnailPane.setStyle("-fx-background-image: url('" + publication.getMainImageUrl() + "');");
        }

        // Setup actions
        actionsController.setData(publication.getTitle(), publication.getMainImageUrl());
    }

    @FXML
    private void handleCardClick() {
        if (mainController != null) {
            mainController.showDetailedView(publication.getMainImageUrl(), publication.getTitle(),
                    publication.getDescription(), publication.getContentUrl());
        }
    }
}
