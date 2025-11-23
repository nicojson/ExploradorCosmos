package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;
import com.example.explorandoelcosmos.dao.UserDAO;
import com.example.explorandoelcosmos.dao.UserDAOImpl;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;
import com.example.explorandoelcosmos.model.User;
import com.example.explorandoelcosmos.service.NavigationService;
import com.example.explorandoelcosmos.service.NotificationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminController {

    // === FXML Fields para Gestión de API Keys ===
    @FXML private ComboBox<ApiEndpointConfig> endpointComboBox;
    @FXML private TextField endpointNameField;
    @FXML private TextField apiKeyField;
    @FXML private TextField appIdField;
    @FXML private TextField appSecretField;

    // === FXML Fields para Gestión de Usuarios ===
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private StackPane userTablePane; // Para el indicador de carga de usuarios

    private final ProgressIndicator loadingIndicator = new ProgressIndicator();

    private final AppConfigDAO appConfigDAO = new AppConfigDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();

    // Campo para almacenar un mensaje de estado o depuración
    private String currentMessage = "AdminController inicializado.";

    @FXML
    public void initialize() {
        setupLoadingIndicator();
        setupUserTable();
        loadUsers();
        setupEndpointComboBox();

        roleComboBox.setItems(FXCollections.observableArrayList("user", "admin"));
        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                roleComboBox.setValue(newSelection.getRole());
            }
        });

        // Inicializar la gestión de API Keys
        loadEndpointConfigs();
        endpointComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayEndpointConfig(newVal);
            }
        });
    }

    private void setupEndpointComboBox() {
        endpointComboBox.setConverter(new StringConverter<ApiEndpointConfig>() {
            @Override
            public String toString(ApiEndpointConfig object) {
                return object != null ? object.getEndpointName() : "";
            }

            @Override
            public ApiEndpointConfig fromString(String string) {
                return endpointComboBox.getItems().stream()
                        .filter(api -> api.getEndpointName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void setupLoadingIndicator() {
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setVisible(false);
        if (userTablePane != null) {
            userTablePane.getChildren().add(loadingIndicator);
        }
    }

    private void showLoading(boolean show) {
        if (userTablePane != null) {
            loadingIndicator.setVisible(show);
            userTableView.setDisable(show);
            userTableView.setOpacity(show ? 0.5 : 1.0);
        }
    }

    // === Métodos para Gestión de API Keys ===

    private void loadEndpointConfigs() {
        Task<List<ApiEndpointConfig>> task = new Task<>() {
            @Override
            protected List<ApiEndpointConfig> call() {
                return appConfigDAO.findAllEndpointConfigs();
            }
        };
        task.setOnSucceeded(e -> {
            ObservableList<ApiEndpointConfig> configs = FXCollections.observableArrayList(task.getValue());
            endpointComboBox.setItems(configs);
            if (!configs.isEmpty()) {
                endpointComboBox.getSelectionModel().selectFirst();
            } else {
                clearEndpointFields();
            }
        });
        task.setOnFailed(e -> {
            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido al cargar configuraciones.";
            NotificationManager.showError("Error", "No se pudieron cargar las configuraciones de endpoints: " + errorMessage);
            currentMessage = "Error al cargar configuraciones de endpoints: " + errorMessage;
        });
        new Thread(task).start();
    }

    @FXML
    private void handleNewEndpoint() {
        clearEndpointFields();
        endpointNameField.setDisable(false); // Permitir editar el nombre para un nuevo endpoint
        NotificationManager.showInfo("Nuevo Endpoint", "Introduce los detalles para el nuevo endpoint.");
        currentMessage = "Creando nuevo endpoint."; // Actualizar mensaje
    }

    @FXML
    private void handleLoadEndpoint() {
        ApiEndpointConfig selectedConfig = endpointComboBox.getSelectionModel().getSelectedItem();
        if (selectedConfig != null) {
            displayEndpointConfig(selectedConfig);
            currentMessage = "Endpoint '" + selectedConfig.getEndpointName() + "' cargado."; // Actualizar mensaje
        } else {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona un endpoint para cargar.");
            currentMessage = "Error: No se seleccionó endpoint para cargar."; // Actualizar mensaje
        }
    }

    private void displayEndpointConfig(ApiEndpointConfig config) {
        endpointNameField.setText(config.getEndpointName());
        apiKeyField.setText(config.getApiKey());
        appIdField.setText(config.getAppId());
        appSecretField.setText(config.getAppSecret());
        endpointNameField.setDisable(true); // No permitir cambiar el nombre de un endpoint existente
    }

    private void clearEndpointFields() {
        endpointNameField.setText("");
        apiKeyField.setText("");
        appIdField.setText("");
        appSecretField.setText("");
        endpointNameField.setDisable(false);
        currentMessage = "Campos de endpoint limpiados."; // Actualizar mensaje
    }

    @FXML
    private void handleSaveEndpoint() {
        String name = endpointNameField.getText().trim();
        String apiKey = apiKeyField.getText().trim();
        String appId = appIdField.getText().trim();
        String appSecret = appSecretField.getText().trim();

        if (name.isEmpty()) {
            NotificationManager.showWarning("Datos Incompletos", "El nombre del endpoint no puede estar vacío.");
            currentMessage = "Error: Nombre de endpoint vacío.";
            return;
        }

        ApiEndpointConfig config = new ApiEndpointConfig(
            name,
            apiKey.isEmpty() ? null : apiKey,
            appId.isEmpty() ? null : appId,
            appSecret.isEmpty() ? null : appSecret
        );

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                appConfigDAO.saveEndpointConfig(config);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            NotificationManager.showSuccess("Éxito", "Configuración del endpoint '" + name + "' guardada.");
            loadEndpointConfigs(); // Recargar la lista de endpoints
            currentMessage = "Endpoint '" + name + "' guardado exitosamente."; // Actualizar mensaje
        });
        task.setOnFailed(e -> {
            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido al guardar.";
            NotificationManager.showError("Error", "No se pudo guardar la configuración del endpoint: " + errorMessage);
            currentMessage = "Error al guardar endpoint '" + name + "': " + errorMessage; // Actualizar mensaje
        });
        new Thread(task).start();
    }

    @FXML
    private void handleDeleteEndpoint() {
        ApiEndpointConfig selectedConfig = endpointComboBox.getSelectionModel().getSelectedItem();
        if (selectedConfig == null) {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona un endpoint para eliminar.");
            currentMessage = "Error: No se seleccionó endpoint para eliminar."; // Actualizar mensaje
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que quieres eliminar el endpoint '" + selectedConfig.getEndpointName() + "'?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() {
                        appConfigDAO.deleteEndpointConfig(selectedConfig.getEndpointName());
                        return null;
                    }
                };
                task.setOnSucceeded(e -> {
                    NotificationManager.showInfo("Eliminado", "Endpoint '" + selectedConfig.getEndpointName() + "' eliminado.");
                    loadEndpointConfigs(); // Recargar la lista de endpoints
                    clearEndpointFields();
                    currentMessage = "Endpoint '" + selectedConfig.getEndpointName() + "' eliminado exitosamente."; // Actualizar mensaje
                });
                task.setOnFailed(e -> {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido al eliminar.";
                    NotificationManager.showError("Error", "No se pudo eliminar el endpoint: " + errorMessage);
                    currentMessage = "Error al eliminar endpoint '" + selectedConfig.getEndpointName() + "': " + errorMessage; // Actualizar mensaje
                });
                new Thread(task).start();
            } else {
                currentMessage = "Eliminación de endpoint cancelada."; // Actualizar mensaje
            }
        });
    }

    // === Métodos para Gestión de Usuarios ===

    private void setupUserTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void loadUsers() {
        showLoading(true);
        Task<List<User>> loadUsersTask = new Task<>() {
            @Override
            protected List<User> call() {
                return userDAO.findAll();
            }
        };

        loadUsersTask.setOnSucceeded(event -> {
            userTableView.setItems(FXCollections.observableArrayList(loadUsersTask.getValue()));
            showLoading(false);
        });

        loadUsersTask.setOnFailed(event -> {
            showLoading(false);
            String errorMessage = loadUsersTask.getException() != null ? loadUsersTask.getException().getMessage() : "Error desconocido al cargar usuarios.";
            NotificationManager.showError("Error", "No se pudieron cargar los usuarios: " + errorMessage);
            currentMessage = "Error al cargar usuarios: " + errorMessage;
        });

        new Thread(loadUsersTask).start();
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            NotificationManager.showWarning("Sin Selección", "Por favor, selecciona un usuario para eliminar.");
            return;
        }
        if (selectedUser.isAdmin()) {
            NotificationManager.showError("Acción no permitida", "No se puede eliminar a un usuario administrador.");
            return;
        }

        Task<Void> deleteTask = new Task<>() {
            @Override
            protected Void call() {
                userDAO.delete(selectedUser.getId());
                return null;
            }
        };

        deleteTask.setOnSucceeded(event -> {
            NotificationManager.showInfo("Usuario Eliminado", "El usuario " + selectedUser.getUsername() + " ha sido eliminado.");
            loadUsers(); // Recargar usuarios de forma asíncrona
        });

        deleteTask.setOnFailed(event -> {
            String errorMessage = deleteTask.getException() != null ? deleteTask.getException().getMessage() : "Error desconocido al eliminar usuario.";
            NotificationManager.showError("Error", "No se pudo eliminar el usuario: " + errorMessage);
            currentMessage = "Error al eliminar usuario: " + errorMessage;
        });
        new Thread(deleteTask).start();
    }

    @FXML
    private void handleUpdateUserRole() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        String newRole = roleComboBox.getValue();

        if (selectedUser == null || newRole == null) {
            NotificationManager.showWarning("Error", "Por favor, selecciona un usuario y un rol.");
            return;
        }

        User updatedUser = new User(selectedUser.getId(), selectedUser.getUsername(), selectedUser.getHashedPassword(), selectedUser.getEmail(), newRole);

        Task<Void> updateTask = new Task<>() {
            @Override
            protected Void call() {
                userDAO.update(updatedUser);
                return null;
            }
        };

        updateTask.setOnSucceeded(event -> {
            NotificationManager.showSuccess("Éxito", "El rol del usuario ha sido actualizado.");
            loadUsers(); // Recargar usuarios de forma asíncrona
        });

        updateTask.setOnFailed(event -> {
            String errorMessage = updateTask.getException() != null ? updateTask.getException().getMessage() : "Error desconocido al actualizar rol.";
            NotificationManager.showError("Error", "No se pudo actualizar el rol del usuario: " + errorMessage);
            currentMessage = "Error al actualizar rol: " + errorMessage;
        });
        new Thread(updateTask).start();
    }

    @FXML
    private void handleGoToMainApp(ActionEvent event) {
        Task<User> findAdminTask = new Task<>() {
            @Override
            protected User call() {
                return userDAO.findByUsername("admin").orElse(null);
            }
        };

        findAdminTask.setOnSucceeded(e -> {
            NavigationService.navigateTo(event, "/com/example/explorandoelcosmos/mainProgram-view.fxml", "Explorando el Cosmos", findAdminTask.getValue());
        });

        findAdminTask.setOnFailed(e -> {
            String errorMessage = findAdminTask.getException() != null ? findAdminTask.getException().getMessage() : "Error desconocido al navegar.";
            NotificationManager.showError("Error", "No se pudo iniciar la navegación: " + errorMessage);
            currentMessage = "Error al navegar: " + errorMessage;
        });
        new Thread(findAdminTask).start();
    }

    // Nuevo método getMessage()
    public String getMessage() {
        return currentMessage;
    }
}
