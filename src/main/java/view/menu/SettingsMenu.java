package view.menu;

import controller.GameController;
import controller.GameViewController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.Paths;
import view.enums.SettingsMenuText;
import view.util.CustomCursor;

public class SettingsMenu extends Application {
    private TextField upKeyField;
    private TextField downKeyField;
    private TextField leftKeyField;
    private TextField rightKeyField;
    private Button saveKeysButton;
    private Button changeThemeButton;
    private Button toggleSFXButton;
    private Button nextSongButton;
    private Button autoReloadButton;
    private Button changeLanguageButton;
    private Button backButton;

    public static BorderPane settingsPane;
    private boolean isEnglish = GameController.language;

    @Override
    public void start(Stage stage) throws Exception {
        createUIComponents();

        settingsPane = new BorderPane();
        settingsPane.setCenter(createSettingsCenter());
        settingsPane.getChildren().add(GameViewController.createMuteUnmuteIcon());
        settingsPane.getStyleClass().add("Background");

        applyTheme();

        Scene settingsMenuScene = new Scene(settingsPane, 700, 700);
        CustomCursor.setGameCursor(settingsMenuScene);
        stage.setScene(settingsMenuScene);
        stage.setTitle(getText(SettingsMenuText.SETTINGS_TITLE_EN));
        stage.show();
    }

    private void createUIComponents() {
        upKeyField = new TextField(GameViewController.getKeyBinding("UP").toString());
        downKeyField = new TextField(GameViewController.getKeyBinding("DOWN").toString());
        leftKeyField = new TextField(GameViewController.getKeyBinding("LEFT").toString());
        rightKeyField = new TextField(GameViewController.getKeyBinding("RIGHT").toString());

        saveKeysButton = new Button(getText(SettingsMenuText.SAVE_KEYS_EN));
        saveKeysButton.setOnAction(e -> saveKeys(null));

        changeThemeButton = new Button(getText(SettingsMenuText.CHANGE_THEME_EN));
        changeThemeButton.setOnAction(e -> changeTheme());

        toggleSFXButton = new Button(getText(SettingsMenuText.TOGGLE_SFX_EN));
        toggleSFXButton.setOnAction(e -> toggleSFX(null));

        nextSongButton = new Button(getText(SettingsMenuText.NEXT_SONG_EN));
        nextSongButton.setOnAction(e -> nextSong(null));

        autoReloadButton = new Button(getText(SettingsMenuText.AUTO_RELOAD_EN));
        autoReloadButton.setOnAction(e -> autoReload(null));

        changeLanguageButton = new Button(getText(SettingsMenuText.CHANGE_LANGUAGE_EN));
        changeLanguageButton.setOnAction(e -> changeLanguage(null));

        backButton = new Button(getText(SettingsMenuText.BACK_EN));
        backButton.setOnAction(e -> back());
    }

    private VBox createSettingsCenter() {
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        GridPane keyBindingsGrid = new GridPane();
        keyBindingsGrid.setHgap(10);
        keyBindingsGrid.setVgap(10);
        keyBindingsGrid.setAlignment(Pos.CENTER);

        keyBindingsGrid.add(new Label(getText(SettingsMenuText.UP_KEY_EN)), 0, 0);
        keyBindingsGrid.add(upKeyField, 1, 0);
        keyBindingsGrid.add(new Label(getText(SettingsMenuText.DOWN_KEY_EN)), 0, 1);
        keyBindingsGrid.add(downKeyField, 1, 1);
        keyBindingsGrid.add(new Label(getText(SettingsMenuText.LEFT_KEY_EN)), 0, 2);
        keyBindingsGrid.add(leftKeyField, 1, 2);
        keyBindingsGrid.add(new Label(getText(SettingsMenuText.RIGHT_KEY_EN)), 0, 3);
        keyBindingsGrid.add(rightKeyField, 1, 3);

        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(
                saveKeysButton,
                changeThemeButton,
                toggleSFXButton,
                nextSongButton,
                autoReloadButton,
                changeLanguageButton,
                backButton
        );

        centerBox.getChildren().addAll(keyBindingsGrid, buttonsBox);
        return centerBox;
    }

    private void applyTheme() {
        if (GameViewController.isBlackWhiteThemeOn) {
            settingsPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            settingsPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        } else {
            settingsPane.getStylesheets().add(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
        }
    }

    private String getText(SettingsMenuText textType) {
        return SettingsMenuText.getText(textType, isEnglish);
    }

    public void toggleSFX(MouseEvent event) {
        GameViewController.isSFXOn = !GameViewController.isSFXOn;
        refreshUI();
    }

    public void changeTheme() {
        GameViewController.isBlackWhiteThemeOn = !GameViewController.isBlackWhiteThemeOn;
        applyTheme();
        refreshUI();
    }

    public void back() {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("an error occurred");
        }
    }

    public void nextSong(MouseEvent mouseEvent) {
        GameViewController.changeToNextMusic();
    }

    public void saveKeys(MouseEvent mouseEvent) {
        try {
            String leftKey = leftKeyField.getText().toUpperCase();
            String rightKey = rightKeyField.getText().toUpperCase();
            String downKey = downKeyField.getText().toUpperCase();
            String upKey = upKeyField.getText().toUpperCase();

            if (leftKey.isEmpty() || rightKey.isEmpty() || upKey.isEmpty() || downKey.isEmpty()) {
                showAlert(
                        getText(SettingsMenuText.EMPTY_KEYS_EN),
                        getText(SettingsMenuText.EMPTY_KEYS_MSG_EN),
                        Alert.AlertType.ERROR
                );
                return;
            }

            try {
                KeyCode.valueOf(upKey);
                KeyCode.valueOf(downKey);
                KeyCode.valueOf(leftKey);
                KeyCode.valueOf(rightKey);
            } catch (IllegalArgumentException e) {
                showAlert(
                        getText(SettingsMenuText.INVALID_KEY_EN),
                        getText(SettingsMenuText.INVALID_KEY_MSG_EN),
                        Alert.AlertType.ERROR
                );
                return;
            }

            GameViewController.setKeyBinding("UP", KeyCode.valueOf(upKey));
            GameViewController.setKeyBinding("DOWN", KeyCode.valueOf(downKey));
            GameViewController.setKeyBinding("LEFT", KeyCode.valueOf(leftKey));
            GameViewController.setKeyBinding("RIGHT", KeyCode.valueOf(rightKey));

            showAlert(
                    getText(SettingsMenuText.KEYS_SAVED_EN),
                    getText(SettingsMenuText.KEYS_SAVED_MSG_EN),
                    Alert.AlertType.INFORMATION
            );

        } catch (Exception e) {
            showAlert("Error", "Failed to save keys: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void autoReload(MouseEvent mouseEvent) {
        GameViewController.autoReload = !GameViewController.autoReload;
        refreshUI();
    }

    private void refreshUI() {
        try {
            new SettingsMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeLanguage(MouseEvent mouseEvent) {
        GameController.language = !GameController.language;
        refreshUI();
    }
}