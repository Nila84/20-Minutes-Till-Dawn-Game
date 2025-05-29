package view.menu;

import controller.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Ability;
import model.Hero;
import view.Paths;

import java.net.URL;
import java.util.ResourceBundle;

public class TalentMenu extends Application implements Initializable {

    @FXML private VBox heroesBox;
    @FXML private GridPane abilitiesGrid;
    @FXML private Label upKeyLabel;
    @FXML private Label downKeyLabel;
    @FXML private Label leftKeyLabel;
    @FXML private Label rightKeyLabel;
    @FXML private Label shootKeyLabel;


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.TALENT_MENU_FXML_FILE.getPath()));
        BorderPane root = loader.load();

        // Apply theme
        if (GameViewController.isBlackWhiteThemeOn) {
            root.getStylesheets().remove(getClass().getResource
                    (Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            root.getStylesheets().add(getClass().getResource
                    (Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }

        stage.setScene(new Scene(root));
        stage.setTitle("Talents Menu");
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize key bindings
        upKeyLabel.setText(formatKey(GameViewController.getKeyBinding("UP")));
        downKeyLabel.setText(formatKey(GameViewController.getKeyBinding("DOWN")));
        leftKeyLabel.setText(formatKey(GameViewController.getKeyBinding("LEFT")));
        rightKeyLabel.setText(formatKey(GameViewController.getKeyBinding("RIGHT")));
        shootKeyLabel.setText(formatKey(GameViewController.getKeyBinding("SHOOT")));
        initializeHeroes();
        initializeAbilities();
    }

    private void initializeAbilities() {
        Ability[] abilities = {
                Ability.getDefaultAbility(Ability.AbilityType.VITALITY),
                Ability.getDefaultAbility(Ability.AbilityType.DAMAGER),
                Ability.getDefaultAbility(Ability.AbilityType.PROCREASE),
                Ability.getDefaultAbility(Ability.AbilityType.AMOCREASE),
                Ability.getDefaultAbility(Ability.AbilityType.SPEEDY)
        };

        abilitiesGrid.getChildren().clear();

        for (int i = 0; i < abilities.length; i++) {
            Ability ability = abilities[i];

            Label nameLabel = new Label("• " + ability.getName());
            nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
            abilitiesGrid.add(nameLabel, 0, i);

            Label descLabel = new Label(ability.getDescription());
            descLabel.setStyle("-fx-font-size: 16;");
            abilitiesGrid.add(descLabel, 1, i);
        }
    }

    private void initializeHeroes() {
        Hero[] heroes = {
                new Hero("SHANA", 4, 4),
                new Hero("DIAMOND", 7, 1),
                new Hero("LILITH", 5, 3),
                new Hero("SCARLET", 3, 5),
                new Hero("DASHER", 6, 2)
        };

        heroesBox.getChildren().clear();
        for (Hero hero : heroes) {
            HBox heroBox = new HBox(20);
            heroBox.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(hero.getName());
            nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

            Label statsLabel = new Label(String.format("HP: %d | Speed: %d", hero.getHp(), hero.getSpeed()));
            statsLabel.setStyle("-fx-font-size: 16;");
            heroBox.getChildren().addAll(nameLabel, statsLabel);
            heroesBox.getChildren().add(heroBox);
        }
    }

    private String formatKey(KeyCode keyCode) {
        if (keyCode == null) return "NOT SET";
        return keyCode.getName().toUpperCase();
    }

    @FXML
    private void back(MouseEvent event) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.err.println("Error returning to main menu: " + e.getMessage());
        }
    }
}