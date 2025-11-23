package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;
import com.example.explorandoelcosmos.model.MoonPhaseResponse;
import com.example.explorandoelcosmos.model.NasaItem;
import com.example.explorandoelcosmos.model.Rocket;
import com.example.explorandoelcosmos.model.User;

import com.example.explorandoelcosmos.service.AstronomyApiService;
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
    // Botones orbitales individuales eliminados, ahora se usa ComboBox
    @FXML private Label btnFavoritos; // Este se mantiene
    @FXML private Label userDataLabel;
    @FXML private MenuButton menuButton;
    @FXML private VBox progressBox;
    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;
    @FXML private TextField searchField;

    // New UX Search Fields
    @FXML private VBox filterPane;
    @FXML private ComboBox<Integer> yearStartComboBox;
    @FXML private ComboBox<Integer> yearEndComboBox;

    // Nuevo ComboBox para seleccionar APIs
    @FXML private ComboBox<String> apiComboBox;

    // === Class Fields ===
    private String activeEndpointName; // Ahora guardamos el nombre del endpoint activo
    private Node detailedView;
    private Node overlay;
    private boolean isFilterPaneVisible = false;

    // === Services ===
    private final SpaceXDataService spaceXService = new SpaceXDataService();
    private final AstronomyApiService astronomyApiService = new AstronomyApiService();
    private final NasaLibraryService nasaLibraryService = new NasaLibraryService();
    private final ReportService reportService = new ReportService();
    private final AppConfigDAO appConfigDAO = new AppConfigDAOImpl(); // Necesario para cargar nombres de endpoints

    @FXML
    public void initialize() {
        setupOcularClip();
        setupSearchInteraction();
        setupFilterPane();
        loadApiEndpointsIntoComboBox(); // Cargar los nombres de los endpoints
        // No cargar nada por defecto, esperar la selección del usuario o cargar el primero
        // handleFilterSpaceX(null); // Eliminado
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
            if (isFilterPaneVisible && !filterPane.getBoundsInParent().contains(event.getX(), event.getY()) && !searchField.getBoundsInParent().contains(event.getX(), event.getY())) {
                toggleFilterPane(false);
            }
        });
    }

    private void setupFilterPane() {
        List<Integer> years = IntStream.rangeClosed(1920, LocalDate.now().getYear()).boxed().collect(Collectors.toList());
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
            if (text.equals(item.getText())) {
                return item;
            }
        }
        return null;
    }
    
    public void showDetailedView(String imageUrl, String title, String details) {
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
            controller.setData(imageUrl, title, details);
            
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
    
    // Métodos handleFilter... eliminados, ahora se usa handleLoadSelectedApi
    // @FXML private void handleFilterSpaceX(MouseEvent event) { ... }
    // @FXML private void handleFilterApod(MouseEvent event) { ... }
    // @FXML private void handleFilterNasaLibrary(MouseEvent event) { ... }

    // Nuevo método para cargar los nombres de los endpoints en el ComboBox
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
                    .collect(Collectors.toList())
            );
            apiComboBox.setItems(endpointNames);
            if (!endpointNames.isEmpty()) {
                apiComboBox.getSelectionModel().selectFirst();
                activeEndpointName = apiComboBox.getSelectionModel().getSelectedItem(); // Establecer el primero como activo
                loadDataForActiveFilter("", 0, 0); // Cargar datos del primer endpoint por defecto
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
        showLoadingSkeletons();
        String filterName = activeEndpointName; // Usamos el endpoint activo

        loadInThread(() -> {
            try {
                List<?> rawData;
                // La lógica de carga ahora depende del nombre del endpoint activo
                switch (filterName) {
                    case "SpaceX":
                        rawData = spaceXService.getRockets();
                        break;
                    case "Foto del Día": // Astronomy API
                        MoonPhaseResponse moonPhase = astronomyApiService.getMoonPhase(LocalDate.now());
                        rawData = new ArrayList<>();
                        //if (moonPhase != null) rawData.add(moonPhase);
                        break;
                    case "Biblioteca NASA":
                        rawData = nasaLibraryService.searchImages(query, yearStart, yearEnd);
                        break;
                    default:
                        rawData = new ArrayList<>();
                        Platform.runLater(() -> NotificationManager.showWarning("API Desconocida", "El endpoint seleccionado no tiene una lógica de carga implementada."));
                        break;
                }
                
                List<?> filteredData = ("Biblioteca NASA".equals(filterName) || query.isEmpty()) ? rawData : filterData(rawData, query, yearStart, yearEnd);
                
                List<Node> cards = new ArrayList<>();
                for(Object item : filteredData) cards.add(createCardNode(item));
                updateGrid(cards);
            } catch (IOException e) {
                handleApiError(filterName, e);
            }
        });
    }
    
    private List<?> filterData(List<?> data, String query, int yearStart, int yearEnd) {
        if (query.isEmpty()) {
            return data;
        }
        String lowerCaseQuery = query.toLowerCase();
        return data.stream()
            .filter(item -> {
                if (item instanceof Rocket rocket) {
                    return rocket.getName().toLowerCase().contains(lowerCaseQuery) ||
                           rocket.getDescription().toLowerCase().contains(lowerCaseQuery);
                }
                if (item instanceof MoonPhaseResponse moonPhaseResponse) {
                    // No hay texto para filtrar directamente en MoonPhaseResponse
                    return true;
                }
                // NasaItem ya se filtra en la llamada a la API, no aquí.
                return false;
            })
            .collect(Collectors.toList());
    }

    private void showLoadingSkeletons() {
        contentContainer.getChildren().clear();
        for (int i = 0; i < 9; i++) {
            try {
                Node card = FXMLLoader.load(getClass().getResource("/com/example/explorandoelcosmos/loading-card-view.fxml"));
                FadeTransition ft = new FadeTransition(Duration.seconds(0.8), card);
                ft.setFromValue(1.0);
                ft.setToValue(0.6);
                ft.setAutoReverse(true);
                ft.setCycleCount(FadeTransition.INDEFINITE);
                ft.play();
                int row = i / 3;
                int col = i % 3;
                contentContainer.add(card, col, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGrid(List<Node> cards) {
        Platform.runLater(() -> {
            contentContainer.getChildren().clear();
            if (cards.isEmpty()) {
                NotificationManager.showInfo("Sin Resultados", "No se encontraron elementos que coincidan con la búsqueda.");
            } else {
                int columns = 3;
                for (int i = 0; i < cards.size(); i++) {
                    int row = i / columns;
                    int col = i % columns;
                    contentContainer.add(cards.get(i), col, row);
                }
            }
        });
    }

    private void setActiveFilter(Label selectedButton) {
        // Este método ya no se usa para los botones de API, pero se mantiene para otros filtros si los hubiera
        // if (activeFilter != null) {
        //     activeFilter.getStyleClass().remove("celestial-button-active");
        // }
        // if (selectedButton != null) {
        //     selectedButton.getStyleClass().add("celestial-button-active");
        //     activeFilter = selectedButton;
        // }
    }

    private void loadInThread(Runnable task) {
        new Thread(task).start();
    }

    private void handleApiError(String apiName, IOException e) {
        System.err.println("Error al obtener datos de " + apiName + ": " + e.getMessage());
        e.printStackTrace();
        Platform.runLater(() -> {
            contentContainer.getChildren().clear();
            NotificationManager.showError("Error de Red", "No se pudieron cargar los datos de " + apiName + ". " + e.getMessage());
        });
    }

    public void handleCerrarSesion(ActionEvent actionEvent) {}
    public void handleEditarPerfil(ActionEvent actionEvent) {}
    public void handleAcercaDe(ActionEvent actionEvent) {}
    public void handleFilterFavoritos(MouseEvent mouseEvent) { /* TODO */ }

    @FXML
    private void handleReportsClick(MouseEvent event) {
        if (activeEndpointName == null || activeEndpointName.isEmpty()) {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona una API para generar el reporte.");
            return;
        }

        String reportName = activeEndpointName; // Usamos el nombre del endpoint activo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.setInitialFileName("Reporte_" + reportName.replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(rootStackPane.getScene().getWindow());

        if (file == null) {
            return;
        }

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
            NotificationManager.showSuccess("Éxito", "El reporte se ha guardado correctamente en:\n" + file.getAbsolutePath());
        });

        reportTask.setOnFailed(e -> {
            progressBox.setVisible(false);
            progressBox.setManaged(false);
            Throwable ex = reportTask.getException();
            NotificationManager.showError("Error", "No se pudo generar el reporte: " + ex.getMessage());
            ex.printStackTrace();
        });

        progressLabel.textProperty().bind(reportTask.messageProperty());
        progressBar.progressProperty().bind(reportTask.progressProperty());
        progressBox.setVisible(true);
        progressBox.setManaged(true);

        new Thread(reportTask).start();
    }

    private List<?> fetchDataForReport(String endpointName) throws IOException {
        return switch (endpointName) {
            case "SpaceX" -> spaceXService.getRockets();
            case "Foto del Día" -> {
                MoonPhaseResponse moonPhase = astronomyApiService.getMoonPhase(LocalDate.now());
                List<MoonPhaseResponse> moonPhases = new ArrayList<>();
                if (moonPhase != null) moonPhases.add(moonPhase);
                yield moonPhases;
            }
            case "Biblioteca NASA" -> nasaLibraryService.searchImages("", 0, 0); // Sin query ni años por defecto para el reporte
            default -> new ArrayList<>();
        };
    }
}
