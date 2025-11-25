package com.example.explorandoelcosmos.controllers;

import com.example.explorandoelcosmos.model.Publication;
import com.example.explorandoelcosmos.model.NasaAsset;
import com.example.explorandoelcosmos.service.NasaApiClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import retrofit2.Response;

import java.io.IOException;
import java.util.Comparator;

public class DetailedCardController {

    @FXML
    private StackPane mediaContainer;
    @FXML
    private Pane imagePane;
    @FXML
    private MediaView mediaView;
    @FXML
    private Button playButton;
    @FXML
    private ProgressIndicator loadingIndicator;

    // Video Controls
    @FXML
    private VBox videoControlsPanel;
    @FXML
    private Button playPauseButton;
    @FXML
    private Slider progressSlider;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button muteButton;
    @FXML
    private Label speedLabel;
    @FXML
    private Button fullscreenButton;

    @FXML
    private Label titleLabel;
    @FXML
    private Label detailsLabel;
    @FXML
    private HBox actions;
    @FXML
    private ActionsController actionsController;

    private MainProgramController mainController;
    private MediaPlayer mediaPlayer;
    private Publication publication;
    private boolean isPlaying = false;
    private boolean isMuted = false;
    private boolean isUpdatingSlider = false;
    private double currentSpeed = 1.0;

    public void setMainController(MainProgramController mainController) {
        this.mainController = mainController;
    }

    public void setData(String imageUrl, String title, String details) {
        setData(imageUrl, title, details, null);
    }

    public void setData(String imageUrl, String title, String details, Publication publication) {
        this.publication = publication;
        String style = String.format("-fx-background-image: url('%s');", imageUrl);
        imagePane.setStyle(style);
        titleLabel.setText(title);
        detailsLabel.setText(details);

        // Show play button only for videos
        if (publication != null && "video".equalsIgnoreCase(publication.getContentType())) {
            playButton.setVisible(true);
        }

        actionsController.setData(title, imageUrl);

        // Setup volume slider
        setupVolumeControl();
    }

    private void setupVolumeControl() {
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
                updateMuteButtonIcon();
            }
        });
    }

    @FXML
    private void handlePlayClick() {
        playButton.setVisible(false);
        loadingIndicator.setVisible(true);
        loadingIndicator.setManaged(true);

        new Thread(() -> {
            try {
                fetchVideoUrl();
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    playButton.setVisible(true);
                });
            }
        }).start();
    }

    private void fetchVideoUrl() throws IOException {
        if (publication == null)
            return;

        String nasaId = publication.getOriginalIdFromApi();
        if (nasaId == null || nasaId.isEmpty()) {
            return;
        }

        Response<NasaAsset> response = NasaApiClient.getNasaLibraryService().getAsset(nasaId).execute();
        if (response.isSuccessful() && response.body() != null) {
            NasaAsset asset = response.body();
            String videoUrl = asset.getCollection().getItems().stream()
                    .map(item -> item.getHref())
                    .filter(href -> href.endsWith(".mp4"))
                    .sorted(Comparator.comparingInt((String s) -> {
                        if (s.contains("~medium"))
                            return 0;
                        if (s.contains("~orig"))
                            return 1;
                        return 2;
                    }))
                    .findFirst()
                    .orElse(null);

            if (videoUrl != null) {
                if (videoUrl.startsWith("http:")) {
                    videoUrl = videoUrl.replace("http:", "https:");
                }
                String finalUrl = videoUrl;
                Platform.runLater(() -> playVideo(finalUrl));
            } else {
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    playButton.setVisible(true);
                    System.err.println("No MP4 found for: " + nasaId);
                });
            }
        }
    }

    private void playVideo(String url) {
        try {
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            loadingIndicator.setVisible(false);
            loadingIndicator.setManaged(false);

            imagePane.setVisible(false);
            mediaView.setVisible(true);
            mediaView.setManaged(true);

            // Show video controls
            videoControlsPanel.setVisible(true);
            videoControlsPanel.setManaged(true);

            // Setup media player listeners
            setupMediaPlayerListeners();

            // Set initial volume
            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);

            mediaPlayer.play();
            isPlaying = true;
            updatePlayPauseButton();

        } catch (Exception e) {
            e.printStackTrace();
            loadingIndicator.setVisible(false);
            playButton.setVisible(true);
        }
    }

    private void setupMediaPlayerListeners() {
        // Update progress slider and time labels as video plays
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!isUpdatingSlider && mediaPlayer.getTotalDuration() != null) {
                double current = newTime.toSeconds();
                double total = mediaPlayer.getTotalDuration().toSeconds();
                progressSlider.setValue((current / total) * 100);
                currentTimeLabel.setText(formatTime(current));
            }
        });

        // Update total time when media is ready
        mediaPlayer.setOnReady(() -> {
            double total = mediaPlayer.getTotalDuration().toSeconds();
            totalTimeLabel.setText(formatTime(total));
            progressSlider.setMax(100);
        });

        // Handle video end
        mediaPlayer.setOnEndOfMedia(() -> {
            isPlaying = false;
            updatePlayPauseButton();
            progressSlider.setValue(0);
            currentTimeLabel.setText("0:00");
        });

        // Progress slider drag handling
        progressSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                if (progressSlider.isValueChanging()) {
                    isUpdatingSlider = true;
                    double seekTime = (newVal.doubleValue() / 100.0) * mediaPlayer.getTotalDuration().toSeconds();
                    currentTimeLabel.setText(formatTime(seekTime));
                }
            }
        });

        progressSlider.setOnMouseReleased(event -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double seekTime = (progressSlider.getValue() / 100.0) * mediaPlayer.getTotalDuration().toSeconds();
                mediaPlayer.seek(Duration.seconds(seekTime));
                isUpdatingSlider = false;
            }
        });
    }

    @FXML
    private void handlePlayPauseClick() {
        if (mediaPlayer == null)
            return;

        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            mediaPlayer.play();
            isPlaying = true;
        }
        updatePlayPauseButton();
    }

    @FXML
    private void handleMuteToggle() {
        if (mediaPlayer == null)
            return;

        isMuted = !isMuted;
        mediaPlayer.setMute(isMuted);
        updateMuteButtonIcon();
    }

    @FXML
    private void handleFullscreenClick() {
        try {
            Stage stage = (Stage) mediaContainer.getScene().getWindow();
            if (stage != null) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        } catch (Exception e) {
            System.err.println("Error toggling fullscreen: " + e.getMessage());
        }
    }

    private void updatePlayPauseButton() {
        playPauseButton.setText(isPlaying ? "⏸" : "▶");
    }

    private void updateMuteButtonIcon() {
        if (isMuted || volumeSlider.getValue() == 0) {
            muteButton.setText("🔇");
        } else if (volumeSlider.getValue() < 50) {
            muteButton.setText("🔉");
        } else {
            muteButton.setText("🔊");
        }
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    public void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        mediaView.setVisible(false);
        mediaView.setManaged(false);
        imagePane.setVisible(true);
        playButton.setVisible(true);
        videoControlsPanel.setVisible(false);
        videoControlsPanel.setManaged(false);
        isPlaying = false;
    }

    @FXML
    private void handleCloseClick() {
        stopVideo();
        if (mainController != null) {
            mainController.hideDetailedView();
        }
    }
}
