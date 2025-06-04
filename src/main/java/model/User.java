package model;

import controller.App;
import controller.DBController;
import controller.GameController;
import controller.UserController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class User implements Comparable<User>{
    private String username;
    private String password;
    private String avatarFilePath;
    private String favoriteColor;
    private Hero selectedHero;
    private Weapon selectedWeapon;
    private static int gameOver;
    private static int won;
    public static int level = 1;
    private static int XP;
    public int killNum;
    public int killNumber;
    private List<Ability> acquiredAbilities = new ArrayList<>();
    public long longestSurvivalTime;
    public int score;
    public int scores;

    public static void setLevel(int level) {
        User.level += level;
    }

    public void setGameOver(int num) {
        gameOver += num;
    }

    public static void setXP(int XP) {
        User.XP = XP;
    }

    public static int getXP() {
        return XP;
    }

    public void setWon(int num) {
        won += num;
    }

    public int getGameOver() {
        return gameOver;
    }

    public int getWon() {
        return won;
    }

    private ArrayList<Integer> scoreOfDiff = new ArrayList<>(); // score in difficulty
    private static int nowDiff = 1; // what is now difficulty
    public String[] lastGames = new String[3];
    //private Game game;

    public User(String username, String password, String address, String favoriteColor) {
        this.username = username;
        this.password = password;
        this.avatarFilePath = address;
        this.favoriteColor = favoriteColor;
        this.acquiredAbilities = new ArrayList<>();
        this.longestSurvivalTime = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.lastGames[0] = this.lastGames[1] = this.lastGames[2] = LocalDateTime.now().format(formatter);
        scoreOfDiff.add(0);
        scoreOfDiff.add(0);
        scoreOfDiff.add(0);
        App.getAllUsers().add(this);
    }

    public Hero getSelectedHero() {
        return selectedHero;
    }

    public void setSelectedHero(Hero selectedHero) {
        this.selectedHero = selectedHero;
    }

    public Weapon getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(Weapon selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }


    public String getUsername() {
        return username;
    }
    public String getFavoriteColor() {
        return favoriteColor;
    }
    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public int getHighscore() {
        return this.scoreOfDiff.get(0) + this.scoreOfDiff.get(1)
                + this.scoreOfDiff.get(2);
    }

    public LocalDateTime getLastGame() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime[] lastGamesInDate = new LocalDateTime[3];
        lastGamesInDate[0] = LocalDateTime.parse(lastGames[0], formatter);
        lastGamesInDate[1] = LocalDateTime.parse(lastGames[1], formatter);
        lastGamesInDate[2] = LocalDateTime.parse(lastGames[2], formatter);
        LocalDateTime lastGame = lastGamesInDate[0].isBefore(lastGamesInDate[1]) ?
                lastGamesInDate[1] : lastGamesInDate[0];
        lastGame = lastGamesInDate[2].isBefore(lastGame) ?
                lastGame : lastGamesInDate[2];
        return lastGame;
    }

    public LocalDateTime getLastGameWithDiff(int difficultyLevel) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lastGameInDate  = LocalDateTime.parse(lastGames[difficultyLevel], formatter);
        return  lastGameInDate;
    }

    @Override
    public int compareTo(User o) {
        if (this.getHighscore() > o.getHighscore())
            return -1;
        if (this.getHighscore() < o.getHighscore())
            return 1;
        if (this.getLastGame().isBefore(o.getLastGame()))
            return -1;
        if (this.getLastGame().isAfter(o.getLastGame()))
            return 1;
        return 0;
    }



    public static class OrderByScoreOfAType implements Comparator<User> {
        @Override
        public int compare(User u1, User u2) {
            if (u1.scoreOfDiff.get(nowDiff) > u2.scoreOfDiff.get(nowDiff))
                return -1;
            if (u1.scoreOfDiff.get(nowDiff) < u2.scoreOfDiff.get(nowDiff))
                return 1;
            if (u1.getLastGameWithDiff(nowDiff).isBefore(u2.getLastGameWithDiff(nowDiff)))
                return -1;
            if (u1.getLastGameWithDiff(nowDiff).isAfter(u2.getLastGameWithDiff(nowDiff)))
                return 1;
            return 0;
        }
    }

    public void addAbility(Ability ability) {
        if (!acquiredAbilities.contains(ability)) {
            acquiredAbilities.add(ability);
        }
    }

    public List<Ability> getAcquiredAbilities() {
        if (acquiredAbilities == null) {
            acquiredAbilities = new ArrayList<>();
        }
        return new ArrayList<>(acquiredAbilities);
    }

    public long getLongestSurvivalTime() {
        return longestSurvivalTime / 1000;
    }


    public static class OrderByScore implements Comparator<User> {
        @Override
        public int compare(User u1, User u2) {
            int score1 = u1.getScore();
            int score2 = u2.getScore();

            if (score1 > score2) return -1;
            if (score1 < score2) return 1;

            return u1.getLastGame().compareTo(u2.getLastGame());
        }
    }

    public void updateSurvivalTime(long survivalTimeMillis) {
        if (survivalTimeMillis > this.longestSurvivalTime) {
            this.longestSurvivalTime = survivalTimeMillis;
        }
    }

    public String getFormattedSurvivalTime() {
        long seconds = longestSurvivalTime / 1000;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    public long getSurvivalTimeInSeconds() {
        return longestSurvivalTime / 1000;
    }


    public String[] getLastGames() {
        return lastGames;
    }

    public int getScore() {
        return scores;
    }

    public void updateUserStats(int killNum, int score, long survivalTime) {
        this.killNumber += killNum;

        this.score += score;

        if (survivalTime > this.longestSurvivalTime) {
            this.longestSurvivalTime = survivalTime;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.lastGames[nowDiff] = LocalDateTime.now().format(formatter);

        this.scoreOfDiff.set(nowDiff, this.scoreOfDiff.get(nowDiff) + score);

        DBController.saveUsers();
        if (App.getCurrentUser() != null && this.username.equals(App.getCurrentUser().getUsername())) {
            DBController.saveCurrentUser();
        }
    }
}
