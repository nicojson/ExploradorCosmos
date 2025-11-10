package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.service.SpaceXDataService;
// ¡No necesitamos Launch.java para esta vista!
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// (Importa ProgressIndicator si lo añades)

public class MainProgramController {

    @FXML
    private Button btnImage;

    @FXML
    private ImageView SpaceXImage;

    @FXML
    void obtenerImagen(ActionEvent event) {
        btnImage.setDisable(true);

        // La Tarea sigue devolviendo un String (la URL), así que solo cambiamos la llamada interna
        Task<String> fetchImageUrlTask = new Task<>() {
            @Override
            protected String call() throws Exception {

                // --- ¡ESTA ES LA LÍNEA MODIFICADA! ---
                // Llamamos al nuevo método aleatorio en lugar de getLatestLaunch()
                return SpaceXDataService.getInstance().getRandomRocketImage();
            }
        };

        // El resto del código (setOnSucceeded y setOnFailed)
        // funciona exactamente igual que antes.

        fetchImageUrlTask.setOnSucceeded(e -> {
            String imageUrl = fetchImageUrlTask.getValue();
            Image spaceXImage = new Image(imageUrl, true); // Carga en background
            SpaceXImage.setImage(spaceXImage);
            btnImage.setDisable(false);
        });

        fetchImageUrlTask.setOnFailed(e -> {
            System.err.println("Error al cargar la imagen: " + fetchImageUrlTask.getException().getMessage());
            fetchImageUrlTask.getException().printStackTrace();
            btnImage.setDisable(false);
        });

        new Thread(fetchImageUrlTask).start();
    }
}