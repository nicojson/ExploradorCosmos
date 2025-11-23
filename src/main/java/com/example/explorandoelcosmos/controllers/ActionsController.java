package com.example.explorandoelcosmos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ActionsController {

    @FXML private Button favoriteButton;
    @FXML private Button shareButton;

    private String title;
    private String imageUrl;
    private boolean isFavorite = false;

    public void setData(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        // Aquí se podría añadir lógica para comprobar si el item ya es favorito
    }

    @FXML
    private void handleFavoriteClick() {
        isFavorite = !isFavorite;
        if (isFavorite) {
            favoriteButton.setStyle("-fx-text-fill: #FFD700;");
            System.out.println("'" + title + "' añadido a favoritos.");
        } else {
            favoriteButton.setStyle("");
            System.out.println("'" + title + "' eliminado de favoritos.");
        }
    }

    @FXML
    private void handleShareClick() {
        System.out.println("Compartiendo: " + imageUrl);
        // Lógica para copiar al portapapeles, etc.
    }
}
