package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.dao.PublicationDAO;
import com.example.explorandoelcosmos.dao.PublicationDAOImpl;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.service.NotificationManager;
import com.example.explorandoelcosmos.service.OfflineService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;

public class ActionsController {

    @FXML private Button favoriteButton;
    @FXML private Button shareButton;
    @FXML private Button downloadButton;
    @FXML private Button deleteButton; // Asumiendo que este botón se añade en el FXML

    private Publication publication;
    private PublicationDAO publicationDAO;
    private MainProgramController mainController;
    private boolean isFavorite = false;

    public ActionsController() {
        this.publicationDAO = new PublicationDAOImpl();
    }

    public void setData(Publication publication, MainProgramController mainController) {
        this.publication = publication;
        this.mainController = mainController;
        this.isFavorite = publication.isFavorite();
        updateFavoriteButton();

        if (deleteButton != null) {
            deleteButton.setVisible(publication.getLocalPath() != null && !publication.getLocalPath().isEmpty());
        }
    }

    @FXML
    private void handleFavoriteClick() {
        isFavorite = !isFavorite;
        publication.setFavorite(isFavorite);
        publicationDAO.update(publication);
        updateFavoriteButton();
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            favoriteButton.setStyle("-fx-text-fill: #FFD700;");
        } else {
            favoriteButton.setStyle("");
        }
    }

    @FXML
    private void handleShareClick() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        String urlToCopy = "video".equals(publication.getContentType()) ? publication.getContentUrl() : publication.getImageUrl();
        content.putString(urlToCopy);
        clipboard.setContent(content);
        NotificationManager.showInfo("Copiado", "URL copiada al portapapeles.");
    }

    @FXML
    private void handleDownloadClick() {
        if (mainController == null) {
            NotificationManager.showError("Error", "No se puede iniciar la descarga.");
            return;
        }

        Task<Void> downloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Iniciando descarga de: " + publication.getTitle());
                OfflineService.downloadPublication(publication, (bytesRead, totalBytes) -> {
                    updateProgress(bytesRead, totalBytes);
                    updateMessage(String.format("Descargando... %.2f MB de %.2f MB",
                            bytesRead / (1024.0 * 1024.0), totalBytes / (1024.0 * 1024.0)));
                });
                return null;
            }
        };

        downloadTask.setOnSucceeded(e -> {
            mainController.hideProgressBar();
            NotificationManager.showSuccess("Éxito", "¡'" + publication.getTitle() + "' descargado correctamente!");
            if (deleteButton != null) {
                deleteButton.setVisible(true);
            }
        });

        downloadTask.setOnFailed(e -> {
            mainController.hideProgressBar();
            Throwable exception = e.getSource().getException();
            if (exception instanceof IOException && exception.getMessage().contains("Formato de video no soportado")) {
                NotificationManager.showError("Formato no Soportado", "Solo se pueden descargar videos en formato MP4.");
            } else {
                NotificationManager.showError("Error de Descarga", "No se pudo descargar el archivo.");
            }
            exception.printStackTrace();
        });

        mainController.showProgressBar(downloadTask);
        new Thread(downloadTask).start();
    }

    @FXML
    private void handleDeleteClick() {
        try {
            OfflineService.deleteOfflinePublication(publication);
            NotificationManager.showSuccess("Eliminado", "El archivo ha sido eliminado.");
            if (deleteButton != null) {
                deleteButton.setVisible(false);
            }
        } catch (IOException e) {
            NotificationManager.showError("Error", "No se pudo eliminar el archivo.");
            e.printStackTrace();
        }
    }
}
