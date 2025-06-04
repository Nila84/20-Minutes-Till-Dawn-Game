package view.menu;

import controller.App;
import controller.GameController;
import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Paths;
import view.enums.LoginMenuText;
import view.util.CustomCursor;

import java.net.URL;

public class LoginMenu extends Application {
    public static Stage stageOfProgram;
    public static Stage forgotPassStage;
    static boolean forgotPass = false;

    // UI Components (now defined in Java instead of FXML)
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button loginButton = new Button();
    private Hyperlink registerLink = new Hyperlink();
    private Hyperlink forgotPasswordLink = new Hyperlink();
    private Hyperlink guestLink = new Hyperlink();

    private boolean isEnglish = GameController.language; // Default to English, can be changed

    public static void main(String[] args) {
        CustomCursor.initialize();
        App.run();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        forgotPassStage = stage;
        stageOfProgram = stage;
        stage.getIcons().add(new Image(LoginMenu.class.getResource("/images/icons/aaIcon.png").toExternalForm()));

        if (App.isStayLoggedIn()) {
            new MainMenu().start(stage);
        } else {
            BorderPane borderPane = createLoginPane();
            borderPane.getStyleClass().add("Background");
            applyTheme(borderPane);

            Scene loginMenuScene = new Scene(borderPane, 700, 700);
            CustomCursor.setGameCursor(loginMenuScene);
            stage.setTitle(getText(LoginMenuText.LOGIN_TITLE));
            stage.setScene(loginMenuScene);
            stage.show();
        }
    }

    private BorderPane createLoginPane() {
        BorderPane borderPane = new BorderPane();

        // Setup UI components
        setupTextField(usernameField, getText(LoginMenuText.USERNAME_PROMPT));
        setupTextField(passwordField, getText(LoginMenuText.PASSWORD_PROMPT));

        loginButton.setText(getText(LoginMenuText.LOGIN_BUTTON));
        loginButton.setOnAction(e -> login(null));

        registerLink.setText(getText(LoginMenuText.REGISTER_LINK));
        registerLink.setOnAction(e -> goToRegister(null));

        forgotPasswordLink.setText(getText(LoginMenuText.FORGOT_PASSWORD_LINK));
        forgotPasswordLink.setOnAction(e -> forgotPassword(null));

        guestLink.setText(getText(LoginMenuText.GUEST_LINK));
        guestLink.setOnAction(e -> playAsGuest(null));

        // Create layout
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(
                createLabeledField(getText(LoginMenuText.USERNAME_LABEL), usernameField),
                createLabeledField(getText(LoginMenuText.PASSWORD_LABEL), passwordField),
                loginButton,
                registerLink,
                forgotPasswordLink,
                guestLink
        );

        borderPane.setCenter(centerBox);
        return borderPane;
    }

    private HBox createLabeledField(String labelText, TextField field) {
        HBox hbox = new HBox(10);
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

    public void login(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showAlert(
                    getText(LoginMenuText.LOGIN_ERROR_TITLE),
                    getText(LoginMenuText.EMPTY_FIELDS_MSG),
                    Alert.AlertType.ERROR
            );
            return;
        }

        boolean isLoginSuccessful = UserController.login(username, password);
        if (isLoginSuccessful) {
            try {
                new MainMenu().start(LoginMenu.stageOfProgram);
            } catch (Exception e) {
                showAlert(
                        getText(LoginMenuText.ERROR_TITLE),
                        getText(LoginMenuText.LOGIN_ERROR_MSG),
                        Alert.AlertType.ERROR
                );
            }
        } else if (!UserController.isUsernameExist(username)) {
            showAlert(
                    getText(LoginMenuText.LOGIN_ERROR_TITLE),
                    getText(LoginMenuText.USERNAME_INCORRECT_MSG),
                    Alert.AlertType.ERROR
            );
        } else {
            showAlert(
                    getText(LoginMenuText.LOGIN_ERROR_TITLE),
                    getText(LoginMenuText.PASSWORD_INCORRECT_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    public void goToRegister(MouseEvent mouseEvent) {
        try {
            new RegisterMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(LoginMenuText.ERROR_TITLE),
                    getText(LoginMenuText.REGISTER_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    public void forgotPassword(MouseEvent mouseEvent) {
        try {
            new findPassword().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(LoginMenuText.ERROR_TITLE),
                    getText(LoginMenuText.FORGOT_PASSWORD_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    public void playAsGuest(MouseEvent mouseEvent) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(LoginMenuText.ERROR_TITLE),
                    getText(LoginMenuText.GUEST_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    private String getText(LoginMenuText textType) {
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