package view.menu;

import controller.GameController;
import controller.GameViewController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Ability;
import model.Hero;
import view.Paths;
import view.enums.TalentMenuText;

public class TalentMenu extends Application {
    private boolean isEnglish = GameController.language;
    private ScrollPane scrollPane;
    private VBox mainContainer;
    private Button backButton;

    @Override
    public void start(Stage stage) throws Exception {
        createUIComponents();

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(20));

        // Apply theme
        if (GameViewController.isBlackWhiteThemeOn) {
            root.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            root.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }

        // Add custom fonts
        root.getStylesheets().add(getClass().getResource("/CSS/custom-fonts.css").toExternalForm());

        stage.setScene(new Scene(root, 900, 800));
        stage.setTitle(getText(TalentMenuText.TALENT_MENU_TITLE_EN));
        stage.show();
    }

    private void createUIComponents() {
        // Create main container
        mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));

        // Create cheat codes section
        VBox cheatCodesBox = createCheatCodesSection();

        // Create heroes section
        VBox heroesBox = createHeroesSection();

        // Create abilities section
        VBox abilitiesBox = createAbilitiesSection();

        // Create key bindings section
        VBox keyBindingsBox = createKeyBindingsSection();

        // Add all sections to main container
        mainContainer.getChildren().addAll(cheatCodesBox, heroesBox, abilitiesBox, keyBindingsBox);

        // Create scroll pane
        scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Create back button
        backButton = new Button(getText(TalentMenuText.BACK_EN));
        backButton.getStyleClass().add("custom-font-button");
        backButton.setStyle("-fx-pref-width: 300; -fx-pref-height: 50; -fx-background-color: #2c3e50; -fx-text-fill: white;");
        backButton.setPadding(new Insets(15, 30, 15, 30));
        backButton.setOnMouseClicked(this::back);
    }

    private VBox createCheatCodesSection() {
        VBox cheatCodesBox = new VBox(10);
        cheatCodesBox.setStyle("-fx-background-color: rgba(200,200,200,0.2); -fx-background-radius: 10; -fx-padding: 15;");

        Label title = new Label(getText(TalentMenuText.CHEAT_CODES_EN));
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-underline: true; -fx-text-fill: #d35400;");

        VBox codesContainer = new VBox(8);
        codesContainer.setStyle("-fx-padding: 10;");

        // Cheat 1
        HBox cheat1 = createCheatItem(
                getText(TalentMenuText.REDUCE_TIME_EN),
                getText(TalentMenuText.REDUCE_TIME_DESC_EN)
        );

        // Cheat 2
        HBox cheat2 = createCheatItem(
                getText(TalentMenuText.LEVEL_UP_EN),
                getText(TalentMenuText.LEVEL_UP_DESC_EN)
        );

        // Cheat 3
        HBox cheat3 = createCheatItem(
                getText(TalentMenuText.RESTORE_HEALTH_EN),
                getText(TalentMenuText.RESTORE_HEALTH_DESC_EN)
        );

        // Cheat 4
        HBox cheat4 = createCheatItem(
                getText(TalentMenuText.TRIGGER_BOSS_EN),
                getText(TalentMenuText.TRIGGER_BOSS_DESC_EN)
        );

        // Cheat 5
        HBox cheat5 = createCheatItem(
                getText(TalentMenuText.INFINITE_AMMO_EN),
                getText(TalentMenuText.INFINITE_AMMO_DESC_EN)
        );

        codesContainer.getChildren().addAll(cheat1, cheat2, cheat3, cheat4, cheat5);
        cheatCodesBox.getChildren().addAll(title, codesContainer);

        return cheatCodesBox;
    }

    private HBox createCheatItem(String title, String description) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #7f8c8d;");

        hbox.getChildren().addAll(titleLabel, descLabel);
        return hbox;
    }

    private VBox createHeroesSection() {
        VBox heroesBox = new VBox(10);
        heroesBox.setStyle("-fx-padding: 10;");

        Label title = new Label(getText(TalentMenuText.HEROES_EN));
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-underline: true;");

        Hero[] heroes = {
                new Hero("SHANA", 4, 4),
                new Hero("DIAMOND", 7, 1),
                new Hero("LILITH", 5, 3),
                new Hero("SCARLET", 3, 5),
                new Hero("DASHER", 6, 2)
        };

        VBox heroesContainer = new VBox(10);
        for (Hero hero : heroes) {
            HBox heroBox = new HBox(20);
            heroBox.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(hero.getName());
            nameLabel.getStyleClass().add("custom-font-regular");
            nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

            Label statsLabel = new Label(String.format("HP: %d | Speed: %d", hero.getHp(), hero.getSpeed()));
            statsLabel.setStyle("-fx-font-size: 16;");

            heroBox.getChildren().addAll(nameLabel, statsLabel);
            heroesContainer.getChildren().add(heroBox);
        }

        heroesBox.getChildren().addAll(title, heroesContainer);
        return heroesBox;
    }

    private VBox createAbilitiesSection() {
        VBox abilitiesBox = new VBox(10);
        abilitiesBox.setStyle("-fx-padding: 10;");

        Label title = new Label(getText(TalentMenuText.ABILITIES_EN));
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-underline: true;");

        GridPane abilitiesGrid = new GridPane();
        abilitiesGrid.setHgap(20);
        abilitiesGrid.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(Pos.CENTER_LEFT.getHpos());
        col1.setPrefWidth(200);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHalignment(Pos.CENTER_LEFT.getHpos());
        col2.setPrefWidth(400);

        abilitiesGrid.getColumnConstraints().addAll(col1, col2);

        Ability[] abilities = {
                Ability.getDefaultAbility(Ability.AbilityType.VITALITY),
                Ability.getDefaultAbility(Ability.AbilityType.DAMAGER),
                Ability.getDefaultAbility(Ability.AbilityType.PROCREASE),
                Ability.getDefaultAbility(Ability.AbilityType.AMOCREASE),
                Ability.getDefaultAbility(Ability.AbilityType.SPEEDY)
        };

        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];

            Label nameLabel = new Label("• " + ability.getName());
            nameLabel.getStyleClass().add("custom-font-regular");
            nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
            abilitiesGrid.add(nameLabel, 0, i);

            Label descLabel = new Label(ability.getDescription());
            descLabel.setStyle("-fx-font-size: 16;");
            abilitiesGrid.add(descLabel, 1, i);
        }

        abilitiesBox.getChildren().addAll(title, abilitiesGrid);
        return abilitiesBox;
    }

    private VBox createKeyBindingsSection() {
        VBox keyBindingsBox = new VBox(10);
        keyBindingsBox.setStyle("-fx-background-color: rgba(200,200,200,0.2); -fx-background-radius: 10; -fx-padding: 15;");

        Label title = new Label(getText(TalentMenuText.KEY_BINDINGS_EN));
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-underline: true;");

        HBox keysContainer = new HBox(30);
        keysContainer.setAlignment(Pos.CENTER);
        keysContainer.setStyle("-fx-padding: 10;");

        // Move Up
        VBox upBox = createKeyBindingItem(
                getText(TalentMenuText.MOVE_UP_EN),
                formatKey(GameViewController.getKeyBinding("UP"))
        );

        // Move Down
        VBox downBox = createKeyBindingItem(
                getText(TalentMenuText.MOVE_DOWN_EN),
                formatKey(GameViewController.getKeyBinding("DOWN"))
        );

        // Move Left
        VBox leftBox = createKeyBindingItem(
                getText(TalentMenuText.MOVE_LEFT_EN),
                formatKey(GameViewController.getKeyBinding("LEFT"))
        );

        // Move Right
        VBox rightBox = createKeyBindingItem(
                getText(TalentMenuText.MOVE_RIGHT_EN),
                formatKey(GameViewController.getKeyBinding("RIGHT"))
        );

        // Shoot
        VBox shootBox = createKeyBindingItem(
                getText(TalentMenuText.SHOOT_EN),
                formatKey(GameViewController.getKeyBinding("SHOOT"))
        );

        keysContainer.getChildren().addAll(upBox, downBox, leftBox, rightBox, shootBox);
        keyBindingsBox.getChildren().addAll(title, keysContainer);

        return keyBindingsBox;
    }

    private VBox createKeyBindingItem(String title, String key) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label keyLabel = new Label(key);
        keyLabel.setStyle("-fx-font-size: 18; -fx-text-fill: #2980b9;");

        vbox.getChildren().addAll(titleLabel, keyLabel);
        return vbox;
    }

    private String formatKey(KeyCode keyCode) {
        if (keyCode == null) return getText(TalentMenuText.NOT_SET_EN);
        return keyCode.getName().toUpperCase();
    }

    private String getText(TalentMenuText textType) {
        return TalentMenuText.getText(textType, isEnglish);
    }

    private void back(MouseEvent event) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.err.println("Error returning to main menu: " + e.getMessage());
        }
    }
}