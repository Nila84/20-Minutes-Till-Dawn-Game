package view.menu;

import controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.*;
import view.Paths;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PreGameMenu extends Application {

    @FXML
    private VBox heroesBox;
    @FXML
    private VBox weaponsBox;
    @FXML
    private Slider timeSlider;
    @FXML
    private Label timeLabel;

    private ToggleGroup heroToggleGroup;
    private ToggleGroup weaponToggleGroup;
    private int theTime;
    private Hero hero;

    public static Weapon weapon2;
    public static Ability ability2;
    public void setTheTime(int theTime) {
        this.theTime = theTime;
    }

    public int getTheTime() {
        return theTime;
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL preGameMenuFXMLUrl = PreGameMenu.class.getResource(Paths.PREGAME_MENU_FXML_FILE.getPath());
        FXMLLoader loader = new FXMLLoader(preGameMenuFXMLUrl);
        BorderPane borderPane = loader.load();

        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }

        Scene preGameMenuScene = new Scene(borderPane);
        stage.setScene(preGameMenuScene);
        stage.show();
    }

    @FXML
    public void initialize() {
        // Initialize heroes selection
        String[] heroes = {"SHANA", "DIAMOND", "SCARLET", "LILITH", "DASHER"};
        heroToggleGroup = new ToggleGroup();
        for (String hero : heroes) {
            RadioButton radioButton = new RadioButton(hero);
            radioButton.setToggleGroup(heroToggleGroup);
            radioButton.setUserData(hero);
            heroesBox.getChildren().add(radioButton);
        }
        ((RadioButton)heroToggleGroup.getToggles().get(0)).setSelected(true);

        // Initialize weapons selection
        String[] weapons = {"Revolver", "Shotgun", "SMGs Dual"};
        weaponToggleGroup = new ToggleGroup();
        for (String weapon : weapons) {
            RadioButton radioButton = new RadioButton(weapon);
            radioButton.setToggleGroup(weaponToggleGroup);
            radioButton.setUserData(weapon);
            weaponsBox.getChildren().add(radioButton);
        }
        ((RadioButton)weaponToggleGroup.getToggles().get(0)).setSelected(true);

        // Initialize time slider
        timeSlider.setMin(2);
        timeSlider.setMax(20);
        timeSlider.setValue(5);
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);
        timeSlider.setMajorTickUnit(5);
        timeSlider.setMinorTickCount(1);
        timeSlider.setSnapToTicks(true);

        timeLabel.setText("Game Time: 5 minutes");

        timeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int minutes = newVal.intValue();
            timeLabel.setText("Game Time: " + minutes + " minutes");
        });
    }

    @FXML
    private void startGame() {
        try {
            String selectedHero = heroToggleGroup.getSelectedToggle().getUserData().toString();
            String selectedWeapon = weaponToggleGroup.getSelectedToggle().getUserData().toString();
            int selectedTime = (int) timeSlider.getValue();

            System.out.println("Starting game with: " +
                    selectedHero + ", " + selectedWeapon + ", " + selectedTime + " minutes.");

            Hero hero = new Hero(selectedHero);
            Weapon weapon = new Weapon(selectedWeapon);
            weapon2 = weapon;
            //TODO : رندوم بودن توانایی
            Ability ability = new Ability("VITALITY");
            ability2 = ability;

            User user = App.getCurrentUser();
            if (user != null) {
                user.setSelectedHero(hero);
                user.setSelectedWeapon(weapon);
            }
            GameController.timeOfGame = selectedTime;
            Stage currentStage = (Stage) heroesBox.getScene().getWindow();
            currentStage.close();
            theTime = selectedTime;
            int level = User.level;
            System.out.println("start timee " + selectedTime);
            new GameScreen().start(new Stage(),selectedTime,hero,weapon, ability,level);
        } catch (Exception e) {
            System.err.println("Error in startGame: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to start game: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void back() {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void startResumedGame(SavedGameData savedData) {
        try {

            User user = App.getCurrentUser();
            Hero hero = new Hero(savedData.getHeroName());
            Weapon weapon = new Weapon(savedData.getWeaponName());
            Ability ability = new Ability(savedData.getAbilityName());


            weapon2 = weapon;
            ability2 = ability;

            user.setSelectedHero(hero);
            user.setSelectedWeapon(weapon);
            User.level = savedData.getLevel();
            user.setXP(savedData.getXp());
            hero.setHp2(savedData.getHp() - 1);
            System.out.println("**===" + hero.getHp());
            user.killNum = savedData.getKills();

            int remainingSeconds = (int) (savedData.getRemainingTime() / 1000L);
            long elapsedTime = (savedData.getTotalTime() * 1000L) - savedData.getRemainingTime();
            GameScreen.setGameDuration(remainingSeconds /1000);
            GameScreen.setStartTime((System.currentTimeMillis() - (remainingSeconds * 1000L))/1000);

            GameScreen gameScreen = new GameScreen();
            Stage gameStage = new Stage();
            System.out.println("time remaining " + remainingSeconds);
            gameScreen.start(gameStage, remainingSeconds / 60, hero, weapon, ability, savedData.getLevel());

            for (EnemyData enemyData : savedData.getEnemyDataList()) {
                Monster monster = GameViewController.createMonsterFromType(
                        enemyData.getType(),
                        enemyData.getX(),
                        enemyData.getY()
                );
                if (monster != null) {
                    monster.getShape().setCenterX(enemyData.getX());
                    monster.getShape().setCenterY(enemyData.getY());
                    gameScreen.addMonster(monster);
                }
            }

            if (savedData.getPlayerX() != 0 && savedData.getPlayerY() != 0) {
                Circle player = gameScreen.getPlayer();
                if (player != null) {
                    player.setCenterX(savedData.getPlayerX());
                    player.setCenterY(savedData.getPlayerY());
                }
            }
            // 8. تنظیم زمان بازی
//            gameScreen.setStartTime((System.currentTimeMillis() - elapsedTime)/1000);

            GameViewController.setupMovementControls(
                    gameScreen.getGamePane().getScene(),
                    gameScreen.getPlayer(),
                    hero.getSpeed()
            );


            if (gameScreen.gameTimer != null) gameScreen.gameTimer.stop();
            if (gameScreen.gameLoop != null) gameScreen.gameLoop.stop();
            gameScreen.gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!GameController.isPaused) {
//                        gameScreen.checkBussFight();
//                        gameScreen.checkPlayerObstacleCollision();
//                        gameScreen.checkImmunity();
//                        gameScreen.weapon.updateReload();
//                        gameScreen.ability.update();
//                        gameScreen.updateGameTime();
//                        gameScreen.updateMonsters();
//                        gameScreen.updateBullets();
//                        gameScreen.updateBulletsForMonster();
//                        gameScreen.updateParticles();
//                        gameScreen.checkBulletCollision();
//                        gameScreen.checkBulletCollisionMonster();
//                        gameScreen.checkExperienceOrbCollection();
//                        gameScreen.checkTentacleMonsterSpawn();
//                        gameScreen.checkEyebatMonsterSpawn();
//                        gameScreen.checkMonsterSpeedIncrease();
//                        gameScreen.monsters.forEach(Monster::updateKnockback);
                        for (Monster monster : GameScreen.getMonsters()) {
                            if (monster.isAlive()) {
                                monster.move(
                                        gameScreen.getPlayer().getCenterX(),
                                        gameScreen.getPlayer().getCenterY()
                                );
                                monster.updatePosition();
                            }
                        }
//                        gameScreen.updateGameUI(savedData);
                    }
                }
            };
            gameScreen.startGameTimer();
            gameScreen.gameLoop.start();

            GameViewController.setupMovementControls(
                    gameScreen.getGamePane().getScene(),
                    gameScreen.getPlayer(),
                    hero.getSpeed()
            );

        } catch (Exception e) {
            System.err.println("Error in startResumedGame: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to resume game: " + e.getMessage());
            alert.showAndWait();
        }
    }

}