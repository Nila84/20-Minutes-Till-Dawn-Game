package view.menu;

import controller.GameController;
import controller.GameViewController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.Paths;
import view.enums.ErrorMessages;
import view.enums.ProfileMenuText;
import view.enums.SuccessMessages;
import view.util.CustomCursor;

public class ProfileMenu extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmationField;
    private boolean isEnglish = GameController.language;

    @Override
    public void start(Stage stage) throws Exception {
        createUIComponents();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createCenterVBox());
        borderPane.getStyleClass().add("Background");

        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        } else {
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
        }

        Scene profileMenuScene = new Scene(borderPane, 700, 700);
        CustomCursor.setGameCursor(profileMenuScene);
        stage.setScene(profileMenuScene);
        stage.show();
    }

    private void createUIComponents() {
        usernameField = new TextField();
        usernameField.setPromptText(getText(ProfileMenuText.USERNAME_PROMPT_EN));

        passwordField = new PasswordField();
        passwordField.setPromptText(getText(ProfileMenuText.PASSWORD_PROMPT_EN));

        confirmationField = new PasswordField();
        confirmationField.setPromptText(getText(ProfileMenuText.PASSWORD_PROMPT_EN));
    }

    private VBox createCenterVBox() {
        VBox vbox = new VBox(40);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getStyleClass().add("profileMenuButton");

        HBox usernameBox = new HBox(20);
        usernameBox.setAlignment(Pos.CENTER);
        Label usernameLabel = new Label(getText(ProfileMenuText.NEW_USERNAME_EN));
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        Button changeUsernameBtn = new Button(getText(ProfileMenuText.CHANGE_USERNAME_EN));
        changeUsernameBtn.setOnAction(e -> changeUsername(null));

        HBox passwordBox = new HBox(20);
        passwordBox.setAlignment(Pos.CENTER);
        Label passwordLabel = new Label(getText(ProfileMenuText.NEW_PASSWORD_EN));
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        HBox confirmationBox = new HBox(20);
        confirmationBox.setAlignment(Pos.CENTER);
        Label confirmationLabel = new Label(getText(ProfileMenuText.CONFIRMATION_EN));
        confirmationBox.getChildren().addAll(confirmationLabel, confirmationField);

        Button changePasswordBtn = new Button(getText(ProfileMenuText.CHANGE_PASSWORD_EN));
        changePasswordBtn.setOnAction(e -> changePassword(null));

        Button logoutBtn = new Button(getText(ProfileMenuText.LOGOUT_EN));
        logoutBtn.setOnAction(e -> logout(null));

        Button deleteAccountBtn = new Button(getText(ProfileMenuText.DELETE_ACCOUNT_EN));
        deleteAccountBtn.setOnAction(e -> deleteAccount(null));

        Button changeAvatarBtn = new Button(getText(ProfileMenuText.CHANGE_AVATAR_EN));
        changeAvatarBtn.setOnAction(e -> changeAvatar(null));

        Button backBtn = new Button(getText(ProfileMenuText.BACK_EN));
        backBtn.setOnAction(e -> back(null));

        vbox.getChildren().addAll(
                usernameBox, changeUsernameBtn,
                passwordBox, confirmationBox, changePasswordBtn,
                logoutBtn, deleteAccountBtn, changeAvatarBtn, backBtn
        );

        return vbox;
    }

    private String getText(ProfileMenuText textType) {
        return ProfileMenuText.getText(textType, isEnglish);
    }

    public void logout(MouseEvent mouseEvent) {
        UserController.logout();
        GameViewController.alertShowing(Alert.AlertType.INFORMATION,
                SuccessMessages.getHeader(SuccessMessages.LOGGED_OUT_EN, isEnglish),
                "Logged out!",
                SuccessMessages.getMessage(SuccessMessages.LOGGED_OUT_EN, isEnglish));
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteAccount(MouseEvent mouseEvent) {
        UserController.deleteUser();
        GameViewController.alertShowing(Alert.AlertType.INFORMATION, "Delete Account was successful!",
                "Account was deleted successfully!", "Account was deleted!");
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeUsername(MouseEvent mouseEvent) {
        String newUsername = usernameField.getText();
        if (newUsername == null || newUsername.equals("")) {
            GameViewController.alertShowing(Alert.AlertType.ERROR,
                    ErrorMessages.getHeader(ErrorMessages.EMPTY_USERNAME_EN, isEnglish),
                    "Empty field",
                    ErrorMessages.getMessage(ErrorMessages.EMPTY_USERNAME_EN, isEnglish));
            return;
        }
        if (UserController.isUsernameExist(newUsername)) {
            GameViewController.alertShowing(Alert.AlertType.ERROR,
                    ErrorMessages.getHeader(ErrorMessages.USERNAME_EXISTS_EN, isEnglish),
                    "Username exists!",
                    ErrorMessages.getMessage(ErrorMessages.USERNAME_EXISTS_EN, isEnglish));
            return;
        }
        UserController.changeUsername(newUsername);
        GameViewController.alertShowing(Alert.AlertType.INFORMATION,
                SuccessMessages.getHeader(SuccessMessages.USERNAME_CHANGED_EN, isEnglish),
                "Username was changed!",
                SuccessMessages.getMessage(SuccessMessages.USERNAME_CHANGED_EN, isEnglish));
        try {
            new ProfileMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(MouseEvent mouseEvent) {
        String newPassword = passwordField.getText();
        String passwordConfirmation = confirmationField.getText();

        if (newPassword == null || passwordConfirmation == null ||
                newPassword.equals("") || passwordConfirmation.equals("")) {
            GameViewController.alertShowing(Alert.AlertType.ERROR,
                    ErrorMessages.getTitle(ErrorMessages.EMPTY_PASSWORD_EN, isEnglish),
                    ErrorMessages.getHeader(ErrorMessages.EMPTY_PASSWORD_EN, isEnglish),
                    ErrorMessages.getMessage(ErrorMessages.EMPTY_PASSWORD_EN, isEnglish));
            return;
        }

        if (!newPassword.equals(passwordConfirmation)) {
            GameViewController.alertShowing(Alert.AlertType.ERROR,
                    ErrorMessages.getTitle(ErrorMessages.PASSWORD_MISMATCH_EN, isEnglish),
                    ErrorMessages.getHeader(ErrorMessages.PASSWORD_MISMATCH_EN, isEnglish),
                    ErrorMessages.getMessage(ErrorMessages.PASSWORD_MISMATCH_EN, isEnglish));
            return;
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*[@%$#&*()_].*") ||
                !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*\\d.*")) {
            GameViewController.alertShowing(Alert.AlertType.ERROR, "Change information failed!",
                    "Change password was not successful!", "Your password format is invalid!");
            return;
        }
        UserController.changePassword(newPassword);
        GameViewController.alertShowing(Alert.AlertType.INFORMATION,
                SuccessMessages.getTitle(SuccessMessages.PASSWORD_CHANGED_EN, isEnglish),
                SuccessMessages.getHeader(SuccessMessages.PASSWORD_CHANGED_EN, isEnglish),
                SuccessMessages.getMessage(SuccessMessages.PASSWORD_CHANGED_EN, isEnglish));

        try {
            new ProfileMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void changeAvatar(MouseEvent mouseEvent) {
        try {
            SelectAvatarMenu.setIsChangingAvatarMenuActive(true);
            new SelectAvatarMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(MouseEvent mouseEvent) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}