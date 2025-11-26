package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;
import com.example.explorandoelcosmos.dao.PublicationDAO;
import com.example.explorandoelcosmos.dao.PublicationDAOImpl;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;
import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.ApodService;
import com.example.explorandoelcosmos.service.AstronomyApiService;
import com.example.explorandoelcosmos.service.JWSTService;
import com.example.explorandoelcosmos.service.NasaLibraryService;
import com.example.explorandoelcosmos.service.NotificationManager;
import com.example.explorandoelcosmos.service.ReportService;
import com.example.explorandoelcosmos.service.SpaceXDataService;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainProgramController {

    // === FXML Fields ===
    @FXML private StackPane rootStackPane;
    @FXML private ScrollPane contentScrollPane;
    @FXML private GridPane contentContainer;
    @FXML private Label btnFavoritos;
    @FXML private Label userDataLabel;
    @FXML private MenuButton menuButton;
    @FXML private VBox progressBox;
    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;
    @FXML private TextField searchField;
    @FXML private VBox filterPane;
    @FXML private ComboBox<Integer> yearStartComboBox;
    @FXML private ComboBox<Integer> yearEndComboBox;
    @FXML private ComboBox<String> apiComboBox;

    // === Class Fields ===
    private String activeEndpointName;
    private Node detailedView;
    private Node overlay;
    private boolean isFilterPaneVisible = false;

    // === Services ===
    private final SpaceXDataService spaceXService = new SpaceXDataService();
    private final AstronomyApiService astronomyApiService = new AstronomyApiService();
    private final NasaLibraryService nasaLibraryService = new NasaLibraryService();
    private final JWSTService jwstService = new JWSTService();
    // Servicio nuevo para la Foto del Día
    private final ApodService apodService = new ApodService();
    private final ReportService reportService = new ReportService();
    private final AppConfigDAO appConfigDAO = new AppConfigDAOImpl();
    private final PublicationDAO publicationDAO = new PublicationDAOImpl();

    @FXML
    public void initialize() {
        setupOcularClip();
        setupSearchInteraction();
        setupFilterPane();
        loadApiEndpointsIntoComboBox();
    }

    private void setupOcularClip() {
        Circle clip = new Circle();
        clip.radiusProperty().bind(contentScrollPane.widthProperty().divide(2));
        clip.centerXProperty().bind(contentScrollPane.widthProperty().divide(2));
        clip.centerYProperty().bind(contentScrollPane.heightProperty().divide(2));
        contentScrollPane.setClip(clip);
    }

    private void setupSearchInteraction() {
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && !isFilterPaneVisible) {
                toggleFilterPane(true);
            }
        });

        rootStackPane.setOnMouseClicked(event -> {
            if (isFilterPaneVisible && !filterPane.getBoundsInParent().contains(event.getX(), event.getY())
                    && !searchField.getBoundsInParent().contains(event.getX(), event.getY())) {
                toggleFilterPane(false);
            }
        });
    }

    private void setupFilterPane() {
        List<Integer> years = IntStream.rangeClosed(1920, LocalDate.now().getYear()).boxed()
                .collect(Collectors.toList());
        yearStartComboBox.getItems().addAll(years);
        yearEndComboBox.getItems().addAll(years);
        yearStartComboBox.setValue(1920);
        yearEndComboBox.setValue(LocalDate.now().getYear());
        filterPane.setTranslateY(150);
    }

    private void toggleFilterPane(boolean show) {
        isFilterPaneVisible = show;
        filterPane.setVisible(true);
        filterPane.setManaged(true);

        TranslateTransition tt = new TranslateTransition(Duration.millis(300), filterPane);
        tt.setToY(show ? 0 : 150);
        tt.setOnFinished(e -> {
            if (!show) {
                filterPane.setVisible(false);
                filterPane.setManaged(false);
            }
        });
        tt.play();
    }

    public void setupSession(User user) {
        if (user != null) {
            userDataLabel.setText("Operador: " + user.getUsername());
        } else {
            userDataLabel.setText("Modo: Invitado");
            btnFavoritos.setVisible(false);
            btnFavoritos.setManaged(false);
            MenuItem editarPerfil = findMenuItem(menuButton, "Editar Perfil");
            if (editarPerfil != null) editarPerfil.setVisible(false);
        }
    }

    private MenuItem findMenuItem(MenuButton menuButton, String text) {
        for (MenuItem item : menuButton.getItems()) {
            if (text.equals(item.getText())) return item;
        }
        return null;
    }

    public void showDetailedView(String imageUrl, String title, String details, String videoUrl) {
        try {
            if (overlay == null) {
                overlay = new StackPane();
                overlay.getStyleClass().add("overlay-pane");
                overlay.setOnMouseClicked(event -> hideDetailedView());
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/explorandoelcosmos/detailed-card-view.fxml"));
            detailedView = loader.load();
            DetailedCardController controller = loader.getController();
            controller.setMainController(this);
            controller.setData(imageUrl, title, details, videoUrl);

            detailedView.setScaleX(0.8);
            detailedView.setScaleY(0.8);
            overlay.setOpacity(0);

            rootStackPane.getChildren().addAll(overlay, detailedView);

            FadeTransition fadeOverlay = new FadeTransition(Duration.millis(200), overlay);
            fadeOverlay.setToValue(1);

            ScaleTransition scaleView = new ScaleTransition(Duration.millis(200), detailedView);
            scaleView.setToX(1);
            scaleView.setToY(1);

            ParallelTransition transition = new ParallelTransition(fadeOverlay, scaleView);
            transition.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideDetailedView() {
        if (detailedView != null && overlay != null) {
            FadeTransition fadeOverlay = new FadeTransition(Duration.millis(200), overlay);
            fadeOverlay.setToValue(0);

            ScaleTransition scaleView = new ScaleTransition(Duration.millis(200), detailedView);
            scaleView.setToX(0.8);
            scaleView.setToY(0.8);

            FadeTransition fadeView = new FadeTransition(Duration.millis(200), detailedView);
            fadeView.setToValue(0);

            ParallelTransition transition = new ParallelTransition(fadeOverlay, scaleView, fadeView);
            transition.setOnFinished(event -> rootStackPane.getChildren().removeAll(overlay, detailedView));
            transition.play();
        }
    }

    private Node createCardNode(Object data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/explorandoelcosmos/card-view.fxml"));
        Node cardNode = loader.load();
        CardController cardController = loader.getController();
        cardController.setup(data, this);
        return cardNode;
    }

    private void loadApiEndpointsIntoComboBox() {
        Task<List<ApiEndpointConfig>> task = new Task<>() {
            @Override
            protected List<ApiEndpointConfig> call() {
                return appConfigDAO.findAllEndpointConfigs();
            }
        };
        task.setOnSucceeded(e -> {
            ObservableList<String> endpointNames = FXCollections.observableArrayList(
                    task.getValue().stream()
                            .map(ApiEndpointConfig::getEndpointName)
                            .collect(Collectors.toList()));
            apiComboBox.setItems(endpointNames);

            if (!endpointNames.isEmpty()) {
                // Seleccionar APOD por defecto si existe
                if (endpointNames.contains("Astronomy API")) {
                    apiComboBox.getSelectionModel().select("Astronomy API");
                } else if (endpointNames.contains("Foto del Día")) {
                    apiComboBox.getSelectionModel().select("Foto del Día");
                } else {
                    apiComboBox.getSelectionModel().selectFirst();
                }

                activeEndpointName = apiComboBox.getSelectionModel().getSelectedItem();
                loadDataForActiveFilter("", 0, 0);
            }
        });
        task.setOnFailed(e -> NotificationManager.showError("Error", "No se pudieron cargar los nombres de los endpoints."));
        new Thread(task).start();
    }

    @FXML
    private void handleLoadSelectedApi() {
        String selectedEndpoint = apiComboBox.getSelectionModel().getSelectedItem();
        if (selectedEndpoint != null) {
            activeEndpointName = selectedEndpoint;
            loadDataForActiveFilter("", 0, 0);
        } else {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona una API para cargar.");
        }
    }

    @FXML
    private void handleAdvancedSearch(ActionEvent event) {
        String query = searchField.getText();
        int yearStart = yearStartComboBox.getValue();
        int yearEnd = yearEndComboBox.getValue();
        loadDataForActiveFilter(query, yearStart, yearEnd);
        toggleFilterPane(false);
    }

    private void loadDataForActiveFilter(String query, int yearStart, int yearEnd) {
        // Lógica visual: Solo mostrar esqueletos si NO es APOD
        if (!activeEndpointName.equals("Astronomy API") && !activeEndpointName.equals("Foto del Día")) {
            showLoadingSkeletons();
        } else {
            contentContainer.getChildren().clear(); // Limpiar para APOD
        }

        String filterName = activeEndpointName;

        loadInThread(() -> {
            try {
                List<Publication> publications = new ArrayList<>();

                // === LÓGICA APOD (Foto del Día) ===
                if (filterName.equals("Foto del Día") || filterName.equals("Astronomy API")) {
                    try {
                        // Obtener objeto único
                        Publication apod = apodService.getTodaysApodAsPublication();

                        // Actualizar UI con Vista Especial Centrada
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/explorandoelcosmos/apod-view.fxml"));
                                Node apodNode = loader.load();
                                ApodController controller = loader.getController();
                                controller.setData(apod);

                                contentContainer.getChildren().clear();
                                contentContainer.add(apodNode, 0, 0);

                                // Forzar centrado en el GridPane
                                javafx.scene.layout.GridPane.setHalignment(apodNode, javafx.geometry.HPos.CENTER);
                                javafx.scene.layout.GridPane.setValignment(apodNode, javafx.geometry.VPos.CENTER);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return; // Salir para no ejecutar lógica de lista
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // === LÓGICA LISTAS (SpaceX, NASA, etc.) ===
                switch (filterName) {
                    case "SpaceX":
                        publications.addAll(spaceXService.getRocketsAsPublications());
                        publications.addAll(spaceXService.getLaunchesAsPublications());
                        break;
                    case "JWST":
                        publications.addAll(jwstService.getImagesAsPublications(1, 20));
                        break;
                    case "Biblioteca NASA":
                    case "NASA":
                        publications.addAll(nasaLibraryService.searchImagesAsPublications(query, yearStart, yearEnd));
                        break;
                }

                List<Publication> filteredData = filterPublications(publications, query);

                Platform.runLater(() -> {
                    try {
                        List<Node> cards = new ArrayList<>();
                        for (Publication item : filteredData) {
                            cards.add(createCardNode(item));
                        }
                        updateGridInternal(cards);
                    } catch (IOException e) {
                        handleApiError(filterName, e);
                    }
                });

            } catch (IOException e) {
                handleApiError(filterName, e);
            }
        });
    }

    private List<Publication> filterPublications(List<Publication> data, String query) {
        if (query.isEmpty()) return data;
        String lowerCaseQuery = query.toLowerCase();
        return data.stream()
                .filter(item -> item.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerCaseQuery)))
                .collect(Collectors.toList());
    }

    private void showLoadingSkeletons() {
        contentContainer.getChildren().clear();
        // Cargar 9 esqueletos para la cuadrícula de 3x3
        for (int i = 0; i < 9; i++) {
            try {
                Node card = FXMLLoader.load(getClass().getResource("/com/example/explorandoelcosmos/loading-card-view.fxml"));
                FadeTransition ft = new FadeTransition(Duration.seconds(0.8), card);
                ft.setFromValue(1.0);
                ft.setToValue(0.6);
                ft.setAutoReverse(true);
                ft.setCycleCount(FadeTransition.INDEFINITE);
                ft.play();

                int columns = 3;
                int row = i / columns;
                int col = i % columns;
                contentContainer.add(card, col, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGridInternal(List<Node> cards) {
        contentContainer.getChildren().clear();
        if (cards.isEmpty()) {
            NotificationManager.showInfo("Sin Resultados", "No se encontraron elementos.");
        } else {
            // Usar 3 columnas
            int columns = 3;
            for (int i = 0; i < cards.size(); i++) {
                int row = i / columns;
                int col = i % columns;
                contentContainer.add(cards.get(i), col, row);
            }
        }
    }

    private void loadInThread(Runnable task) {
        new Thread(task).start();
    }

    private void handleApiError(String apiName, IOException e) {
        System.err.println("Error al obtener datos de " + apiName + ": " + e.getMessage());
        e.printStackTrace();
        Platform.runLater(() -> {
            contentContainer.getChildren().clear();
            NotificationManager.showError("Error de Red",
                    "No se pudieron cargar los datos de " + apiName + ". " + e.getMessage());
        });
    }

    public void handleCerrarSesion(ActionEvent actionEvent) {}
    public void handleEditarPerfil(ActionEvent actionEvent) {}
    public void handleAcercaDe(ActionEvent actionEvent) {}

    public void handleFilterFavoritos(MouseEvent mouseEvent) {
        showLoadingSkeletons();
        loadInThread(() -> {
            List<Publication> favorites = publicationDAO.findFavorites();
            Platform.runLater(() -> {
                try {
                    List<Node> cards = new ArrayList<>();
                    for (Publication item : favorites) {
                        cards.add(createCardNode(item));
                    }
                    updateGridInternal(cards);
                } catch (IOException e) {
                    e.printStackTrace();
                    NotificationManager.showError("Error", "No se pudieron cargar los favoritos.");
                }
            });
        });
    }

    @FXML
    private void handleReportsClick(MouseEvent event) {
        if (activeEndpointName == null || activeEndpointName.isEmpty()) {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona una API para generar el reporte.");
            return;
        }

        String reportName = activeEndpointName;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.setInitialFileName("Reporte_" + reportName.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(rootStackPane.getScene().getWindow());

        if (file == null) return;

        Task<Void> reportTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Obteniendo datos para el reporte...");
                updateProgress(0, 100);
                List<?> data = fetchDataForReport(reportName);
                updateMessage("Generando archivo PDF...");
                updateProgress(50, 100);
                reportService.generatePdfReport("Reporte de " + reportName, data, file.getAbsolutePath());
                updateProgress(100, 100);
                updateMessage("¡Reporte generado!");
                return null;
            }
        };

        reportTask.setOnSucceeded(e -> {
            progressBox.setVisible(false);
            progressBox.setManaged(false);
            NotificationManager.showSuccess("Éxito", "El reporte se ha guardado correctamente.");
        });

        reportTask.setOnFailed(e -> {
            progressBox.setVisible(false);
            progressBox.setManaged(false);
            NotificationManager.showError("Error", "No se pudo generar el reporte.");
            reportTask.getException().printStackTrace();
        });

        progressLabel.textProperty().bind(reportTask.messageProperty());
        progressBar.progressProperty().bind(reportTask.progressProperty());
        progressBox.setVisible(true);
        progressBox.setManaged(true);

        new Thread(reportTask).start();
    }

    private List<?> fetchDataForReport(String endpointName) throws IOException {
        List<Publication> publications = new ArrayList<>();
        switch (endpointName) {
            case "SpaceX":
                publications.addAll(spaceXService.getRocketsAsPublications());
                publications.addAll(spaceXService.getLaunchesAsPublications());
                break;
            case "JWST":
                publications.addAll(jwstService.getImagesAsPublications(1, 20));
                break;
            case "Biblioteca NASA":
            case "NASA":
                publications.addAll(nasaLibraryService.searchImagesAsPublications("", 0, 0));
                break;
        }
        return publications;
    }
}