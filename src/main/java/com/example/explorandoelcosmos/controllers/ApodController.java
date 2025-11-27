package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import java.time.format.DateTimeFormatter;

public class ApodController {

    @FXML private ImageView imageView;
    @FXML private WebView videoWebView;
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label urlLabel;
    @FXML private Label versionLabel;
    @FXML private ActionsController actionsController;

    public void setData(Publication publication, MainProgramController mainController) {
        titleLabel.setText(publication.getTitle());
        descriptionLabel.setText(publication.getDescription());
        urlLabel.setText(publication.getContentUrl() != null ? publication.getContentUrl() : publication.getMainImageUrl());
        versionLabel.setText(publication.getServiceVersion() != null ? publication.getServiceVersion() : "v1");

        if (publication.getPublishedDate() != null) {
            dateLabel.setText(publication.getPublishedDate().format(DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy")));
        } else {
            dateLabel.setText("");
        }

        if (actionsController != null) {
            actionsController.setData(publication, mainController);
        }

        String url = publication.getContentUrl() != null ? publication.getContentUrl() : publication.getMainImageUrl();

        if ("video".equals(publication.getContentType())) {
            imageView.setVisible(false);
            videoWebView.setVisible(true);
            if (url != null) videoWebView.getEngine().load(url);
        } else {
            videoWebView.setVisible(false);
            imageView.setVisible(true);
            if (url != null) {
                Image image = new Image(url, 300, 0, true, true, true);
                imageView.setImage(image);
            }
        }
    }
}
