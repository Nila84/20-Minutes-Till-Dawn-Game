package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import view.menu.MainMenu;

public class TalentMenuController {
    @FXML private Label upKeyLabel;
    @FXML private Label downKeyLabel;
    @FXML private Label leftKeyLabel;
    @FXML private Label rightKeyLabel;
    @FXML private Label shootKeyLabel;

    @FXML
    public void initialize() {
        // Set current key bindings from GameViewController
        upKeyLabel.setText(GameViewController.getKeyBinding("UP").toString());
        downKeyLabel.setText(GameViewController.getKeyBinding("DOWN").toString());
        leftKeyLabel.setText(GameViewController.getKeyBinding("LEFT").toString());
        rightKeyLabel.setText(GameViewController.getKeyBinding("RIGHT").toString());
        shootKeyLabel.setText(GameViewController.getKeyBinding("SHOOT").toString());
    }

    @FXML
    private void handleBackButton() {
        // Return to main menu
        MainMenu.show();
    }
}