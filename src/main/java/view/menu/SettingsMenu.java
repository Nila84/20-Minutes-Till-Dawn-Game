package view.menu;

import controller.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Paths;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Application {
    @FXML
    private TextField downKeyField;
    @FXML
    private TextField leftKeyField;
    @FXML
    private TextField rightKeyField;
    @FXML
    private TextField upKeyField;
    @FXML
    public static BorderPane settingsPane;

    @Override
    public void start(Stage stage) throws Exception {
        URL settingsMenuFXMLUrl = SettingsMenu.class.getResource(Paths.SETTINGS_MENU_FXML_FILE.getPath());
        FXMLLoader loader = new FXMLLoader(settingsMenuFXMLUrl);
        BorderPane borderPane = loader.load();
        settingsPane = borderPane;
        settingsPane.getChildren().add(GameViewController.createMuteUnmuteIcon());
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass()
                    .getResource(Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass()
                    .getResource(Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        Scene settingsMenuScene = new Scene(borderPane);
        stage.setScene(settingsMenuScene);
        stage.show();
    }

    @FXML
    public void toggleSFX(MouseEvent event) {
        GameViewController.isSFXOn = !GameViewController.isSFXOn;
    }


    public void changeTheme() {
        GameViewController.isBlackWhiteThemeOn = !GameViewController.isBlackWhiteThemeOn;
        if (GameViewController.isBlackWhiteThemeOn) {
            settingsPane.getStylesheets().remove(getClass().getResource
                    (Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            settingsPane.getStylesheets().add(getClass().getResource
                    (Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        } else {
            settingsPane.getStylesheets().add(getClass().getResource
                    (Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            settingsPane.getStylesheets().remove(getClass().getResource
                    (Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
    }

    public void back() {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("an error occurred");
        }
    }

    @FXML
    public void nextSong(MouseEvent mouseEvent) {
        GameViewController.changeToNextMusic();
    }

    @FXML
    public void saveKeys(MouseEvent mouseEvent) {
        try {
            String leftKey = leftKeyField.getText().toUpperCase();
            String rightKey = rightKeyField.getText().toUpperCase();
            String downKey = downKeyField.getText().toUpperCase();
            String upKey = upKeyField.getText().toUpperCase();

            if (leftKey.isEmpty() || rightKey.isEmpty() || upKey.isEmpty() || downKey.isEmpty()) {
                GameViewController.alertShowing(Alert.AlertType.ERROR, "Change keys failed!",
                        "Change keys failed!", "Keys cannot be empty!");
                return;
            }

            // Validate each key
            try {
                KeyCode.valueOf(upKey);
                KeyCode.valueOf(downKey);
                KeyCode.valueOf(leftKey);
                KeyCode.valueOf(rightKey);
            } catch (IllegalArgumentException e) {
                GameViewController.alertShowing(Alert.AlertType.ERROR, "Invalid Key",
                        "Invalid Key Entered", "One or more keys are not valid. Please use standard key codes.");
                return;
            }

            // If validation passes, set the key bindings
            GameViewController.setKeyBinding("UP", KeyCode.valueOf(upKey));
            GameViewController.setKeyBinding("DOWN", KeyCode.valueOf(downKey));
            GameViewController.setKeyBinding("LEFT", KeyCode.valueOf(leftKey));
            GameViewController.setKeyBinding("RIGHT", KeyCode.valueOf(rightKey));

            GameViewController.alertShowing(Alert.AlertType.INFORMATION, "Success",
                    "Keys Saved", "Key bindings have been updated successfully.");

        } catch (Exception e) {
            GameViewController.alertShowing(Alert.AlertType.ERROR, "Error",
                    "Failed to save keys", "An unexpected error occurred: " + e.getMessage());
        }
    }

    public void autoReload(MouseEvent mouseEvent) {
        GameViewController.autoReload = !GameViewController.autoReload;
    }
}
