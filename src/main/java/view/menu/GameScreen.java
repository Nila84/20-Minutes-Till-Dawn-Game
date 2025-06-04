package view.menu;

import controller.App;
import controller.GameController;
import controller.GameViewController;
import controller.SavedGameData;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen {
    private long lastTentacleSpawnTime = 0;
    private long lastEyebatSpawnTime = 0;
    private int eyebatSpawnCount = 0;
    public static List<Bullet> bullets = new ArrayList<>();
    private List<BulletForMonster> activeBullets = new ArrayList<>();
    private List<ExperienceOrb> experienceOrbs = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();
    public static List<Obstacle> obstacles = new ArrayList<>();
    private static GameScreen instance;
    private static final int PLAYER_RADIUS = 15;
    private static final double MOVEMENT_SPEED = 5.0;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private long lastSpawnTime = 0;
    private long lastSpeedIncreaseTime = 0;
    public static boolean isMute = false;
    private static boolean isImmune = false;
    private static boolean isImmune2 = false;
    private static long immuneStartTime = 0;
    private static final double PULSE_AMOUNT = 2.0;
    private static final double PULSE_SPEED = 0.1;
    private Timeline pulseTimeline;

    private static final long IMMUNE_DURATION = 2000;
    private static Hero hero;
    public static Ability ability;
    public Weapon weapon;
    private Text hpText;
    private Text kills;
    private Text weaponNum;
    private Text abilityType;
    private static Text levell;
    private static Circle player;
    public static Pane gamePane;
    private Text timeText;
    private static long startTime;
    public static long startTime2;
    private static int gameDuration;
    public static AnimationTimer gameTimer;
    private static List<Monster> monsters = new ArrayList<>();
    private boolean canShootAuto = false;
    private HBox levelProgressBar;
    private List<StackPane> levelBoxes = new ArrayList<>();
    public AnimationTimer gameLoop;
    private static boolean busFight = false;
    private static boolean canBussFight = true;
    public static Rectangle barrier;
    public static boolean barrierActive = false;
    private static Timeline barrierTimeline;
    private static final double INITIAL_BARRIER_SIZE = 100;
    private static final double BARRIER_SHRINK_DURATION = 30;
    private static final double MIN_BARRIER_SIZE = 200;
    private static final int BARRIER_DAMAGE = 2;
    private static long lastBarrierDamageTime = 0;
    private static final long BARRIER_DAMAGE_COOLDOWN = 1000;
    public static double cameraOffsetX = 0;
    public static double cameraOffsetY = 0;
    public static Pane worldPane = new Pane();


    public GameScreen() {
        instance = this;
    }

    public static GameScreen getInstance() {
        return instance;
    }

    public static List<Monster> getMonsters() {
        return monsters;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static int getGameDuration() {
        return gameDuration;
    }

    public static void setStartTime(long startTime) {
        GameScreen.startTime = startTime;
    }

    public static void setGameDuration(int gameDurationn) {
        gameDuration += gameDurationn;
    }

    public List<ExperienceOrb> getExperienceOrbs() {
        return experienceOrbs;
    }

    public static void setMonsters(List<Monster> monstersList) {
        monsters.clear();
        monsters.addAll(monstersList);
    }

    public static void setHero(Hero hero) {
        GameScreen.hero = hero;
    }

    public static Ability getAbility() {
        return ability;
    }

    public static void showAbilities() {
        GameController.isPaused = true;
        List<Ability> acquiredAbilities = App.getCurrentUser().getAcquiredAbilities();
        if (acquiredAbilities == null) {
            System.err.println("Error: acquiredAbilities is null");
            return;
        }

        Pane abilitiesPane = new Pane();
        abilitiesPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
        abilitiesPane.setPrefSize(WIDTH, HEIGHT);

        Text title = new Text("Acquired Abilities");
        title.setFont(Font.font("Verdana", 32));
        title.setFill(Color.GOLD);
        title.setLayoutX(WIDTH / 2 - 120);
        title.setLayoutY(80);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(50);
        scrollPane.setLayoutY(120);
        scrollPane.setPrefSize(WIDTH - 100, HEIGHT - 200);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox abilitiesList = new VBox(15);
        abilitiesList.setPadding(new Insets(15));
        abilitiesList.setStyle("-fx-background-color: rgba(50, 50, 50, 0.7);");


        if (acquiredAbilities.isEmpty()) {
            Label noAbilities = new Label("You haven't acquired any abilities yet!");
            noAbilities.setFont(Font.font(20));
            noAbilities.setTextFill(Color.LIGHTGRAY);
            abilitiesList.getChildren().add(noAbilities);
        } else {
            for (Ability ability : acquiredAbilities) {
                abilitiesList.getChildren().add(createAbilityCard(ability));
            }
        }

        scrollPane.setContent(abilitiesList);

        Button okButton = new Button("OK");
        okButton.setPrefSize(120, 40);
        okButton.setStyle("-fx-font-size: 18; -fx-background-color: #444; -fx-text-fill: white;");
        okButton.setLayoutX(WIDTH / 2 - 60);
        okButton.setLayoutY(HEIGHT - 70);
        okButton.setOnAction(e -> {
            GameScreen.getInstance().gamePane.getChildren().remove(abilitiesPane);
            GameController.isPaused = false;
        });

        abilitiesPane.getChildren().addAll(title, scrollPane, okButton);
        GameScreen.getInstance().gamePane.getChildren().add(abilitiesPane);
    }

    private static Pane createAbilityCard(Ability ability) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: rgba(30, 30, 30, 0.8); -fx-background-radius: 10; -fx-border-radius: 10;");

        Label nameLabel = new Label(ability.getName());
        nameLabel.setFont(Font.font("Verdana", 20));
        nameLabel.setTextFill(Color.LIGHTCORAL);

        Text description = new Text(ability.getDescription());
        description.setFont(Font.font(16));
        description.setFill(Color.LIGHTGRAY);
        description.setWrappingWidth(WIDTH - 150);

        if (ability.getDuration() > 0) {
            Label durationLabel = new Label("Duration: " + ability.getDuration() + " seconds");
            durationLabel.setFont(Font.font(14));
            durationLabel.setTextFill(Color.LIGHTBLUE);
            card.getChildren().add(durationLabel);
        }

        card.getChildren().addAll(nameLabel, description);
        return card;
    }

    public void addMonsterToGame(Monster monster) {
        monsters.add(monster);
        if (!gamePane.getChildren().contains(monster.getShape())) {
            gamePane.getChildren().add(monster.getShape());
        }
        monster.updatePosition();
//        gamePane.getChildren().add(monster.getShape());
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
        gamePane.getChildren().add(monster.getShape());
    }

    public void start(Stage stage, int minutes, Hero selectedHero , Weapon weapon, Ability ability,int level) {
        worldPane.setPrefSize(WIDTH , HEIGHT);
        App.getCurrentUser().killNumber = 0;
        App.getCurrentUser().score = 0;
        this.hero = selectedHero;
        this.weapon = weapon;
        this.gameDuration = minutes * 60;
        this.startTime = System.currentTimeMillis();
        System.out.println("timmmme" + startTime);
        startTime2 = startTime;
        this.ability = ability;

        ability.activate();

        gamePane = new Pane();
        gamePane.setFocusTraversable(true);
        gamePane.setPrefSize(WIDTH, HEIGHT);
        if (GameViewController.isLightThemeOn) {
            gamePane.setStyle("-fx-background-color: #ffffff;");
        } else {
            gamePane.setStyle("-fx-background-color: #333333;");
        }
        gamePane.getChildren().add(GameViewController.createMuteUnmuteIcon());
        gamePane.getChildren().add(GameViewController.createMenuIcon(gamePane));
        gamePane.getChildren().add(worldPane);


        if (hero.getName().equals("SHANA")) player = new Circle(PLAYER_RADIUS, Color.PURPLE);
        else if (hero.getName().equals("DIAMOND")) player = new Circle(PLAYER_RADIUS, Color.PINK);
        else if (hero.getName().equals("LILITH")) player = new Circle(PLAYER_RADIUS, Color.GREEN);
        else if (hero.getName().equals("SCARLET")) player = new Circle(PLAYER_RADIUS, Color.GRAY);
        else if (hero.getName().equals("DASHER")) player = new Circle(PLAYER_RADIUS, Color.BLUE);
        Pane viewportPane = new Pane(); // پنی که فقط بخش قابل مشاهده را نشان می‌دهد
        viewportPane.setPrefSize(WIDTH, HEIGHT);
        viewportPane.setClip(new Rectangle(WIDTH, HEIGHT));

        worldPane.getChildren().addAll(/* تمام المان‌های بازی */);
        viewportPane.getChildren().add(worldPane);

        Scene scene = new Scene(viewportPane, WIDTH, HEIGHT);
        player.setCenterX(WIDTH / 2.0);
        player.setCenterY(HEIGHT / 2.0);
        setupPulseAnimation();

        timeText = new Text();
        timeText.setFont(new Font(20));
//        timeText.setFill(Color.PINK);
        timeText.setLayoutX(WIDTH - 150);
        timeText.setLayoutY(30);
        hpText = new Text();
        hpText.setFont(new Font(20));
//        hpText.setFill(Color.YELLOW);
        hpText.setLayoutX(WIDTH - 150);
        hpText.setLayoutY(60); // پایین‌تر از تایمر
        kills = new Text();
        kills.setFont(new Font(20));
//        kills.setFill(Color.GREEN);
        kills.setLayoutX(WIDTH - 150);
        kills.setLayoutY(90);
        weaponNum = new Text();
        weaponNum.setFont(new Font(15));
//        weaponNum.setFill(Color.CYAN);
        weaponNum.setLayoutX(WIDTH - 150);
        weaponNum.setLayoutY(120);
        abilityType = new Text();
        abilityType.setFont(new Font(15));
//        abilityType.setFill(Color.GREEN);
        abilityType.setLayoutX(WIDTH - 150);
        abilityType.setLayoutY(140);
        levell = new Text();
        levell.setFont(new Font(15));
//        levell.setFill(Color.CYAN);
        levell.setLayoutX(WIDTH - 150);
        levell.setLayoutY(160);


        if (GameViewController.isLightThemeOn) {
            timeText.setFill(Color.BLACK);
            hpText.setFill(Color.DARKBLUE);
            kills.setFill(Color.DARKGREEN);
            weaponNum.setFill(Color.DARKCYAN);
            abilityType.setFill(Color.DARKGREEN);
            levell.setFill(Color.DARKCYAN);
        } else {
            timeText.setFill(Color.PINK);
            hpText.setFill(Color.YELLOW);
            kills.setFill(Color.GREEN);
            weaponNum.setFill(Color.CYAN);
            abilityType.setFill(Color.GREEN);
            levell.setFill(Color.CYAN);
        }


        gamePane.getChildren().addAll(player, timeText);
        gamePane.getChildren().add(hpText);
        gamePane.getChildren().add(kills);
        gamePane.getChildren().add(weaponNum);
        gamePane.getChildren().add(abilityType);
        gamePane.getChildren().add(levell);

        scene = new Scene(gamePane, WIDTH, HEIGHT);

        gamePane.setOnKeyPressed(event -> {
            System.out.println("KEY PRESSED IN GAMEPANE: " + event.getCode());
            if (event.getCode() == KeyCode.R ) {
                System.out.println("Manual reload");
                weapon.startReload();
            }
            switch (event.getCode()) {
                case I: shootInDirection(0, -1); break;
                case K: shootInDirection(0, 1); break;
                case J: shootInDirection(-1, 0); break;
                case L: shootInDirection(1, 0); break;
                // TODO :  8 جهت کنم
                case SPACE: canShootAuto = !canShootAuto; break;
            }
        });

        gamePane.setOnMouseClicked(event -> {
            if (canShootAuto) shootNearestEnemy();
            else if (event.getButton() == MouseButton.PRIMARY) {
                double centerX = player.getCenterX();
                double centerY = player.getCenterY();

                double dx = event.getX() - centerX;
                double dy = event.getY() - centerY;

                double length = Math.sqrt(dx * dx + dy * dy);
                if (length > 0) {
                    dx /= length;
                    dy /= length;
                }

                shootInDirection(dx, dy);
            }
        });
        //TODO : درست کردن موس

//        Circle mousePointer = new Circle(5);
//        mousePointer.setStrokeWidth(2);
//        mousePointer.setVisible(false);
//        gamePane.getChildren().add(mousePointer);
//
//        gamePane.setOnMouseMoved(event -> {
//            mousePointer.setCenterX(event.getX());
//            mousePointer.setCenterY(event.getY());
//        });
//
//        gamePane.setOnMouseEntered(event -> mousePointer.setVisible(true));
//        gamePane.setOnMouseExited(event -> mousePointer.setVisible(false));
//

        addGlowToPlayer();
        addObstacles();
        initializeLevelProgressBar(gamePane);
        GameViewController.setupMovementControls(scene, player,hero.getSpeed());

        if (monsters == null || monsters.isEmpty()) {
            spawnMonsters(5);
        }
        for (Monster monster : monsters) {
            if (!gamePane.getChildren().contains(monster.getShape())) {
                gamePane.getChildren().add(monster.getShape());
            }
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
            }
        };
        gameLoop.start();



        startGameTimer();

        stage.setScene(scene);
        stage.setTitle("Game");
        stage.show();
        gamePane.requestFocus();
    }

    private Color getPlayerColor() {
        return hero.getName().equals("SHANA") ? Color.PURPLE :
                hero.getName().equals("DIAMOND") ? Color.PINK :
                        hero.getName().equals("LILITH") ? Color.GREEN :
                                hero.getName().equals("SCARLET") ? Color.GRAY : Color.BLUE;
    }

    void updateGameUI(SavedGameData savedData) {
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = Math.max(gameDuration - elapsedSeconds, 0);
        timeText.setText("Time: " + remaining + "s");
        hpText.setText("HP: " + hero.getHp());
        kills.setText("Kills: " + savedData.getKills());
        weaponNum.setText("Weapon: " + savedData.getWeaponName());
        abilityType.setText("Ability: " + savedData.getAbilityName());
        levell.setText("Level: " + User.level);
    }

    private void addObstacles() {
        Random rand = new Random();
        int numberOfObstacles = 4;

        for (int i = 0; i < numberOfObstacles; i++) {
            double radius = 15;
            double x = radius + rand.nextDouble() * (WIDTH - 2 * radius);
            double y = radius + rand.nextDouble() * (HEIGHT - 2 * radius);

            Obstacle obstacle = new Obstacle(x, y, radius, 2);
            obstacles.add(obstacle);
            gamePane.getChildren().add(obstacle.getShape());
        }
    }

    public void checkPlayerObstacleCollision() {
        double px = player.getCenterX();
        double py = player.getCenterY();
        double pr = player.getRadius();

        for (Obstacle o : obstacles) {
            double ox = o.getX();
            double oy = o.getY();
            double or = o.getRadius();

            double dx = px - ox;
            double dy = py - oy;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < pr + or) {
                if (!isImmune) {
                    hero.setHp(-o.getDamageOnCollision());
                    isImmune = true;
                    immuneStartTime = System.currentTimeMillis();
//                    updateHpText();
                }
            }
        }
    }


    private void highlightTarget(Monster target) {
        Circle highlight = new Circle(target.getShape().getRadius() + 15);
        highlight.setFill(Color.TRANSPARENT);
        highlight.setStroke(Color.YELLOW);
        highlight.setStrokeWidth(3);
        highlight.setCenterX(target.getX());
        highlight.setCenterY(target.getY());

        Glow glow = new Glow(0.8);
        highlight.setEffect(glow);

        gamePane.getChildren().add(highlight);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), highlight);
        fade.setFromValue(1.0);
        fade.setToValue(0);
        fade.setOnFinished(e -> gamePane.getChildren().remove(highlight));
        fade.play();
    }

    private void shootNearestEnemy() {
        System.out.println("Space pressed - trying to shoot!");
        if (!weapon.fire()) {
            System.out.println("No ammo!");
            return;
        }

        if (monsters.isEmpty()) return;

        Monster nearest = null;
        double minDistance = Double.MAX_VALUE;

        double px = player.getCenterX();
        double py = player.getCenterY();

        for (Monster monster : monsters) {
            if (!monster.isAlive()) continue;

            double dx = monster.getX() - px;
            double dy = monster.getY() - py;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = monster;
            }
        }

        if (nearest != null) {
            highlightTarget(nearest);
            int projectileCount = App.getCurrentUser().getSelectedWeapon().getProjectileCount();

            Timeline timeline = new Timeline();
            for (int i = 0; i < projectileCount; i++) {
                int index = i;
                Monster finalNearest = nearest;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(index * 50), event -> {
                    BulletForMonster bullet = new BulletForMonster(px, py, finalNearest.getX(), finalNearest.getY());
                    bullet.getShape().setFill(Color.RED);
                    bullet.setTarget(finalNearest);
                    bullet.setDamage(weapon.getTotalDamage());

                    activeBullets.add(bullet);
                    gamePane.getChildren().add(bullet.getShape());

                    animateBullet(bullet);
                    GameMenu.playSFX();
                }));
            }
            timeline.play();
        }
    }

    private void animateBullet(BulletForMonster bullet) {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            bullet.update(); // متد `update` برای حرکت طبیعی تیر
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }



    private void spawnMonsters(int count) {
        for (int i = 0; i < count; i++) {
            Monster monster = GameController.createRandomMonster();
            monsters.add(monster);
            gamePane.getChildren().add(monster.getShape());
        }
    }

    public static void spawnMonstersCheat(int count) {
        for (int i = 0; i < count; i++) {
            Monster monster = createRandomMonsterCheat();
            getMonsters().add(monster);
            gamePane.getChildren().add(monster.getShape());
        }
    }

    public static Monster createRandomMonsterCheat() {
        Random random = new Random();
        double x = random.nextDouble() * 800;
        double y = random.nextDouble() * 600;
        Color color;

        return switch (random.nextInt(3)) {
            case 0 -> new ElderMonster(x, y, Color.ORANGE);
            case 1 -> new ElderMonster(x, y, Color.ORANGE);
            case 2 -> new ElderMonster(x, y, Color.ORANGE);
            default -> new ElderMonster(x, y, Color.ORANGE);
        };
    }


    public void startGameTimer() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!GameController.isPaused) {
                    checkBussFight();
                    restrictPlayerMovement();
                    checkPlayerObstacleCollision();
                    checkBarrierCollision();
                    checkImmunity();
                    weapon.updateReload();
                    ability.update();
                    updateGameTime();
                    updateMonsters();
                    updateBullets();
                    updateBulletsForMonster();
                    updateParticles();
                    checkBulletCollision();
                    checkBulletCollisionMonster();
                    checkExperienceOrbCollection();
                    checkTentacleMonsterSpawn();
                    checkEyebatMonsterSpawn();
                    checkMonsterSpeedIncrease();
                    monsters.forEach(Monster::updateKnockback);
                }
            }
        };
        gameTimer.start();
    }

    public void checkBussFight() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedTime >= gameDuration / 2 && canBussFight) {
            spawnElderMonster();
            createBarrier();
            canBussFight = false;
        }

        if (busFight && monsters.stream().noneMatch(m -> m instanceof ElderMonster)) {
            busFight = false;
            removeBarrier();
        }
    }

    private void spawnElderMonster() {
        for (int i = 0; i < 8; i++) {
            Random rand = new Random();
            double x = rand.nextDouble() * (WIDTH - 100) + 50;
            double y = rand.nextDouble() * (HEIGHT - 100) + 50;
            ElderMonster elder = new ElderMonster(x, y, Color.ORANGE);
            addMonsterToGame(elder);
        }
        busFight = true;
    }

    public static Monster createRandomElderMonster() {
        Random random = new Random();
        double x = random.nextDouble() * 800;
        double y = random.nextDouble() * 600;
        Color color;
        return new ElderMonster(x, y, Color.ORANGE);
    }

    public void updateBullets() {
        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isOutOfBounds(WIDTH, HEIGHT)) {
                toRemove.add(bullet);
                gamePane.getChildren().remove(bullet.getShape());
            }
        }
        bullets.removeAll(toRemove);
    }


    public void checkBulletCollision() {
        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            double dx = player.getCenterX() - bullet.getShape().getCenterX();
            double dy = player.getCenterY() - bullet.getShape().getCenterY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < PLAYER_RADIUS + bullet.getShape().getRadius() && !isImmune) {
                toRemove.add(bullet);
                gamePane.getChildren().remove(bullet.getShape());
                createHitEffect();

                hero.setHp(- 1);
                GameMenu.playDamage();
                System.out.println("Player hit by bullet! HP left: " + hero.getHp());
                if (hero.getHp() <= 0) {
                    System.out.println("Player died!");
                    App.getCurrentUser().setGameOver(1);
                    gameTimer.stop();
                    showGameOverScreen();
                }

            }
        }
        bullets.removeAll(toRemove);
    }


    public void checkBulletCollisionMonster() {
        List<BulletForMonster> bulletsToRemove = new ArrayList<>();
        List<Monster> monstersToRemove = new ArrayList<>();

        for (BulletForMonster bullet : activeBullets) {
            for (Monster monster : monsters) {
                if (!monster.isAlive()) continue;

                double dx = bullet.getShape().getCenterX() - monster.getX();
                double dy = bullet.getShape().getCenterY() - monster.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < bullet.getShape().getRadius() + monster.getShape().getRadius()) {
                    createHitParticles(monster);
                    monster.takeDamage(weapon.getDamagePerProjectile());
                    monster.startKnockback(player.getCenterX(), player.getCenterY());
                    monster.takeDamage(weapon.getDamagePerProjectile());
                    bulletsToRemove.add(bullet);

                    if (!monster.isAlive()) {
                        createSmokeEffect(monster.getX(), monster.getY());
                        monstersToRemove.add(monster);
                        App.getCurrentUser().killNumber++;
                        ExperienceOrb orb = new ExperienceOrb(monster.getX(), monster.getY());
                        experienceOrbs.add(orb);
                        gamePane.getChildren().add(orb.getShape());
                    }
                    break;
                }
            }
        }

        activeBullets.removeAll(bulletsToRemove);
        bulletsToRemove.forEach(b -> gamePane.getChildren().remove(b.getShape()));

        monsters.removeAll(monstersToRemove);
        monstersToRemove.forEach(m -> gamePane.getChildren().remove(m.getShape()));
    }

    public void checkExperienceOrbCollection() {
        List<ExperienceOrb> orbsToRemove = new ArrayList<>();

        for (ExperienceOrb orb : experienceOrbs) {
            orb.update();

            double dx = player.getCenterX() - orb.getX();
            double dy = player.getCenterY() - orb.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < PLAYER_RADIUS + orb.getShape().getRadius() && !orb.isCollected()) {
                orb.setCollected(true);
                orbsToRemove.add(orb);
                gamePane.getChildren().remove(orb.getShape());

                App.getCurrentUser().setXP(App.getCurrentUser().getXP() + 3);
                updateLevelProgressBar(App.getCurrentUser().getXP()/3);
                if (App.getCurrentUser().getXP() % (6 * User.level) == 0) {
                    showAbilitySelectionMenu();
                }
                GameMenu.playSFX();
            }
            if (orb.getY() > HEIGHT) {
                orbsToRemove.add(orb);
                gamePane.getChildren().remove(orb.getShape());
            }
        }

        experienceOrbs.removeAll(orbsToRemove);
    }

    public static void showAbilitySelectionMenu() {
        GameController.isPaused = true;

        Pane abilityPane = new Pane();
        abilityPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        abilityPane.setPrefSize(WIDTH, HEIGHT);

        Text title = new Text("Choose an Ability!");
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.WHITE);
        title.setLayoutX(WIDTH / 2 - 120);
        title.setLayoutY(100);

        List<Ability> randomAbilities = Ability.getThreeUniqueRandomAbilities();
        VBox abilitiesBox = new VBox(20);
        abilitiesBox.setLayoutX(WIDTH / 2 - 150);
        abilitiesBox.setLayoutY(150);

        for (Ability ability : randomAbilities) {
            Button abilityButton = createAbilityButton(ability);

            abilityButton.setOnAction(e -> {
                if (App.getCurrentUser().getAcquiredAbilities() == null) {
                    System.err.println("Error: acquiredAbilities is null");
                    return;
                }
                ability.activate();
                App.getCurrentUser().addAbility(ability);
                App.getCurrentUser().level++;
                levell.setText("Level: " + App.getCurrentUser().level);
                gamePane.getChildren().remove(abilityPane);
                GameController.isPaused = false;

                showAbilityAcquiredMessage(ability.getName());
            });

            abilitiesBox.getChildren().add(abilityButton);
        }        abilityPane.getChildren().addAll(title, abilitiesBox);
        gamePane.getChildren().add(abilityPane);
    }

    private static void showAbilityAcquiredMessage(String abilityName) {
        Text message = new Text("Acquired: " + abilityName);
        message.setFont(Font.font("Verdana", 20));
        message.setFill(Color.GOLD);
        message.setLayoutX(WIDTH / 2 - 100);
        message.setLayoutY(HEIGHT - 50);

        gamePane.getChildren().add(message);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> gamePane.getChildren().remove(message));
        delay.play();
    }

    private static Button createAbilityButton(Ability ability) {
        Button button = new Button();
        button.setPrefSize(300, 80);
        button.setStyle("-fx-background-color: #444444; -fx-text-fill: white;");

        VBox content = new VBox(5);

        Text nameText = new Text(ability.getName());
        nameText.setFont(Font.font(18));
        nameText.setFill(Color.YELLOW);

        Text descText = new Text(ability.getDescription());
        descText.setFont(Font.font(14));
        descText.setFill(Color.WHITE);
        descText.setWrappingWidth(280);

        content.getChildren().addAll(nameText, descText);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(5));

        button.setGraphic(content);
        return button;
    }

    public void checkMonsterSpeedIncrease() {
        boolean done = false;
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        if (elapsedSeconds - lastSpeedIncreaseTime >= 7) {
            for (Monster monster : monsters) {
                if (monster instanceof ElderMonster) {
                    monster.setSpeed(monster.getSpeed() * 2);
                    done = true;
                }
            }
            if (done)lastSpeedIncreaseTime = elapsedSeconds;
        }
    }


    public void updateGameTime() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        long remainingTime = gameDuration - elapsedTime;

        if (remainingTime <= 0) {
            remainingTime = 0;
            gameTimer.stop();
            System.out.println("You Won :)");
            App.getCurrentUser().setWon(1);
            winScreen();
        }

        String formattedTime = String.format("%02d:%02d", remainingTime / 60, remainingTime % 60);
        timeText.setText("Time: " + formattedTime);
        hpText.setText("HP: " + hero.getHp());
        kills.setText("Kills: " + App.getCurrentUser().killNumber);
        weaponNum.setText("Weapons: " + weapon.getCurrentAmmo());
        abilityType.setText("Ability: " + ability.getName());
        levell.setText("Level: " + App.getCurrentUser().level);
    }

    public static void updateMonsters() {
        long now = System.currentTimeMillis();
        double playerX = player.getCenterX();
        double playerY = player.getCenterY();

        for (Monster monster : monsters) {
            monster.checkSpawn();
            if (monster.isAlive()) {
                if (!monster.isPulsing()) {
                    monster.startPulseAnimation();
                }
                monster.moveTowards(player);
                monster.getShape().setCenterX(monster.getX());
                monster.getShape().setCenterY(monster.getY());

                if (monster instanceof EyebatMonster eyebat) {
                    if (eyebat.canShoot(now)) {
                        eyebat.shot(now);
                        Bullet bullet = new Bullet(
                                eyebat.getX(), eyebat.getY(),
                                player.getCenterX(), player.getCenterY()
                        );
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet.getShape());
                        System.out.println("Eyebat shooting!");
                    }
                }
                if (monster instanceof ElderMonster elderMonster) {
                    if (elderMonster.canShoot(now)) {
                        elderMonster.shot(now);
                        Bullet bullet = new Bullet(
                                elderMonster.getX(), elderMonster.getY(),
                                player.getCenterX(), player.getCenterY()
                        );
                        bullets.add(bullet);
                        gamePane.getChildren().add(bullet.getShape());
                        System.out.println("Elder shooting!");
                    }
                }

                double dx = playerX - monster.getX();
                double dy = playerY - monster.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < PLAYER_RADIUS + monster.getShape().getRadius() && !isImmune) {
                    isImmune = true;
                    immuneStartTime = now;
                    addImmuneEffect();
                    if (hero.getHp() <= 0) {
                        System.out.println("Player died!");
                        App.getCurrentUser().setGameOver(1);
                        gameTimer.stop();
                        showGameOverScreen();
                    }
                    System.out.println("Player hit by monster!");
                }
            }
        }
    }

    private static void addImmuneEffect() {
        Timeline blinkTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(player.fillProperty(), Color.WHITE)
                ),
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(player.fillProperty(), hero.getName().equals("SHANA") ? Color.PURPLE :
                                hero.getName().equals("DIAMOND") ? Color.PINK :
                                        hero.getName().equals("LILITH") ? Color.GREEN :
                                                hero.getName().equals("SCARLET") ? Color.GRAY : Color.BLUE)
                )
        );
        blinkTimeline.setCycleCount((int)(IMMUNE_DURATION / 100));
        blinkTimeline.play();

        Glow glow = new Glow(0.8);
        glow.setInput(new DropShadow(15, Color.WHITE));
        player.setEffect(glow);
    }

    public void checkTentacleMonsterSpawn() {
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;

        if (elapsedSeconds >= 30) {
            if (elapsedSeconds - lastTentacleSpawnTime >= 3) {
                lastTentacleSpawnTime = elapsedSeconds;

                Random rand = new Random();
                double x = rand.nextDouble() * (WIDTH - 100) + 50;
                double y = rand.nextDouble() * (HEIGHT - 100) + 50;

                TentacleMonster newMonster = new TentacleMonster(x, y, Color.YELLOW);
                addMonsterToGame(newMonster);
                lastTentacleSpawnTime = elapsedSeconds;
            }
        }
    }
    public void checkEyebatMonsterSpawn() {
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        int totalGameSeconds = gameDuration;

        if (elapsedSeconds >= totalGameSeconds / 4) {
            int expectedSpawnCount = (int) ((4 * elapsedSeconds - totalGameSeconds + 30) / 30);

            while (eyebatSpawnCount < expectedSpawnCount) {
                spawnEyebatMonster();
                eyebatSpawnCount++;
            }
        }
    }
    private static void showGameOverScreen() {
        long survivalTime = System.currentTimeMillis() - startTime;
        App.getCurrentUser().updateSurvivalTime(survivalTime);
        Pane resultPane = new Pane();
        resultPane.setStyle("-fx-background-color: black;");
        resultPane.setPrefSize(WIDTH, HEIGHT);

        long endTime = System.currentTimeMillis();
        long totalTimeSeconds = (endTime - startTime) / 1000;

        String username = App.getCurrentUser().getUsername();
        int gameOverNum = App.getCurrentUser().getGameOver();
        int killCount = App.getCurrentUser().killNumber;
        long score = totalTimeSeconds * killCount;

        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Verdana", 40));
        gameOverText.setFill(Color.RED);
        gameOverText.setLayoutX(WIDTH / 2.0 - 130);
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
        User currentUser = App.getCurrentUser();
        App.getCurrentUser().killNum += App.getCurrentUser().killNumber;
        App.getCurrentUser().scores += score;
        currentUser.updateUserStats(App.getCurrentUser().killNum, App.getCurrentUser().getScore(), survivalTime);

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back to Main Menu");
        backButton.setLayoutX(100);
        backButton.setLayoutY(470);
        backButton.setOnAction(event -> back());

        resultPane.getChildren().addAll(gameOverText, userText,gameOver, timeText, killsText, scoreText, backButton);

        Scene resultScene = new Scene(resultPane, WIDTH, HEIGHT);
        Stage stage = (Stage) gamePane.getScene().getWindow();
        stage.setScene(resultScene);
    }

    private void winScreen() {
        long survivalTime = System.currentTimeMillis() - startTime;
        App.getCurrentUser().updateSurvivalTime(survivalTime);
        Pane resultPane = new Pane();
        resultPane.setStyle("-fx-background-color: black;");
        resultPane.setPrefSize(WIDTH, HEIGHT);

        long endTime = System.currentTimeMillis();
        long totalTimeSeconds = (endTime - startTime) / 1000;

        String username = App.getCurrentUser().getUsername();
        int gameOverNum = App.getCurrentUser().getWon();
        int killCount = App.getCurrentUser().killNumber;
        long score = totalTimeSeconds * killCount;

        Text gameOverText = new Text("YOU WON:)");
        gameOverText.setFont(Font.font("Verdana", 40));
        gameOverText.setFill(Color.RED);
        gameOverText.setLayoutX(WIDTH / 2.0 - 130);
        gameOverText.setLayoutY(150);

        Text userText = new Text("Username: " + username);
        userText.setFont(Font.font(24));
        userText.setFill(Color.WHITE);
        userText.setLayoutX(100);
        userText.setLayoutY(250);

        Text gameOver = new Text("Tou won " + gameOverNum + " games!");
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
        User currentUser = App.getCurrentUser();
        App.getCurrentUser().killNum += App.getCurrentUser().killNumber;
        App.getCurrentUser().scores += score;
        currentUser.updateUserStats(App.getCurrentUser().killNum, App.getCurrentUser().getScore(), survivalTime);
        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back to Main Menu");
        backButton.setLayoutX(100);
        backButton.setLayoutY(470);
        backButton.setOnAction(event -> back());
        resultPane.getChildren().addAll(gameOverText, userText,gameOver, timeText, killsText, scoreText, backButton);
        Scene resultScene = new Scene(resultPane, WIDTH, HEIGHT);
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

    private void spawnEyebatMonster() {
        Random rand = new Random();
        double x = rand.nextDouble() * (WIDTH - 100) + 50;
        double y = rand.nextDouble() * (HEIGHT - 100) + 50;
        EyebatMonster eyebat = new EyebatMonster(x, y, Color.RED);
        addMonsterToGame(eyebat);
    }


    private void shootInDirection(double dirX, double dirY) {
        GameMenu.playSFX();
        System.out.println("Attempting to shoot in direction: " + dirX + "," + dirY);

        if (weapon == null) {
            System.out.println("WEAPON IS NULL! Cannot shoot.");
            return;
        }

        System.out.println("Current ammo: " + weapon.getCurrentAmmo() + "/" + weapon.getMaxAmmo());
        if (!weapon.fire()) {
            System.out.println("No ammo!");
            return;
        }
        int projectileCount = weapon.getProjectileCount();
        double spreadAngle = 15.0;

        double px = player.getCenterX();
        double py = player.getCenterY();
        double targetX = px + (dirX * 1000);
        double targetY = py + (dirY * 1000);
        for (int i = 0; i < projectileCount; i++) {
            // محاسبه زاویه برای تیرهای چندگانه
            double angleOffset = (i - (projectileCount - 1) / 2.0) * spreadAngle;
            double radianOffset = Math.toRadians(angleOffset);

            // محاسبه جهت جدید با در نظر گرفتن انحراف
            double newDirX = dirX * Math.cos(radianOffset) - dirY * Math.sin(radianOffset);
            double newDirY = dirX * Math.sin(radianOffset) + dirY * Math.cos(radianOffset);

            // نرمالایز کردن جهت
            double length = Math.sqrt(newDirX * newDirX + newDirY * newDirY);
            if (length > 0) {
                newDirX /= length;
                newDirY /= length;
            }
            BulletForMonster bullet = new BulletForMonster(px, py, targetX, targetY);
            bullet.setDamage(weapon.getTotalDamage());

            switch(weapon.getName()) {
                case "Revolver":
                    bullet.getShape().setFill(Color.CYAN);
                    break;
                case "Shotgun":
                    bullet.getShape().setFill(Color.RED);
                    break;
                case "SMGs Dual":
                    bullet.getShape().setFill(Color.PURPLE);
                    break;
            }

            activeBullets.add(bullet);
            gamePane.getChildren().add(bullet.getShape());
        }

//        BulletForMonster bullet = new BulletForMonster(px, py, targetX, targetY);
//        bullet.setDamage(weapon.getTotalDamage());
//
//        switch(weapon.getName()) {
//            case "Revolver":
//                bullet.getShape().setFill(Color.CYAN);
//                break;
//            case "Shotgun":
//                bullet.getShape().setFill(Color.RED);
//                break;
//            case "SMGs Dual":
//                bullet.getShape().setFill(Color.PURPLE);
//                break;
//        }
//
//        activeBullets.add(bullet);
//        gamePane.getChildren().add(bullet.getShape());

        System.out.println("Bullet added successfully. Total bullets: " + activeBullets.size());
    }
//    private void shootInDirection(double dirX, double dirY) {
//        GameMenu.playSFX();
//        // موقعیت گلوله نسبت به دوربین
//        double bulletX = player.getCenterX() - cameraOffsetX;
//        double bulletY = player.getCenterY() - cameraOffsetY;
//
//        BulletForMonster bullet = new BulletForMonster(bulletX, bulletY, dirX, dirY);
//        bullet.setDamage(weapon.getTotalDamage());
//
//        switch(weapon.getName()) {
//            case "Revolver":
//                bullet.getShape().setFill(Color.CYAN);
//                break;
//            case "Shotgun":
//                bullet.getShape().setFill(Color.RED);
//                break;
//            case "SMGs Dual":
//                bullet.getShape().setFill(Color.PURPLE);
//                break;
//        }
//        activeBullets.add(bullet);
//        worldPane.getChildren().add(bullet.getShape());
//    }
    public void updateBulletsForMonster() {
        System.out.println("Updating bullets. Count: " + activeBullets.size());

        List<BulletForMonster> toRemove = new ArrayList<>();

        for (BulletForMonster bullet : activeBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(WIDTH, HEIGHT)) {
                System.out.println("Removing out-of-bounds bullet");
                toRemove.add(bullet);
                gamePane.getChildren().remove(bullet.getShape());
            }
        }

        activeBullets.removeAll(toRemove);
        System.out.println("Bullets after update: " + activeBullets.size());
    }

    public static void createSmokeEffect(double x, double y) {
        Circle smoke = new Circle(15);
        smoke.setCenterX(x);
        smoke.setCenterY(y);
        smoke.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GRAY),
                new Stop(1, Color.TRANSPARENT)));

        gamePane.getChildren().add(smoke);

        FadeTransition fade = new FadeTransition(Duration.millis(500), smoke);
        fade.setFromValue(0.8);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), smoke);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.3);
        scale.setToY(0.3);

        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.setOnFinished(e -> gamePane.getChildren().remove(smoke));
        parallel.play();
    }


    private void createHitEffect() {
        Circle hitEffect = new Circle(PLAYER_RADIUS + 5, Color.rgb(255, 0, 0, 0.5));
        hitEffect.setCenterX(player.getCenterX());
        hitEffect.setCenterY(player.getCenterY());

        gamePane.getChildren().add(gamePane.getChildren().indexOf(player), hitEffect);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(hitEffect.opacityProperty(), 0.7),
                        new KeyValue(hitEffect.radiusProperty(), PLAYER_RADIUS + 5)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(hitEffect.opacityProperty(), 0),
                        new KeyValue(hitEffect.radiusProperty(), PLAYER_RADIUS + 20)
                )
        );

        timeline.setOnFinished(e -> gamePane.getChildren().remove(hitEffect));
        timeline.play();

        TranslateTransition shake = new TranslateTransition(Duration.millis(100), player);
        shake.setByX(5);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void addGlowToPlayer() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(20);
        glow.setSpread(0.5);

        player.setEffect(glow);
    }

    private void addAdvancedGlowToPlayer() {
        DropShadow outerGlow = new DropShadow();
        outerGlow.setColor(Color.rgb(255, 255, 150, 0.7));
        outerGlow.setRadius(15);
        outerGlow.setSpread(0.3);
        outerGlow.setBlurType(BlurType.GAUSSIAN);

        InnerShadow innerGlow = new InnerShadow();
        innerGlow.setColor(Color.rgb(255, 255, 200, 0.5));
        innerGlow.setRadius(10);
        innerGlow.setChoke(0.5);

        Glow glowEffect = new Glow(0.5);

        outerGlow.setInput(innerGlow);
        innerGlow.setInput(glowEffect);

        player.setEffect(outerGlow);
    }

    private void addPulsingGlow() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        player.setEffect(glow);

        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.radiusProperty(), 10),
                        new KeyValue(glow.spreadProperty(), 0.3)
                ),
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(glow.radiusProperty(), 20),
                        new KeyValue(glow.spreadProperty(), 0.7)
                )
        );
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
    }

    private void createCustomGlowEffect() {
        Canvas glowCanvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = glowCanvas.getGraphicsContext2D();

        gamePane.getChildren().add(0, glowCanvas);

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                gc.setFill(Color.rgb(255, 255, 150, 0.3));
                for (int i = 0; i < 3; i++) {
                    double radius = player.getRadius() + 15 + i*5;
                    gc.fillOval(
                            player.getCenterX() - radius,
                            player.getCenterY() - radius,
                            radius * 2,
                            radius * 2
                    );
                }
            }
        };
        gameTimer.start();
    }

    private void createHitParticles(Monster monster) {
        Color particleColor = (Color) monster.getShape().getFill();

        int count = 10 + (int)(Math.random() * 5);
        for (int i = 0; i < count; i++) {
            Particle p = new Particle(
                    monster.getX(),
                    monster.getY(),
                    particleColor.brighter()
            );
            particles.add(p);
            gamePane.getChildren().add(p.getShape());
        }
    }

    public void updateParticles() {
        List<Particle> toRemove = new ArrayList<>();

        for (Particle p : particles) {
            p.update();
            if (p.isFinished()) {
                toRemove.add(p);
                gamePane.getChildren().remove(p.getShape());
            }
        }

        particles.removeAll(toRemove);
    }

    public void checkImmunity() {
        if (isImmune && System.currentTimeMillis() - immuneStartTime >= IMMUNE_DURATION) {
            isImmune = false;
            player.setEffect(null);
            addGlowToPlayer();
        }
    }

    public void initializeLevelProgressBar(Pane root) {
        levelProgressBar = new HBox(10);
        levelProgressBar.setPadding(new Insets(10));
        levelProgressBar.setAlignment(Pos.CENTER_LEFT);

        for (int i = 1; i <= 5; i++) {
            Rectangle box = new Rectangle(60, 40);
            box.setFill(Color.LIGHTGRAY);
            box.setStroke(Color.BLACK);

            Label label = new Label("XP: 0 / " + (2 * i));
            label.setFont(Font.font(12));

            StackPane stack = new StackPane(box, label);
            levelBoxes.add(stack);
            levelProgressBar.getChildren().add(stack);
        }

        root.getChildren().add(levelProgressBar);

        AnchorPane.setTopAnchor(levelProgressBar, 10.0);
        AnchorPane.setLeftAnchor(levelProgressBar, 10.0);
    }

    public void updateLevelProgressBar(int currentXP) {
        for (int i = 0; i < 5; i++) {
            int level = i + 1;
            int xpRequired = 2 * level;
            Label label = (Label) levelBoxes.get(i).getChildren().get(1);
            Rectangle box = (Rectangle) levelBoxes.get(i).getChildren().get(0);

            if (currentXP >= xpRequired) {
                box.setFill(Color.LIGHTGREEN);
                label.setText("Level " + level + " ✔");
            } else {
                box.setFill(Color.LIGHTGRAY);
                int currentLevelXP = (i == 0) ? currentXP : currentXP - (2 * i);
                currentLevelXP = Math.max(0, currentLevelXP);
                label.setText("XP: " + currentLevelXP + " / " + xpRequired);
            }
        }
    }

    public static Circle getPlayer() {
        return player;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public void resumeGame() {
        if (gameTimer != null) gameTimer.stop();
        if (gameLoop != null) gameLoop.stop();
        startGameTimer();
        gameLoop.start();
    }

    private void createBarrier() {
        double initialSize = Math.min(WIDTH, HEIGHT) / 4;
        barrier = new Rectangle(
                initialSize,
                initialSize,
                WIDTH - 2 * initialSize,
                HEIGHT - 2 * initialSize
        );
        barrier.setFill(Color.TRANSPARENT);
        barrier.setStroke(Color.RED);
        barrier.setStrokeWidth(3);
        barrier.setStrokeDashOffset(10);
        barrier.getStrokeDashArray().addAll(20d, 10d);

        Glow glow = new Glow(0.5);
        barrier.setEffect(glow);

        gamePane.getChildren().add(barrier);
        barrierActive = true;

        barrierTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(barrier.xProperty(), initialSize),
                        new KeyValue(barrier.yProperty(), initialSize),
                        new KeyValue(barrier.widthProperty(), WIDTH - 2 * initialSize),
                        new KeyValue(barrier.heightProperty(), HEIGHT - 2 * initialSize)
                ),
                new KeyFrame(Duration.seconds(BARRIER_SHRINK_DURATION),
                        new KeyValue(barrier.xProperty(), (WIDTH - MIN_BARRIER_SIZE)/2),
                        new KeyValue(barrier.yProperty(), (HEIGHT - MIN_BARRIER_SIZE)/2),
                        new KeyValue(barrier.widthProperty(), MIN_BARRIER_SIZE),
                        new KeyValue(barrier.heightProperty(), MIN_BARRIER_SIZE)
                )
        );
        barrierTimeline.setCycleCount(1);
        barrierTimeline.play();
    }

    public static void updateBarrierPosition(double cameraOffsetX, double cameraOffsetY) {
        if (!barrierActive) return;

        // تنظیم موقعیت معکوس دیواره
        barrier.setX(WIDTH / 2 - barrier.getWidth() / 2 - cameraOffsetX);
        barrier.setY(HEIGHT / 2 - barrier.getHeight() / 2 - cameraOffsetY); // حرکت معکوس در محور Y
    }


    private void restrictPlayerMovement() {
        if (!barrierActive) return;

        double px = player.getCenterX();
        double py = player.getCenterY();
        double pr = player.getRadius();
        boolean adjusted = false;

        if (px - pr < barrier.getX()) {
            player.setCenterX(barrier.getX() + pr);
            adjusted = true;
        }
        else if (px + pr > barrier.getX() + barrier.getWidth()) {
            player.setCenterX(barrier.getX() + barrier.getWidth() - pr);
            adjusted = true;
        }

        if (py - pr < barrier.getY()) {
            player.setCenterY(barrier.getY() + pr);
            adjusted = true;
        }
        else if (py + pr > barrier.getY() + barrier.getHeight()) {
            player.setCenterY(barrier.getY() + barrier.getHeight() - pr);
            adjusted = true;
        }

        if (adjusted && !isImmune2) {
            hero.setHp(-BARRIER_DAMAGE);
            isImmune2 = true;
            immuneStartTime = System.currentTimeMillis();
            createHitEffect();
            GameMenu.playDamage();
        }
    }

    private void checkBarrierCollision() {
        if (!barrierActive || isImmune) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBarrierDamageTime < BARRIER_DAMAGE_COOLDOWN) return;

        double px = player.getCenterX();
        double py = player.getCenterY();
        double pr = player.getRadius();

        boolean collided = false;

        if (px - pr < barrier.getX()) {
            player.setCenterX(barrier.getX() + pr);
            collided = true;
        }
        else if (px + pr > barrier.getX() + barrier.getWidth()) {
            player.setCenterX(barrier.getX() + barrier.getWidth() - pr);
            collided = true;
        }

        if (py - pr < barrier.getY()) {
            player.setCenterY(barrier.getY() + pr);
            collided = true;
        }
        else if (py + pr > barrier.getY() + barrier.getHeight()) {
            player.setCenterY(barrier.getY() + barrier.getHeight() - pr);
            collided = true;
        }

        if (collided) {
            lastBarrierDamageTime = currentTime;
            hero.setHp(-BARRIER_DAMAGE);
            isImmune = true;
            immuneStartTime = currentTime;

            createHitEffect();
            GameMenu.playDamage();

            if (hero.getHp() <= 0) {
                gameTimer.stop();
                showGameOverScreen();
            }
        }
    }

    private void removeBarrier() {
        if (!barrierActive) return;

        FadeTransition fade = new FadeTransition(Duration.seconds(1), barrier);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            gamePane.getChildren().remove(barrier);
            barrierActive = false;
        });
        fade.play();

        if (barrierTimeline != null) {
            barrierTimeline.stop();
        }
    }

    public void startPulseAnimation() {
        if (pulseTimeline == null) {
            setupPulseAnimation();
        }
        if (pulseTimeline.getStatus() != Animation.Status.RUNNING) {
            pulseTimeline.playFromStart();
        }
    }

    public void stopPulseAnimation() {
        if (pulseTimeline != null) {
            pulseTimeline.stop();
            player.setRadius(PLAYER_RADIUS);
            player.setFill(getPlayerColor());
        }
    }

    private void setupPulseAnimation() {
        double originalRadius = PLAYER_RADIUS;
        Color originalColor = getPlayerColor();

        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(player.radiusProperty(), originalRadius),
                        new KeyValue(player.fillProperty(), originalColor)
                ),
                new KeyFrame(Duration.seconds(0.15),
                        new KeyValue(player.radiusProperty(), originalRadius - 1.5),
                        new KeyValue(player.fillProperty(), originalColor.brighter())
                ),
                new KeyFrame(Duration.seconds(0.3),
                        new KeyValue(player.radiusProperty(), originalRadius),
                        new KeyValue(player.fillProperty(), originalColor)
                )
        );
        pulseTimeline.setCycleCount(Animation.INDEFINITE);
        pulseTimeline.setAutoReverse(true);
    }

}
