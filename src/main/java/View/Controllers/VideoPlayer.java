package View.Controllers;

import View.Main;
import com.jfoenix.controls.JFXButton;
import controler.SellerController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.media.MediaPlayer.Status;

import java.io.IOException;

public class VideoPlayer {
    public JFXButton close;
    public JFXButton play;
    public JFXButton pause;

    private static int id;
    public MediaView player;
    private MediaPlayer mediaPlayer;

    public static void playVideoFor(int id) {
        VideoPlayer.id = id;
        try {
            Scene scene = new Scene(Main.loadFXML("VideoPlayer"));
            Main.setSceneToStage(new Stage(StageStyle.UNDECORATED), scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        Media media = SellerController.getInstance().getViedo(id);
        mediaPlayer = new MediaPlayer(media);
        player = new MediaView(mediaPlayer);
        player.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        buttonInitialize();
    }

    private void buttonInitialize() {
        play.setOnMouseClicked(event -> {
            Status status = mediaPlayer.getStatus();
            if (status.equals(Status.STOPPED) | status.equals(Status.PAUSED)) {
                mediaPlayer.play();
            }
        });
        pause.setOnMouseClicked(event -> {
            Status status = mediaPlayer.getStatus();
            if (status.equals(Status.PLAYING)) {
                mediaPlayer.pause();
            }
        });
        close.setOnMouseClicked(event -> {
            mediaPlayer.stop();
            ((Stage) close.getScene().getWindow()).close();
        });
    }
}
