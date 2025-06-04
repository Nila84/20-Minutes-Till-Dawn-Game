package view.menu;

import controller.App;
import controller.GameController;
import controller.GameViewController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import view.Paths;
import view.enums.ScoreboardMenuText;
import view.util.CustomCursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardMenu extends Application {
    private static final int MAX_RANKS = 10;
    private List<Label> rankLabels = new ArrayList<>();
    private Button timeButton;
    private Button killsButton;
    private Button scoreButton;
    private Button usernameButton;
    private Button backButton;
    private Label titleLabel;

    private User loggedInUser;
    private String currentSort = "score";
    private boolean isEnglish = GameController.language; // Default to English

    @Override
    public void start(Stage stage) throws Exception {
        this.loggedInUser = App.getCurrentUser();
        BorderPane borderPane = createScoreboardPane();
        borderPane.getStyleClass().add("Background");
        applyTheme(borderPane);

        Scene scoreboardMenuScene = new Scene(borderPane,700 ,700);
        CustomCursor.setGameCursor(scoreboardMenuScene);
        stage.setTitle(getText(ScoreboardMenuText.SCOREBOARD_TITLE));
        stage.setScene(scoreboardMenuScene);
        stage.show();

        showRanking(currentSort);
    }

    private BorderPane createScoreboardPane() {
        BorderPane borderPane = new BorderPane();

        // Initialize ranking labels
        for (int i = 0; i < MAX_RANKS; i++) {
            Label label = new Label();
            rankLabels.add(label);
        }

        // Create sorting buttons
        timeButton = createSortButton("time", ScoreboardMenuText.TIME_BUTTON);
        killsButton = createSortButton("kills", ScoreboardMenuText.KILLS_BUTTON);
        scoreButton = createSortButton("score", ScoreboardMenuText.SCORE_BUTTON);
        usernameButton = createSortButton("username", ScoreboardMenuText.USERNAME_BUTTON);

        // Create back button
        backButton = new Button(getText(ScoreboardMenuText.BACK_BUTTON));
        backButton.setOnAction(e -> back(null));

        // Create title
        titleLabel = new Label(getText(ScoreboardMenuText.SCOREBOARD_TITLE));
        titleLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");

        // Create button panel
        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getChildren().addAll(timeButton, killsButton, scoreButton, usernameButton);

        // Create main content
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(buttonPanel);
        centerBox.getChildren().add(titleLabel);
        centerBox.getChildren().addAll(rankLabels);
        centerBox.getChildren().add(backButton);

        borderPane.setCenter(centerBox);
        return borderPane;
    }

    private Button createSortButton(String sortType, ScoreboardMenuText text) {
        Button button = new Button(getText(text));
        button.setOnAction(e -> showRanking(sortType));
        return button;
    }

    private void applyTheme(BorderPane pane) {
        if (GameViewController.isBlackWhiteThemeOn) {
            pane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            pane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        } else {
            pane.getStylesheets().add(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
        }
    }

    private void showRanking(String sortType) {
        this.currentSort = sortType;
        ArrayList<User> rankedUsers = getRankedUsers(sortType);

        for (int i = 0; i < rankedUsers.size() && i < MAX_RANKS; i++) {
            User user = rankedUsers.get(i);
            Label label = rankLabels.get(i);
            updateRankLabel(label, user, i + 1);
        }
    }

    private ArrayList<User> getRankedUsers(String sortType) {
        switch (sortType) {
            case "time": return GameController.rankingBySurvivalTime();
            case "kills": return GameController.rankingByKills();
            case "username": return GameController.rankingByUsername();
            case "score":
            default: return GameController.rankingByScore();
        }
    }

    private void updateRankLabel(Label label, User user, int rank) {
        String rankSymbol = (rank <= 3) ? " +" : "";
        String prefix = user.equals(loggedInUser) ? "*" : "";
        String survivalTime = user.getFormattedSurvivalTime();

        String text = String.format("%s%d. %s%s | %s: %d | %s: %d | %s: %s",
                prefix,
                rank,
                rankSymbol,
                user.getUsername(),
                getText(ScoreboardMenuText.SCORE_LABEL),
                user.getScore(),
                getText(ScoreboardMenuText.KILLS_LABEL),
                user.killNum,
                getText(ScoreboardMenuText.TIME_LABEL),
                survivalTime);

        label.setText(text);
        label.setStyle("-fx-font-weight: " + (user.equals(loggedInUser) ? "bold" : "normal") +
                "; -fx-text-fill: " + getRankColor(rank));
    }

    private String getRankColor(int rank) {
        switch (rank) {
            case 1: return "gold";
            case 2: return "silver";
            case 3: return "#CD7F32"; // bronze
            default: return "black";
        }
    }

    public void back(MouseEvent mouseEvent) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            showAlert(
                    getText(ScoreboardMenuText.ERROR_TITLE),
                    getText(ScoreboardMenuText.BACK_ERROR_MSG),
                    Alert.AlertType.ERROR
            );
        }
    }

    private String getText(ScoreboardMenuText textType) {
        return textType.getText(isEnglish);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}