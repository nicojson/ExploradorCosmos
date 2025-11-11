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
import javafx.scene.layout.TilePane; // <-- Importado
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane; // <-- Importado

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainProgramController {

    // --- CAMPOS FXML ---
    @FXML
    private Label dateLabel;
    @FXML
    private Label userDataLabel;
    @FXML
    private TextField searchField;
    @FXML
    private MenuButton menuButton;
    @FXML
    private TilePane contentContainer; // <-- El nuevo contenedor de cuadrícula
    @FXML
    private Label btnSpaceX;
    @FXML
    private Label btnSolarSystem;
    @FXML
    private Label btnPlanet;
    @FXML
    private Label btnJamesWebb;
    @FXML
    private Label btnFavoritos; // <-- El nuevo botón de favoritos
    @FXML
    private ScrollPane contentScrollPane; // El ScrollPane

    private Label currentActiveFilter; // Para rastrear el filtro activo

    // --- SERVICIOS ---
    private SpaceXDataService spaceXDataService;
    private NasaDataService nasaDataService;
    private SolarSystemDataService solarSystemDataService;
    private PlanetDataService planetDataService;
    private JamesWebbDataService jamesWebbDataService;

    @FXML
    public void initialize() {
        // Inicializa los servicios
        spaceXDataService = new SpaceXDataService();
        nasaDataService = new NasaDataService();
        solarSystemDataService = new SolarSystemDataService();
        planetDataService = new PlanetDataService();
        jamesWebbDataService = new JamesWebbDataService();

        // Configura la UI
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        userDataLabel.setText("Usuario: Invitado");

        // Carga el contenido inicial
        loadRandomReels();
    }

    /**
     * Carga contenido aleatorio de todas las APIs
     */
    private void loadRandomReels() {
        contentContainer.getChildren().clear(); // Limpia el contenedor

        // Quita la selección de cualquier filtro
        if (currentActiveFilter != null) {
            currentActiveFilter.getStyleClass().remove("api-label-active");
            if (!currentActiveFilter.getStyleClass().contains("api-label")) {
                currentActiveFilter.getStyleClass().add("api-label");
            }
            currentActiveFilter = null;
        }

        loadSpaceXReel();
        loadSolarSystemReel();
        loadPlanetReel();
        loadJamesWebbReel();
    }

    /**
     * Maneja el estado visual de los botones de filtro.
     */
    private void setActiveFilter(Label selectedLabel) {
        // 1. Quitar el estilo "activo" del botón anterior
        if (currentActiveFilter != null) {
            currentActiveFilter.getStyleClass().remove("api-label-active");
            if (!currentActiveFilter.getStyleClass().contains("api-label")) {
                currentActiveFilter.getStyleClass().add("api-label");
            }
        }

        // 2. Añadir el estilo "activo" al botón seleccionado
        selectedLabel.getStyleClass().remove("api-label");
        if (!selectedLabel.getStyleClass().contains("api-label-active")) {
            selectedLabel.getStyleClass().add("api-label-active");
        }

        // 3. Actualizar la referencia
        currentActiveFilter = selectedLabel;
    }

    // --- MÉTODOS DE FILTRO ---
    @FXML
    private void handleFilterSpaceX(MouseEvent event) {
        contentContainer.getChildren().clear();
        loadSpaceXReel();
        setActiveFilter(btnSpaceX);
    }

    @FXML
    private void handleFilterSolar(MouseEvent event) {
        contentContainer.getChildren().clear();
        loadSolarSystemReel();
        setActiveFilter(btnSolarSystem);
    }

    @FXML
    private void handleFilterPlanet(MouseEvent event) {
        contentContainer.getChildren().clear();
        loadPlanetReel();
        setActiveFilter(btnPlanet);
    }

    @FXML
    private void handleFilterWebb(MouseEvent event) {
        contentContainer.getChildren().clear();
        loadJamesWebbReel();
        setActiveFilter(btnJamesWebb);
    }

    @FXML
    private void handleFilterFavoritos(MouseEvent event) {
        contentContainer.getChildren().clear();
        setActiveFilter(btnFavoritos);
        // TODO: Llamar al método para cargar favoritos desde la BD
        // loadFavorites();
        System.out.println("Cargando favoritos...");
        // (Puedes añadir un addPlaceholderReel si no hay favoritos)
        addPlaceholderReel("Favoritos (Aún no implementado)");
    }

    // --- LÓGICA DE CARGA DE REELS ---

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

    // --- MÉTODOS DE AYUDA (addReel, addPlaceholder, handleError) ---

    private void addReelToShowcase(String imageUrl, String apiName) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150); // <-- Asumo que esto era 150
        imageView.setPreserveRatio(true);

        // Usar un placeholder mientras carga
        // Image placeholder = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        // imageView.setImage(placeholder);

        Image image = new Image(imageUrl, true); // Carga en background
        imageView.setImage(image);

        Label label = new Label(apiName);
        VBox reel = new VBox(imageView, label);
        reel.getStyleClass().add("reel");

        contentContainer.getChildren().add(reel);
    }

    private void addPlaceholderReel(String apiName) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        // (Opcional: cargar una imagen placeholder local)
        // imageView.setImage(new Image(getClass().getResourceAsStream("/images/placeholder.png")));

        Label label = new Label(apiName);
        VBox reel = new VBox(imageView, label);
        reel.getStyleClass().add("reel");
        contentContainer.getChildren().add(reel);
    }

    private void handleApiError(String apiName, Throwable exception) {
        System.err.println("Error al cargar la imagen de '" + apiName + "': " + exception.getMessage());
        exception.printStackTrace();
        addPlaceholderReel(apiName + " (Error)");
    }

    // --- MÉTODOS DEL MENÚ ---

    @FXML
    void handleSearch(ActionEvent event) {
        System.out.println("Buscando: " + searchField.getText());
        // TODO: Implementar lógica de búsqueda
        // 1. Limpiar el contentContainer
        // 2. Llamar a los servicios de búsqueda
        // 3. Añadir los resultados con addReelToShowcase
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        System.out.println("Cerrar Sesión");
        // TODO: Añadir lógica para volver a la pantalla de login
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