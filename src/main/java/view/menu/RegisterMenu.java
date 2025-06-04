package view.menu;

import controller.GameController;
import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Paths;
import view.enums.RegisterMenuText;

public class RegisterMenu extends Application {
    // UI Components
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private TextField favoriteColorField = new TextField();
    private Button registerButton = new Button();
    private Button backButton = new Button();

    private boolean isEnglish = GameController.language; // Default to English

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = createRegisterPane();
        borderPane.getStyleClass().add("Background");
        applyTheme(borderPane);

        Scene registerMenuScene = new Scene(borderPane,700,700);
        stage.setTitle(getText(RegisterMenuText.REGISTER_TITLE));
        stage.setScene(registerMenuScene);
        stage.show();
    }

    private BorderPane createRegisterPane() {
        BorderPane borderPane = new BorderPane();

        // Setup text fields
        setupTextField(usernameField, getText(RegisterMenuText.USERNAME_PROMPT));
        setupTextField(passwordField, getText(RegisterMenuText.PASSWORD_PROMPT));
        setupTextField(favoriteColorField, getText(RegisterMenuText.COLOR_PROMPT));

        // Setup buttons
        registerButton.setText(getText(RegisterMenuText.REGISTER_BUTTON));
        registerButton.setOnAction(e -> register(null));

        backButton.setText(getText(RegisterMenuText.BACK_BUTTON));
        backButton.setOnAction(e -> back(null));

        // Create layout
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(
                createLabeledField(getText(RegisterMenuText.USERNAME_LABEL), usernameField),
                createLabeledField(getText(RegisterMenuText.PASSWORD_LABEL), passwordField),
                createLabeledField(getText(RegisterMenuText.COLOR_LABEL), favoriteColorField),
                registerButton,
                backButton
        );

        borderPane.setCenter(centerBox);
        return borderPane;
    }

    private HBox createLabeledField(String labelText, TextField field) {
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        Label label = new Label(labelText);
        hbox.getChildren().addAll(label, field);
        return hbox;
    }

    private void setupTextField(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setPrefWidth(200);
    }

    private void applyTheme(BorderPane pane) {
        if (GameViewController.isBlackWhiteThemeOn) {
            pane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            pane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        } else {
            pane.getStylesheets().add(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
        }
    }

    public void register(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String favoriteColor = favoriteColorField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showAlert(
                    getText(RegisterMenuText.REGISTER_ERROR_TITLE),
                    getText(RegisterMenuText.EMPTY_FIELDS_MSG),
                    Alert.AlertType.ERROR
            );
            return;
        }

        if (password.length() < 8 || !password.matches(".*[@%$#&*()_].*") ||
                !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*")) {
            showAlert(
                    getText(RegisterMenuText.REGISTER_ERROR_TITLE),
                    getText(RegisterMenuText.INVALID_PASSWORD_MSG),
                    Alert.AlertType.ERROR
            );
            return;
        }

        if (UserController.isUsernameExist(username)) {
            showAlert(
                    getText(RegisterMenuText.REGISTER_ERROR_TITLE),
                    getText(RegisterMenuText.USERNAME_EXISTS_MSG),
                    Alert.AlertType.ERROR
            );
            return;
        }

        if (favoriteColor == null || favoriteColor.isEmpty()) {
            showAlert(
                    getText(RegisterMenuText.REGISTER_ERROR_TITLE),
                    getText(RegisterMenuText.EMPTY_COLOR_MSG),
                    Alert.AlertType.ERROR
            );
            return;
        }

        UserController.setTemporaryUsername(username);
        UserController.setTemporaryPassword(password);
        UserController.setTemporaryFavoriteColor(favoriteColor);

        try {
            new SelectAvatarMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(RegisterMenuText.ERROR_TITLE),
                    getText(RegisterMenuText.REGISTER_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    public void back(MouseEvent mouseEvent) {
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(RegisterMenuText.ERROR_TITLE),
                    getText(RegisterMenuText.BACK_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    private String getText(RegisterMenuText textType) {
        return textType.getText(isEnglish);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}