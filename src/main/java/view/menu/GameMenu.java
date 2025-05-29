package view.menu;

import controller.GameViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import view.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;

public class GameMenu extends Application {
    @FXML
    public static Pane gamePane;
    public static boolean isMute = false;
    public static boolean isPlay = true;
    public static Scene scene;
    public static MediaPlayer songPlayer;
    public static Label timer;

    @Override
    public void start(Stage stage) throws Exception {
        URL gameMenuFXMLUrl = GameMenu.class.getResource(Paths.GAME_MENU_FXML_FILE.getPath());
        Pane pane = FXMLLoader.load(gameMenuFXMLUrl);
        if (GameViewController.isBlackWhiteThemeOn) {
            pane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            pane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        gamePane = pane;
        Rectangle mute_unmuteIcon = GameViewController.createMuteUnmuteIcon();
        pane.getChildren().add(mute_unmuteIcon);

        gamePane.getChildren().add(GameViewController.createPlayPauseIcon());


        Scene gameMenuScene = new Scene(pane);
        scene = gameMenuScene;
        stage.setScene(gameMenuScene);
        stage.show();
    }

    public static void playMusic() {
        Media media = new Media(new File(Paths.MUSICS_PATH.getPath() + "2.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(-1);
        if (GameMenu.isMute)
            mediaPlayer.setMute(true);
        songPlayer = mediaPlayer;
    }

    public static void playSFX() {
        if (!GameViewController.isSFXOn) return;
        Media media = new Media(new File(Paths.MUSICS_PATH.getPath() + "clickSound.wav")
                .toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(1);
        if (GameMenu.isMute)
            mediaPlayer.setMute(true);
        mediaPlayer.play();
    }

    public static void playDamage() {
        if (!GameViewController.isSFXOn) return;
        Media media = new Media(new File(Paths.MUSICS_PATH.getPath() + "Blood_Splash_Quick_01.wav")
                .toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(1);
        if (GameMenu.isMute)
            mediaPlayer.setMute(true);
        mediaPlayer.play();
    }
    @FXML
    public void initialize() {

    }



}
