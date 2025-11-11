package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.service.JamesWebbDataService;
import com.example.explorandoelcosmos.service.NasaDataService;
import com.example.explorandoelcosmos.service.PlanetDataService;
import com.example.explorandoelcosmos.service.SolarSystemDataService;
import com.example.explorandoelcosmos.service.SpaceXDataService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainProgramController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label userDataLabel;

    @FXML
    private TextField searchField;

    @FXML
    private HBox reelsContainer;

    private SpaceXDataService spaceXDataService;
    private NasaDataService nasaDataService;
    private SolarSystemDataService solarSystemDataService;
    private PlanetDataService planetDataService;
    private JamesWebbDataService jamesWebbDataService;

    @FXML
    public void initialize() {
        spaceXDataService = new SpaceXDataService();
        nasaDataService = new NasaDataService();
        solarSystemDataService = new SolarSystemDataService();
        planetDataService = new PlanetDataService();
        jamesWebbDataService = new JamesWebbDataService();

        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        userDataLabel.setText("Usuario: Invitado");

        loadRandomReels();
    }

    private void loadRandomReels() {
        loadSpaceXReel();
        loadSolarSystemReel();
        loadPlanetReel();
        loadJamesWebbReel(); // Updated call
    }

    private void loadSpaceXReel() {
        Task<String> fetchImageUrlTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return spaceXDataService.getRandomRocketImage();
            }
        };
        fetchImageUrlTask.setOnSucceeded(e -> addReelToShowcase(fetchImageUrlTask.getValue(), "SpaceX"));
        fetchImageUrlTask.setOnFailed(e -> handleApiError("SpaceX", fetchImageUrlTask.getException()));
        new Thread(fetchImageUrlTask).start();
    }

    private void loadSolarSystemReel() {
        Task<String> fetchNameAndImageUrlTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                String bodyName = solarSystemDataService.getRandomBodyName();
                return nasaDataService.getRandomImageUrl(bodyName);
            }
        };
        fetchNameAndImageUrlTask.setOnSucceeded(e -> addReelToShowcase(fetchNameAndImageUrlTask.getValue(), "The Solar System"));
        fetchNameAndImageUrlTask.setOnFailed(e -> handleApiError("The Solar System", fetchNameAndImageUrlTask.getException()));
        new Thread(fetchNameAndImageUrlTask).start();
    }

    private void loadPlanetReel() {
        Task<String> fetchImageUrlTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return planetDataService.getRandomItemThumbnail();
            }
        };
        fetchImageUrlTask.setOnSucceeded(e -> addReelToShowcase(fetchImageUrlTask.getValue(), "Planet"));
        fetchImageUrlTask.setOnFailed(e -> handleApiError("Planet", fetchImageUrlTask.getException()));
        new Thread(fetchImageUrlTask).start();
    }

    private void loadJamesWebbReel() {
        Task<String> fetchImageUrlTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return jamesWebbDataService.getRandomImageUrl();
            }
        };
        fetchImageUrlTask.setOnSucceeded(e -> addReelToShowcase(fetchImageUrlTask.getValue(), "James Webb"));
        fetchImageUrlTask.setOnFailed(e -> handleApiError("James Webb", fetchImageUrlTask.getException()));
        new Thread(fetchImageUrlTask).start();
    }

    private void addReelToShowcase(String imageUrl, String apiName) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);

        Image image = new Image(imageUrl, true);
        imageView.setImage(image);

        Label label = new Label(apiName);
        VBox reel = new VBox(imageView, label);
        reel.getStyleClass().add("reel");
        reelsContainer.getChildren().add(reel);
    }

    private void addPlaceholderReel(String apiName) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        Label label = new Label(apiName);
        VBox reel = new VBox(imageView, label);
        reel.getStyleClass().add("reel");
        reelsContainer.getChildren().add(reel);
    }

    private void handleApiError(String apiName, Throwable exception) {
        System.err.println("Error al cargar la imagen de '" + apiName + "': " + exception.getMessage());
        exception.printStackTrace();
        addPlaceholderReel(apiName + " (Error)");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        System.out.println("Buscando: " + searchField.getText());
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        System.out.println("Cerrar Sesión");
    }

    @FXML
    void handleEditarPerfil(ActionEvent event) {
        System.out.println("Editar Perfil");
    }

    @FXML
    void handleModoSinConexion(ActionEvent event) {
        System.out.println("Modo sin conexión");
    }

    @FXML
    void handleAcercaDe(ActionEvent event) {
        System.out.println("Acerca de...");
    }
}
