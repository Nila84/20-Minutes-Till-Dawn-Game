package view.menu;


import controller.App;
import controller.GameController;
import controller.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import view.Paths;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreboardMenu extends Application {
    public Label rank1;
    public Label rank2;
    public Label rank3;
    public Label rank4;
    public Label rank5;
    public Label rank6;
    public Label rank7;
    public Label rank8;
    public Label rank9;
    public Label rank10;
    public Label stateOfWin;
    private User loggedInUser;
    private String currentSort = "score";

    @Override
    public void start(Stage stage) throws Exception {
        this.loggedInUser = App.getCurrentUser();
        URL scoreboardMenuFXMLUrl = ScoreboardMenu.class.getResource(Paths.SCOREBOARD_MENU_FXML_FILE.getPath());
        BorderPane borderPane = FXMLLoader.load(scoreboardMenuFXMLUrl);
        if (GameViewController.isBlackWhiteThemeOn) {
            borderPane.getStylesheets().remove(getClass().getResource(
                    Paths.COMMON_STYLES_FILE_PATH.getPath()).toExternalForm());
            borderPane.getStylesheets().add(getClass().getResource(
                    Paths.BLACK_WHITE_STYLE_FILE_PATH.getPath()).toExternalForm());
        }
        Scene scoreboardMenuScene = new Scene(borderPane);
        stage.setScene(scoreboardMenuScene);
        stage.show();
    }

    public void initialize() {
        showRanking("score");
    }

    private void showRanking(String sortType) {
        this.currentSort = sortType;
        HashMap<Integer, Label> labels = new HashMap<>();
        pushingLabelsToHashMap(labels);

        ArrayList<User> rankedUsers;
        switch (sortType) {
            case "time":
                rankedUsers = GameController.rankingBySurvivalTime();
                break;
            case "kills":
                rankedUsers = GameController.rankingByKills();
                break;
            case "username":
                rankedUsers = GameController.rankingByUsername();
                break;
            case "score":
            default:
                rankedUsers = GameController.rankingByScore();
        }

        for (int i = 0; i < rankedUsers.size() && i < 10; i++) {
            User user = rankedUsers.get(i);
            Label label = labels.get(i + 1);
            String survivalTime = user.getFormattedSurvivalTime();

            if (user.equals(App.getCurrentUser())) {
                String text = String.format("*"+"%d. %s | Score: %d | Kills: %d | Survival: %s",
                        i + 1,
                        user.getUsername(),
                        user.getScore(),
                        user.killNum,
                        survivalTime);
                label.setText(text);

            }
            else {
                String text = String.format("%d. %s | Score: %d | Kills: %d | Survival: %s",
                        i + 1,
                        user.getUsername(),
                        user.getScore(),
                        user.killNum,
                        survivalTime);
                label.setText(text);
            }


            if (user.equals(loggedInUser)) {
                label.setStyle("-fx-font-weight: bold; -fx-text-fill: " +
                        (i < 3 ? getRankColor(i + 1) : "black"));
            } else {
                label.setStyle("-fx-text-fill: " + getRankColor(i + 1));
            }
        }
    }

    private String getRankColor(int rank) {
        switch (rank) {
            case 1: return "gold";
            case 2: return "silver";
            case 3: return "#CD7F32"; // bronze
            default: return "black";
        }
    }

    private void pushingLabelsToHashMap(HashMap<Integer , Label> labels) {
        labels.put(1 , rank1);
        labels.put(2 , rank2);
        labels.put(3 , rank3);
        labels.put(4 , rank4);
        labels.put(5 , rank5);
        labels.put(6 , rank6);
        labels.put(7 , rank7);
        labels.put(8 , rank8);
        labels.put(9 , rank9);
        labels.put(10 , rank10);
    }


    public void back(MouseEvent mouseEvent) {
        try {
            new MainMenu().start(LoginMenu.stageOfProgram);
        } catch (Exception e) {
            System.out.println("an error occurred");
        }
    }

    public void livingTime(MouseEvent mouseEvent) {
        showRanking("time");
    }

    public void killsNumber(MouseEvent mouseEvent) {
        showRanking("kills");
    }

    public void scoreRanking(MouseEvent mouseEvent) {
        showRanking("score");
    }

    public void usernameRanking(MouseEvent mouseEvent) {
        showRanking("username");
    }
}
