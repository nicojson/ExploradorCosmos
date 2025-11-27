package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Publication;
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
    private Publication publication;

    public void setup(Object data, MainProgramController mainController) {
        this.mainController = mainController;
        boolean isVideo = false;

        if (data instanceof Publication pub) {
            this.publication = pub;
            if ("video".equals(this.publication.getContentType()) || (this.publication.getContentUrl() != null && !this.publication.getContentUrl().isEmpty())) {
                isVideo = true;
            }
        } else if (data instanceof NasaItem nasaItem) {
            // Crear un objeto Publication a partir de NasaItem
            this.publication = new Publication();
            if (nasaItem.getData() != null && !nasaItem.getData().isEmpty()) {
                this.publication.setTitle(nasaItem.getData().get(0).getTitle());
                this.publication.setDescription(nasaItem.getData().get(0).getDescription());
                this.publication.setMainImageUrl(nasaItem.getPreviewImageUrl());
                this.publication.setSourceApiId(3); // 3 es el ID para NASA

                if ("video".equals(nasaItem.getData().get(0).getMediaType())) {
                    isVideo = true;
                    this.publication.setContentType("video");
                    // Aquí necesitarás una forma de obtener la URL del video desde NasaItem
                    // this.publication.setContentUrl(nasaItem.getVideoUrl());
                } else {
                    this.publication.setContentType("image");
                }
            }
        } else if (data instanceof Rocket rocket) {
            this.publication = new Publication();
            this.publication.setTitle(rocket.getName());
            this.publication.setDescription(rocket.getDescription());
            this.publication.setMainImageUrl(rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty() ? rocket.getFlickrImages().get(0) : null);
            // No hay acciones para cohetes
        } else if (data instanceof MoonPhaseResponse moonPhaseResponse) {
            this.publication = new Publication();
            this.publication.setTitle("Fase Lunar (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM")) + ")");
            this.publication.setMainImageUrl(moonPhaseResponse.getData() != null ? moonPhaseResponse.getData().getImageUrl() : null);
            this.publication.setDescription("Datos de Astronomy API.");
            // No hay acciones para fase lunar
        }

        if (this.publication != null) {
            cardTitle.setText(this.publication.getTitle() != null ? this.publication.getTitle() : "Sin Título");
            String imageUrl = this.publication.getMainImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Image image = new Image(imageUrl, 320, 0, true, true, true);
                imageView.setImage(image);
            } else {
                imageView.setImage(null);
            }

            if (actionsController != null) {
                actionsController.setData(this.publication, mainController);
                actions.setVisible(true);
            } else {
                actions.setVisible(false);
            }
        }

        if (playIconContainer != null) {
            playIconContainer.setVisible(isVideo);
        }
    }

    @FXML
    private void handleCardClick() {
        if (mainController != null && publication != null) {
            mainController.showDetailedView(publication);
        }
    }
}
