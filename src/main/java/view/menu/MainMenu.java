package view.menu;

import controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;
import view.Paths;
import view.enums.MainMenuText;
import view.util.CustomCursor;

public class MainMenu extends Application {
    private ImageView avatarImageView;
    private Label usernameLabel;
    private Label scoreLabel;
    private Circle player;
    public static boolean loadGame = false;
    private boolean isEnglish = GameController.language;

    @Override
    public void start(Stage stage) throws Exception {
        createUIComponents();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTopBar());
        borderPane.setCenter(createCenterMenu());
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
        Scene mainMenuScene = new Scene(borderPane, 700, 700);
        CustomCursor.setGameCursor(mainMenuScene);
        stage.setScene(mainMenuScene);
        stage.show();
    }

    private void createUIComponents() {
        avatarImageView = new ImageView();
        avatarImageView.setFitWidth(60);
        avatarImageView.setFitHeight(60);

        usernameLabel = new Label();
        usernameLabel.getStyleClass().add("username-label");

        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("score-label");
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("user-info-box");
        Font customFont = Font.loadFont(getClass().getResourceAsStream(
                "/fonts/MyFont.ttf"), 10);

        if (customFont != null) {
            usernameLabel.setFont(customFont);
            scoreLabel.setFont(Font.font(customFont.getFamily(), 10));
        } else {
            System.out.println("bjhhjb");
            usernameLabel.setFont(Font.font("Arial", 18));
            scoreLabel.setFont(Font.font("Arial", 16));
        }

        User currentUser = App.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            scoreLabel.setText(getText(MainMenuText.SCORE_EN) + currentUser.getScore());

            try {
                String path = currentUser.getAvatarFilePath();
                Image avatarImage = new Image(path);
                avatarImageView.setImage(avatarImage);
            } catch (Exception e) {
                System.out.println("Could not load avatar: " + e.getMessage());
            }
        }

        VBox userInfoBox = new VBox();
        userInfoBox.getChildren().addAll(usernameLabel, scoreLabel);

        topBar.getChildren().addAll(avatarImageView, userInfoBox);
        return topBar;
    }

    private VBox createCenterMenu() {
        VBox centerMenu = new VBox(40);
        centerMenu.setAlignment(Pos.CENTER);
        centerMenu.getStyleClass().add("mainMenuButtons");
        Font buttonFont = Font.loadFont(getClass().getResourceAsStream(
                "/fonts/MyFont.ttf"), 10);

        if (buttonFont != null) {
            System.out.println("innn");
            centerMenu.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    ((Button) node).setFont(buttonFont);
                }
            });
        }

        Button talentButton = createMenuButton(getText(MainMenuText.TALENT_MENU_EN), this::talentMenu);
        Button preGameButton = createMenuButton(getText(MainMenuText.PRE_GAME_EN), this::preGameMenu);
        Button resumeButton = createMenuButton(getText(MainMenuText.RESUME_GAME_EN), this::resumingPreviousGame);
        Button profileButton = createMenuButton(getText(MainMenuText.PROFILE_MENU_EN), this::profileMenu);
        Button scoreboardButton = createMenuButton(getText(MainMenuText.SCOREBOARD_EN), this::scoreBoardMenu);
        Button settingsButton = createMenuButton(getText(MainMenuText.SETTINGS_EN), this::settings);
        Button logoutButton = createMenuButton(getText(MainMenuText.LOGOUT_EN), this::logout);
        Button exitButton = createMenuButton(getText(MainMenuText.EXIT_EN), this::exit);

        centerMenu.getChildren().addAll(
                talentButton, preGameButton, resumeButton,
                profileButton, scoreboardButton, settingsButton,
                logoutButton, exitButton
        );

        return centerMenu;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<MouseEvent> handler) {
        Button button = new Button(text);
        button.setOnMouseClicked(handler);
        return button;
    }

    private String getText(MainMenuText textType) {
        return MainMenuText.getText(textType, isEnglish);
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void logout(MouseEvent mouseEvent) {
        UserController.logout();
        try {
            new LoginMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settings(MouseEvent mouseEvent) {
        try {
            new SettingsMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profileMenu(MouseEvent mouseEvent) {
        if (App.isStayLoggedIn()) {
            try {
                new ProfileMenu().start(LoginMenu.stageOfProgram);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void talentMenu(MouseEvent mouseEvent) {
        if (App.isStayLoggedIn()) {
            try {
                new TalentMenu().start(LoginMenu.stageOfProgram);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error opening TalentMenu: " + e.getMessage());
            }
        }
    }

    public void preGameMenu(MouseEvent mouseEvent) {
        try {
            if (!App.isStayLoggedIn()) {
                User user = new User("Guest111", "Guest2222", "jdbchsjvb", "red");
                App.setCurrentUser(user);
                return;
            }

            Stage currentStage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            new PreGameMenu().start(currentStage);
        } catch (Exception e) {
            System.err.println("Error opening PreGameMenu: " + e.getMessage());
            e.printStackTrace();
            showAlert(getText(MainMenuText.ERROR_EN), "Failed to open Pre-Game Menu: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void resumingPreviousGame(MouseEvent mouseEvent) {
        if (!App.isStayLoggedIn()) {
            showAlert(
                    getText(MainMenuText.ACCESS_DENIED_EN),
                    getText(MainMenuText.MUST_LOGIN_EN)
            );
            return;
        }

        try {
            User currentUser = App.getCurrentUser();
            SavedGameData savedData = GameViewController.loadSavedGame(currentUser.getUsername());

            if (savedData == null) {
                showAlert(
                        getText(MainMenuText.NO_SAVE_EN),
                        "No saved game found for this user."
                );
                return;
            }

            GameScreen.setStartTime(System.currentTimeMillis());
            GameScreen.setGameDuration((int) (savedData.getRemainingTime() / 1000L));

            if (currentUser.getSelectedHero().getName().equals("SHANA")) player = new Circle(15, Color.PURPLE);
            else if (currentUser.getSelectedHero().getName().equals("DIAMOND")) player = new Circle(15, Color.PINK);
            else if (currentUser.getSelectedHero().getName().equals("LILITH")) player = new Circle(15, Color.GREEN);
            else if (currentUser.getSelectedHero().getName().equals("SCARLET")) player = new Circle(15, Color.GRAY);
            else if (currentUser.getSelectedHero().getName().equals("DASHER")) player = new Circle(15, Color.BLUE);
            player.setCenterX(savedData.getPlayerX());
            player.setCenterY(savedData.getPlayerY());

            Weapon.setCurrentAmmo(savedData.getAmmoCount());
            loadGame = true;

            PreGameMenu.startResumedGame(savedData);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(getText(MainMenuText.ERROR_EN), "Failed to resume game: " + e.getMessage());
        }
    }

    public void scoreBoardMenu(MouseEvent mouseEvent) {
        try {
            new ScoreboardMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show() {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("Error returning to main menu: " + e.getMessage());
        }
    }
}