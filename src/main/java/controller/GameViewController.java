package controller;

import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;
import view.Paths;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import view.menu.*;

import java.io.*;
import java.util.*;

import static view.menu.GameMenu.*;

public class GameViewController {
    public static boolean isSFXOn = false;
    public static boolean isLightThemeOn = false;
    public static boolean autoReload = false;
    private static final List<String> musicFileNames = Arrays.asList(
            "1.mp3",
            "2.mp3",
            "3.mp3"
    );

    private static int currentSongIndex = 0;
    private static MediaPlayer mediaPlayer;

    public static boolean isBlackWhiteThemeOn = false;
    private static boolean sfxEnabled = true;
    private static Map<String, KeyCode> keyBindings = new HashMap<>();

    static {
        // Default key bindings
        keyBindings.put("UP", KeyCode.W);
        keyBindings.put("DOWN", KeyCode.S);
        keyBindings.put("LEFT", KeyCode.A);
        keyBindings.put("RIGHT", KeyCode.D);
        keyBindings.put("SHOOT", KeyCode.SPACE);
    }

    private static Hero hero = App.getCurrentUser().getSelectedHero();

    private static void enableInfiniteAmmo(int seconds) {
        System.out.println("Infinite ammo enabled for " + seconds + " seconds");
        //TODO
    }

    public static KeyCode getKeyBinding(String action) {
        return keyBindings.getOrDefault(action, null);
    }

    public static void setKeyBinding(String action, KeyCode keyCode) {
        keyBindings.put(action, keyCode);
    }

//    public static void setupMovementControls(Scene scene, Circle player, double speed) {
//        final boolean[] upPressed = {false};
//        final boolean[] downPressed = {false};
//        final boolean[] leftPressed = {false};
//        final boolean[] rightPressed = {false};
//
//        scene.setOnKeyPressed(event -> {
//            KeyCode keyCode = event.getCode();
//
//            if (keyCode == getKeyBinding("UP")) upPressed[0] = true;
//            else if (keyCode == getKeyBinding("DOWN")) downPressed[0] = true;
//            else if (keyCode == getKeyBinding("LEFT")) leftPressed[0] = true;
//            else if (keyCode == getKeyBinding("RIGHT")) rightPressed[0] = true;
//
//            updatePlayerPosition(player, speed, upPressed[0], downPressed[0], leftPressed[0], rightPressed[0]);
//        });
//
//        scene.setOnKeyReleased(event -> {
//            KeyCode keyCode = event.getCode();
//
//            if (keyCode == getKeyBinding("UP")) upPressed[0] = false;
//            else if (keyCode == getKeyBinding("DOWN")) downPressed[0] = false;
//            else if (keyCode == getKeyBinding("LEFT")) leftPressed[0] = false;
//            else if (keyCode == getKeyBinding("RIGHT")) rightPressed[0] = false;
//
//            updatePlayerPosition(player, speed, upPressed[0], downPressed[0], leftPressed[0], rightPressed[0]);
//        });
//    }

    public static void setupMovementControls(Scene scene, Circle player, double speed) {
        final boolean[] upPressed = {false};
        final boolean[] downPressed = {false};
        final boolean[] leftPressed = {false};
        final boolean[] rightPressed = {false};
        final boolean[] isMoving = {false};

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            boolean wasMoving = isMoving[0];

            if (keyCode == getKeyBinding("UP")) upPressed[0] = true;
            else if (keyCode == getKeyBinding("DOWN")) downPressed[0] = true;
            else if (keyCode == getKeyBinding("LEFT")) leftPressed[0] = true;
            else if (keyCode == getKeyBinding("RIGHT")) rightPressed[0] = true;

            isMoving[0] = upPressed[0] || downPressed[0] || leftPressed[0] || rightPressed[0];

            // شروع انیمیشن هنگام شروع حرکت
            if (isMoving[0] && !wasMoving) {
                GameScreen.getInstance().startPulseAnimation();
            }

            updatePlayerPosition(player, speed, upPressed[0], downPressed[0], leftPressed[0], rightPressed[0]);
        });

        scene.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            boolean wasMoving = isMoving[0];

            if (keyCode == getKeyBinding("UP")) upPressed[0] = false;
            else if (keyCode == getKeyBinding("DOWN")) downPressed[0] = false;
            else if (keyCode == getKeyBinding("LEFT")) leftPressed[0] = false;
            else if (keyCode == getKeyBinding("RIGHT")) rightPressed[0] = false;

            isMoving[0] = upPressed[0] || downPressed[0] || leftPressed[0] || rightPressed[0];

            // توقف انیمیشن هنگام توقف حرکت
            if (!isMoving[0] && wasMoving) {
                GameScreen.getInstance().stopPulseAnimation();
            }

            updatePlayerPosition(player, speed, upPressed[0], downPressed[0], leftPressed[0], rightPressed[0]);
        });
    }

    public static Monster createMonsterFromType(String type, double x, double y) {
        switch (type) {
            case "EyebatMonster":
                return new EyebatMonster(x, y , Color.RED);
            case "TentacleMonster":
                return new TentacleMonster(x, y, Color.YELLOW);
            case "ElderMonster":
                return new EyebatMonster(x,y,Color.ORANGE);
            default:
                return null;
        }
    }


    public static SavedGameData loadSavedGame(String username) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("saves/" + username + ".json");
            SavedGameData savedData = gson.fromJson(reader, SavedGameData.class);
            reader.close();
            return savedData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void updatePlayerPosition(Circle player, double speed,
                                             boolean up, boolean down,
                                             boolean left, boolean right) {
        double dx = 0;
        double dy = 0;

        if (up && !down) dy = -speed;
        else if (down && !up) dy = speed;

        if (left && !right) dx = -speed;
        else if (right && !left) dx = speed;

        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length * speed;
            dy = dy / length * speed;
        }

        player.setCenterX(player.getCenterX() + dx);
        player.setCenterY(player.getCenterY() + dy);
    }


    public static void changeToNextMusic() {
        currentSongIndex = (currentSongIndex + 1) % musicFileNames.size();

        String filePath = Paths.MUSICS_PATH.getPath() + musicFileNames.get(currentSongIndex);
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Music file not found: " + filePath);
            return;
        }

        Media media = new Media(file.toURI().toString());
        MediaPlayer newPlayer = new MediaPlayer(media);
        if (mediaPlayer != null) {
            newPlayer.setMute(mediaPlayer.isMute());
            newPlayer.setVolume(mediaPlayer.getVolume());
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        mediaPlayer = newPlayer;
        mediaPlayer.play();
    }


    public static Rectangle createMuteUnmuteIcon() {
        Rectangle icon = new Rectangle(380 , 10 , 20 , 20);
        if (isMute == false) {
            icon.setFill(new ImagePattern(new Image
                    (GameViewController.class.getResource
                            ("/images/icons/unmuteIcon.png").toExternalForm())));
        } else {
            icon.setFill(new ImagePattern(new Image
                    (GameViewController.class.getResource
                            ("/images/icons/muteIcon.jpg").toExternalForm())));
        }
        icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isMute == false) {
                    icon.setFill
                            (new ImagePattern(new Image(GameMenu.class.getResource
                                    ("/images/icons/muteIcon.jpg").toExternalForm())));
                    songPlayer.setMute(true);
                    isMute = true;
                } else {
                    icon.setFill
                            (new ImagePattern(new Image(GameMenu.class.getResource
                                    ("/images/icons/unmuteIcon.png").toExternalForm())));
                    songPlayer.setMute(false);
                    isMute = false;
                }
            }
        });
        return icon;
    }

    public static Rectangle createMenuIcon(Pane gamePane) {
        Rectangle icon = new Rectangle(415 , 10 , 20 , 20);
        icon.setFill(new ImagePattern(new Image(GameViewController.class.getResource
                ("/images/icons/menuIcon.png").toExternalForm())));

        icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                menuIconEvents(gamePane);
            }
        });

        return icon;
    }

    public static void menuIconEvents(Pane gamePane) {
        GameController.isPaused = true;
        gamePane.setEffect(new GaussianBlur());
        VBox pauseRoot = new VBox(10);
        pauseRoot.setAlignment(Pos.CENTER);
        pauseRoot.setPadding(new Insets(20));
        pauseRoot.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 10;");
        pauseRoot.getChildren().add(new Label("⏸️ Game Paused"));
        Button resume = new Button("▶️ Resume");
        Button restart = new Button("🔄 Restart");
        Button saveGame = new Button("💾 Save Game");
        Button helpKeys = new Button("❓ Help (Keys)");
        Button changeMusic = new Button("🎵 Change Music");
        Button changeTheme = new Button("🎨 Change Theme");
        Button cheatCodes = new Button("💻 Cheat Codes");
        Button abilities = new Button("Abilities");
        Button quitGame = new Button("❌ Quit Game");

        pauseRoot.getChildren().addAll
                (resume, restart, saveGame, helpKeys, changeMusic, changeTheme, cheatCodes,abilities, quitGame);

        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.initOwner(LoginMenu.stageOfProgram);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(pauseRoot, Color.TRANSPARENT));

        resume.setOnAction(event -> {
            GameController.isPaused = false;
            gamePane.setEffect(null);
            GameController.time = System.currentTimeMillis();
            popupStage.hide();
        });

        restart.setOnAction(e -> {
            try {
                restartEvent(popupStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        quitGame.setOnAction(e -> {
            showGameOverScreen(popupStage);
        });

        helpKeys.setOnAction(e -> {
            helpKeyEvents();
        });

        changeMusic.setOnAction(e -> {
            changeMusicEvents();
        });

        cheatCodes.setOnAction(e -> {
            showCheatCodes();
        });

        changeTheme.setOnAction(e -> {
            changeTheme(gamePane);
        });

        abilities.setOnAction(e ->{
            GameScreen.showAbilities();
        });


        saveGame.setOnAction(e -> {
            try {
                List<EnemyData> enemyDataList = new ArrayList<>();
                for (Monster monster : GameScreen.getMonsters()) {
                    enemyDataList.add(new EnemyData
                            (monster.getX(), monster.getY(), monster.getClass().getSimpleName()));
                }

                Circle player = GameScreen.getInstance().getPlayer();
                User currentUser = App.getCurrentUser();
                int xp = currentUser.getXP();
                int hp = currentUser.getSelectedHero().getHp();
                int level = User.level;
                int kills = currentUser.killNum;
                long elapsed = System.currentTimeMillis() - GameScreen.getStartTime();
                long remainingTime = GameScreen.getGameDuration() * 1000L - elapsed;

                String heroName = App.getCurrentUser().getSelectedHero().getName();
                String weaponName = App.getCurrentUser().getSelectedWeapon().getName();
                String abilityName = GameScreen.getAbility().getName();
                SavedGameData savedData = new SavedGameData(
                        currentUser.getUsername(), hp, xp, level, kills, remainingTime,GameScreen.getGameDuration(),
                        enemyDataList, heroName,weaponName,abilityName,player.getCenterX(),
                        player.getCenterY(),
                        Weapon.getCurrentAmmo()
                );

                new File("saves").mkdirs();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                FileWriter writer = new FileWriter("saves/" + currentUser.getUsername() + ".json");
                gson.toJson(savedData, writer);
                writer.close();

                System.out.println("Game saved to JSON!");
                back();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });



        popupStage.show();
    }



    public static void changeTheme(Pane targetPane) {
        try {
            Scene scene = targetPane.getScene();
            if (scene == null) return;

            String darkStyle = GameViewController.class.getResource("/CSS/commonStyles.css").toExternalForm();
            String lightStyle = GameViewController.class.getResource("/CSS/light_theme.css").toExternalForm();

            if (isLightThemeOn) {
                scene.getStylesheets().remove(lightStyle);
                scene.getStylesheets().add(darkStyle);
                targetPane.setStyle("-fx-background-color: #333333;");
            } else {
                scene.getStylesheets().remove(darkStyle);
                scene.getStylesheets().add(lightStyle);
                targetPane.setStyle("-fx-background-color: #ffffff;");
            }

            isLightThemeOn = !isLightThemeOn;
        } catch (Exception e) {
            System.err.println("Error changing theme: " + e.getMessage());
        }
    }

    private static void showGameOverScreen(Stage gamePane) {
        Pane resultPane = new Pane();
        resultPane.setStyle("-fx-background-color: black;");
        resultPane.setPrefSize(1200, 700);

        long endTime = System.currentTimeMillis();
        long totalTimeSeconds = (endTime - GameScreen.startTime2) / 1000;

        String username = App.getCurrentUser().getUsername();
        int gameOverNum = App.getCurrentUser().getGameOver();
        int killCount = App.getCurrentUser().killNumber;
        long score = totalTimeSeconds * killCount;

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Verdana", 40));
        gameOverText.setFill(Color.RED);
        gameOverText.setLayoutX(1200 / 2.0 - 130);
        gameOverText.setLayoutY(150);

        Text userText = new Text("Username: " + username);
        userText.setFont(Font.font(24));
        userText.setFill(Color.WHITE);
        userText.setLayoutX(100);
        userText.setLayoutY(250);

        Text gameOver = new Text("Game Over in " + gameOverNum + " games!");
        gameOver.setFont(Font.font(24));
        gameOver.setFill(Color.PINK);
        gameOver.setLayoutX(100);
        gameOver.setLayoutY(300);

        Text timeText = new Text("Time Survived: " + totalTimeSeconds + " seconds");
        timeText.setFont(Font.font(24));
        timeText.setFill(Color.WHITE);
        timeText.setLayoutX(100);
        timeText.setLayoutY(350);

        Text killsText = new Text("Kills: " + killCount);
        killsText.setFont(Font.font(24));
        killsText.setFill(Color.WHITE);
        killsText.setLayoutX(100);
        killsText.setLayoutY(400);

        Text scoreText = new Text("Score: " + score);
        scoreText.setFont(Font.font(24));
        scoreText.setFill(Color.YELLOW);
        scoreText.setLayoutX(100);
        scoreText.setLayoutY(450);
        //TODO : نمایش برد ها و باخت ها و خواندن از فایل برای آن کاربر
        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back to Main Menu");
        backButton.setLayoutX(100);
        backButton.setLayoutY(470);
        backButton.setOnAction(event -> back());

        resultPane.getChildren().addAll(gameOverText, userText,gameOver, timeText, killsText, scoreText, backButton);

        Scene resultScene = new Scene(resultPane, 1200, 700);
        Stage stage = (Stage) gamePane.getScene().getWindow();
        stage.setScene(resultScene);
    }

    public static void back() {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static HBox createCheatBox(String cheatName, String cheatDesc, String hotkey, Runnable onClickAction) {
        Label nameLabel = new Label(cheatName);
        nameLabel.setFont(Font.font(16));
        Label descLabel = new Label(" - " + cheatDesc);
        descLabel.setFont(Font.font(14));
        Label keyLabel = new Label(" [" + hotkey + "]");
        keyLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2980b9;");

        HBox box = new HBox(10, nameLabel, descLabel, keyLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(8));
        box.setStyle
                ("-fx-background-color: #f0f0f0; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;");
        box.setOnMouseClicked(e -> {
            onClickAction.run();
            System.out.println(cheatName + " activated!");
        });
        box.setOnMouseEntered
                (e -> box.setStyle("-fx-background-color: #d0e7ff; -fx-border-color: #2980b9; -fx-border-radius: 5; -fx-background-radius: 5;"));
        box.setOnMouseExited
                (e -> box.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;"));

        return box;
    }


    public static void showCheatCodes() {
        Stage cheatCodeStage = new Stage();
        cheatCodeStage.initModality(Modality.APPLICATION_MODAL);
        cheatCodeStage.setTitle("Cheat Codes");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("AVAILABLE CHEAT CODES");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        VBox cheatList = new VBox(10);
        cheatList.setAlignment(Pos.CENTER_LEFT);

        HBox cheat1 = createCheatBox("Reduce Game Time", "Shortens remaining game time by 50%", "F1", () -> {
            GameScreen.getInstance().setGameDuration(-50);
            System.out.println("Game time reduced by 1 minute");
        });

        HBox cheat2 = createCheatBox("Level Up Character", "Instantly levels up with full animations", "F2", () -> {
            GameScreen.showAbilitySelectionMenu();
            App.getCurrentUser().setLevel(1);
            System.out.println("Hero leveled up!");
        });

        HBox cheat3 = createCheatBox("Restore Health", "Fully restores health when empty", "F3", () -> {
            Hero hero = App.getCurrentUser().getSelectedHero();
            if (hero.getHp() == 1) {
                hero.setHp(10);
                System.out.println("Hero health increased by 10!");
            } else {
                System.out.println("Hero health is not 1, no change.");
            }
        });

        HBox cheat4 = createCheatBox("Trigger Boss Fight", "Skips to boss fight immediately", "F4", () -> {
            for (int i = 0; i < 15; i++) {
                GameScreen.spawnMonstersCheat(1);
            }
            System.out.println("15 Elder enemies added!");
        });

        HBox cheat5 = createCheatBox("Infinite Ammo", "Unlimited ammo for 60 seconds", "F5", () -> {
            enableInfiniteAmmo(60);
            System.out.println("Infinite ammo enabled for 60 seconds");
        });

        cheatList.getChildren().addAll(cheat1, cheat2, cheat3, cheat4, cheat5);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 16; -fx-pref-width: 120;");
        closeButton.setOnAction(e -> cheatCodeStage.close());

        root.getChildren().addAll(title, cheatList, closeButton);

        Scene scene = new Scene(root, 550, 400);
        cheatCodeStage.setScene(scene);
        cheatCodeStage.show();
    }


    public static void changeMusicEvents() {
        VBox pauseRoot = new VBox(5);
        pauseRoot.getChildren().add(new Label("Paused"));
        pauseRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseRoot.setAlignment(Pos.CENTER);
        pauseRoot.setPadding(new Insets(20));


        Button music1 = new Button("Die With A Smile");
        Button music2 = new Button("perfect");
        Button music3 = new Button("Sky");
        Button okButton = new Button("OK");

        setHandleForChangeMusicButtons(music1 , 1);
        setHandleForChangeMusicButtons(music2 , 2);
        setHandleForChangeMusicButtons(music3 , 3);

        pauseRoot.getChildren().add(okButton);
        pauseRoot.getChildren().add(music1);
        pauseRoot.getChildren().add(music2);
        pauseRoot.getChildren().add(music3);

        Stage popupStage2 = new Stage(StageStyle.TRANSPARENT);
        popupStage2.initOwner(LoginMenu.stageOfProgram);
        popupStage2.initModality(Modality.APPLICATION_MODAL);
        popupStage2.setScene(new Scene(pauseRoot, Color.TRANSPARENT));
        popupStage2.show();
        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                popupStage2.hide();
            }
        });
    }
    //TODO : change it
    public static void helpKeyEvents(){
        VBox pauseRoot2 = new VBox(5);
        pauseRoot2.getChildren().add(new Label("Paused"));
        pauseRoot2.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseRoot2.setAlignment(Pos.CENTER);
        pauseRoot2.setPadding(new Insets(20));
        pauseRoot2.getChildren().add(new Label("Shoot : Space\nIce mode: " +
                "Tab\nMove Right(Phase 4): " +
                "Right arrow\nMove Left(Phase 4): Left arrow"));
        Button okButton = new Button("OK");
        pauseRoot2.getChildren().add(okButton);
        Stage popupStage2 = new Stage(StageStyle.TRANSPARENT);
        popupStage2.initOwner(LoginMenu.stageOfProgram);
        popupStage2.initModality(Modality.APPLICATION_MODAL);
        popupStage2.setScene(new Scene(pauseRoot2, Color.TRANSPARENT));
        popupStage2.show();
        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                popupStage2.hide();
            }
        });
    }

    public static void setHandleForChangeMusicButtons(Button button , int numberOfSong) {
        button.setOnAction(e -> {
            Media song = new Media(new File(Paths.MUSICS_PATH.getPath() + numberOfSong + ".mp3")
                    .toURI().toString());
            songPlayer.stop();
            MediaPlayer mediaPlayer = new MediaPlayer(song);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(-1);
            if (GameMenu.isMute)
                mediaPlayer.setMute(true);
            else if (!isPlay) {
                mediaPlayer.pause();
            }
            songPlayer = mediaPlayer;
        });
    }

    public static void restartEvent(Stage popupStage) throws Exception {
        if (popupStage != null) {
            popupStage.close();
        }
        GameController.isPaused = false;

        Hero hero = new Hero("SHANA");
        Weapon weapon = PreGameMenu.weapon2;
        Ability ability = PreGameMenu.ability2;
        GameScreen newGame = new GameScreen();
        newGame.start(LoginMenu.stageOfProgram, 2 ,hero,weapon,ability, User.level);
    }

    public static Rectangle createPlayPauseIcon() {
        Rectangle icon = new Rectangle(350 , 10 , 20 , 20);
        icon.setFill(new ImagePattern(new Image
                (GameViewController.class.getResource("/images/icons/playIcon.png").toExternalForm())));
        icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isPlay) {
                    icon.setFill
                            (new ImagePattern(new Image(GameMenu.class.getResource
                                    ("/images/icons/pauseIcon.png").toExternalForm())));
                    isPlay = false;
                    songPlayer.pause();
                } else {
                    icon.setFill
                            (new ImagePattern(new Image(GameMenu.class.getResource
                                    ("/images/icons/playIcon.png").toExternalForm())));
                    isPlay = true;
                    songPlayer.play();
                }
            }
        });
        return icon;
    }

    public static void alertShowing(Alert.AlertType alertType , String title , String header ,
                                    String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
