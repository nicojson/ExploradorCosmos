package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Rocket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardController {

    @FXML
    private Pane imagePane;
    @FXML
    private Label cardTitle;
    @FXML
    private HBox actions; // El contenedor del include
    @FXML
    private ActionsController actionsController; // El controlador inyectado

    private MainProgramController mainController;
    private String imageUrl;
    private String title;
    private String details;

    public void setup(Object data, MainProgramController mainController) {
        this.mainController = mainController;

        if (data instanceof com.example.explorandoelcosmos.model.Publication publication) {
            this.imageUrl = publication.getMainImageUrl();
            this.title = publication.getTitle();
            this.details = publication.getDescription();
        } else if (data instanceof Rocket rocket) {
            this.imageUrl = rocket.getFlickrImages() != null && !rocket.getFlickrImages().isEmpty()
                    ? rocket.getFlickrImages().get(0)
                    : null;
            this.title = rocket.getName();
            this.details = rocket.getDescription();
        } else if (data instanceof MoonPhaseResponse moonPhaseResponse) { // Manejo para Astronomy API
            this.imageUrl = moonPhaseResponse.getData() != null ? moonPhaseResponse.getData().getImageUrl() : null;
            this.title = "Fase Lunar de Hoy (" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + ")";
            this.details = "Imagen generada por Astronomy API.";
        } else if (data instanceof NasaItem nasaItem) {
            this.imageUrl = nasaItem.getPreviewImageUrl();
            // Asumiendo que siempre hay al menos un elemento en la lista de datos
            if (nasaItem.getData() != null && !nasaItem.getData().isEmpty()) {
                this.title = nasaItem.getData().get(0).getTitle();
                this.details = nasaItem.getData().get(0).getDescription();
            } else {
                this.title = "Elemento de la NASA";
                this.details = "Sin detalles disponibles.";
            }
        }
        // Los modelos MarsPhoto, JamesWebbImage, PlanetFeature y CelestialBody ya no se
        // usan directamente aquí

        cardTitle.setText(title != null ? title : "Sin Título");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String style = String.format("-fx-background-image: url('%s');", imageUrl);
            imagePane.setStyle(style);
        } else {
            imagePane.setStyle("-fx-background-color: #2b2b2b;"); // Color de fondo por defecto si no hay imagen
        }

        // Pasar los datos al controlador de acciones
        actionsController.setData(title, imageUrl);
    }

    @FXML
    private void handleCardClick() {
        if (mainController != null) {
            mainController.showDetailedView(imageUrl, title, details);
        }
    }
}
