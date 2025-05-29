package view.menu;

import controller.App;
import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Paths;
import javafx.scene.input.MouseEvent;

//import java.awt.event.MouseEvent;
import java.net.URL;

public class findPassword extends Application {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField favoriteColorField;
    @FXML
    private PasswordField newPasswordField;


    public void start(Stage stage) throws Exception {
        URL url = findPassword.class.getResource(Paths.FORGOT_PASS.getPath());
        BorderPane borderPane = FXMLLoader.load(url);
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        Scene findPassScene = new Scene(borderPane);
        stage.setScene(findPassScene);
        stage.show();
    }

    public void findPassbyQuestion(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String favoriteColor = favoriteColorField.getText();
        String newPassword = newPasswordField.getText();

        if (username == null || favoriteColor == null || newPassword == null ||
                username.isEmpty() || favoriteColor.isEmpty() || newPassword.isEmpty()) {
            GameViewController.alertShowing(Alert.AlertType.ERROR, "Error!",
                    "Fields are empty", "Please fill all fields.");
            return;
        }

        String correctColor = UserController.getFavoriteColorOfUser(username);
        if (correctColor == null) {
            GameViewController.alertShowing(Alert.AlertType.ERROR, "Error!",
                    "Username not found", "No user with this username exists.");
            return;
        }

        if (!correctColor.equalsIgnoreCase(favoriteColor.trim())) {
            GameViewController.alertShowing(Alert.AlertType.ERROR, "Error!",
                    "Incorrect Color", "Favorite color does not match.");
            return;
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*[@%$#&*()_].*") ||
                !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*\\d.*")) {
            GameViewController.alertShowing(Alert.AlertType.ERROR , "Invalid Password!" ,
                    "Reset failed!" , "Password format is invalid.");
            return;
        }

        UserController.updatePassword(username, newPassword);
        GameViewController.alertShowing(Alert.AlertType.INFORMATION, "Success!",
                "Password changed", "Your password has been reset.");

        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("Error while returning to login menu");
        }
    }
    public void backToLogin(MouseEvent mouseEvent) {
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("Error returning to login menu.");
        }
    }

}