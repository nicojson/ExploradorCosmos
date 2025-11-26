package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Rocket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardController {

    @FXML private ImageView imageView;
    @FXML private Label cardTitle;
    @FXML private HBox actions;
    @FXML private ActionsController actionsController;
    @FXML private StackPane playIconContainer;

    private MainProgramController mainController;

    private String imageUrl;
    private String title;
    private String details;
    private String videoUrl;

    public void setup(Object data, MainProgramController mainController) {
        this.mainController = mainController;
        this.videoUrl = null;
        boolean isVideo = false;

        if (data instanceof com.example.explorandoelcosmos.model.Publication publication) {
            this.imageUrl = publication.getMainImageUrl();
            this.title = publication.getTitle();
            this.details = publication.getDescription();
            this.videoUrl = publication.getContentUrl();

            if ("video".equals(publication.getContentType()) || (this.videoUrl != null && !this.videoUrl.isEmpty())) {
                isVideo = true;
            }

        } else if (data instanceof Rocket rocket) {
            this.imageUrl = rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty()
                    ? rocket.getFlickrImages().get(0)
                    : null;
            this.title = rocket.getName();
            this.details = rocket.getDescription();

        } else if (data instanceof MoonPhaseResponse moonPhaseResponse) {
            this.imageUrl = moonPhaseResponse.getData() != null ? moonPhaseResponse.getData().getImageUrl() : null;
            this.title = "Fase Lunar (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")) + ")";
            this.details = "Datos de Astronomy API.";

        } else if (data instanceof NasaItem nasaItem) {
            this.imageUrl = nasaItem.getPreviewImageUrl();
            if (nasaItem.getData() != null && !nasaItem.getData().isEmpty()) {
                this.title = nasaItem.getData().get(0).getTitle();
                this.details = nasaItem.getData().get(0).getDescription();

                if ("video".equals(nasaItem.getData().get(0).getMediaType())) {
                    isVideo = true;
                }
            } else {
                this.title = "NASA Item";
                this.details = "";
            }
        }

        cardTitle.setText(title != null ? title : "Sin Título");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl, 320, 0, true, true, true);
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
        }

        if (playIconContainer != null) {
            playIconContainer.setVisible(isVideo);
        }

        actionsController.setData(title, imageUrl);
    }

    @FXML
    private void handleCardClick() {
        if (mainController != null) {
            mainController.showDetailedView(imageUrl, title, details, videoUrl);
        }
    }
}