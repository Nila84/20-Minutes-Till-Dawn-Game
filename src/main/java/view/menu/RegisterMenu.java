package view.menu;

import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Paths;

import java.net.URL;

public class RegisterMenu extends Application {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField favoriteColorField;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = RegisterMenu.class.getResource(Paths.REGISTER_MENU_FXML_FILE.getPath());
        BorderPane borderPane = FXMLLoader.load(url);
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        Scene registerMenuScene = new Scene(borderPane);
        stage.setScene(registerMenuScene);
        stage.show();
    }

    public void register(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String favoriteColor = favoriteColorField.getText();
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Invalid register!" ,
                    "Register was not successful!" , "Username or password is empty!");
            return;
        }
        if (password.length() < 8 || !password.matches(".*[@%$#&*()_].*") ||
                !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*")) {
            Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Invalid register!" ,
                    "Register was not successful!" , "Your password format is invalid!");
            return;
        }
        if (UserController.isUsernameExist(username)) {
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Invalid register!" ,
                    "Register was not successful!" , "A user with this username exists!");
            return;
        }
        if (favoriteColor == null || favoriteColor.isEmpty()) {
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Invalid register!" ,
                    "Register was not successful!" , "Favorite color is empty!");
            return;
        }
        UserController.setTemporaryUsername(username);
        UserController.setTemporaryPassword(password);
        UserController.setTemporaryFavoriteColor(favoriteColor);

        try {
            new SelectAvatarMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void back(MouseEvent mouseEvent) {
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
