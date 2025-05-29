package view.menu;


import controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import model.Monster;
import model.User;
import view.Paths;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Application {
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label scoreLabel;



    @Override
    public void start(Stage stage) throws Exception {
        URL mainMenuFXMLUrl = MainMenu.class.getResource(Paths.MAIN_MENU_FXML_FILE.getPath());
        BorderPane borderPane = FXMLLoader.load(mainMenuFXMLUrl);
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        Scene mainMenuScene = new Scene(borderPane);
        stage.setScene(mainMenuScene);
        stage.show();
    }

    @FXML
    public void initialize() {
        User currentUser = App.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            scoreLabel.setText("Score: " + currentUser.getScore());
            try {
                String path = currentUser.getAvatarFilePath(); // fix slashes
                Image avatarImage = new Image(path);
                avatarImageView.setImage(avatarImage);
            } catch (Exception e) {
                System.out.println("Could not load avatar from file system: " + e.getMessage());
            }


        }
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
                e.printStackTrace(); // خطا را کامل چاپ کنید
                System.out.println("Error opening TalentMenu: " + e.getMessage());
            }
        }
    }
    public void preGameMenu(MouseEvent mouseEvent) {
        try {
            if (!App.isStayLoggedIn()) {
                User user = new User
                        ("Guest111" , "Guest2222","jdbchsjvb","red");
                App.setCurrentUser(user);
                //TODO : فعال کردن حالتی برای گست
//                showAlert("Access Denied", "You must be logged in to access the Pre-Game Menu.");
                return;
            }

            Stage currentStage = (Stage) ((Button) mouseEvent.getSource()).getScene().getWindow();
            new PreGameMenu().start(currentStage);
        } catch (Exception e) {
            System.err.println("Error opening PreGameMenu: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to open Pre-Game Menu: " + e.getMessage());
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
            showAlert("Access Denied", "You must be logged in to resume a saved game.");
            return;
        }

        try {
            User currentUser = App.getCurrentUser();
            SavedGameData savedData = GameViewController.loadSavedGame(currentUser.getUsername());

            if (savedData == null) {
                showAlert("No Save Found", "No saved game found for this user.");
                return;
            }

            // بازیابی اطلاعات کاربر
            currentUser.setXP(savedData.getXp());
            currentUser.getSelectedHero().setHp(savedData.getHp());
            User.level = savedData.getLevel();
            currentUser.killNum = savedData.getKills();

            // ست کردن تایمر بازی
            GameScreen.setStartTime(System.currentTimeMillis());
            GameScreen.setGameDuration((int) (savedData.getRemainingTime() / 1000L));

            // بازسازی لیست دشمن‌ها
            List<Monster> restoredMonsters = new ArrayList<>();
            for (EnemyData enemyData : savedData.getEnemyDataList()) {
                Monster monster = GameViewController.createMonsterFromType(enemyData.getType(), enemyData.getX(), enemyData.getY());
                if (monster != null) {
                    restoredMonsters.add(monster);
                }
            }
            GameScreen.setMonsters(restoredMonsters);

            // اجرای بازی
            PreGameMenu.startResumedGame(savedData); // false = نیازی به نمایش انتخاب هیرو نیست

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to resume game: " + e.getMessage());
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
