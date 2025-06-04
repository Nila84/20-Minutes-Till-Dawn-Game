package controller;

import javafx.scene.paint.Color;

import model.*;
import java.util.*;

public class GameController {
//    public static Game game;
    public static boolean isPaused = false;
    public static int timeOfGame;
    public static long time;
    public static boolean language = true;

    private List<Monster> monsters;
    private User player;
    private Random random;

    public GameController(User player) {
        this.player = player;
        this.monsters = new ArrayList<>();
        this.random = new Random();
        spawnMonsters(10);
    }


    private void spawnMonsters(int count) {
        for (int i = 0; i < count; i++) {
            Monster monster = createRandomMonster();
            double x = random.nextInt(800);
            double y = random.nextInt(1000);
            monster.setPosition(x, y);
            monsters.add(monster);
        }
    }

    public static Monster createRandomMonster() {
        Random random = new Random();
        double x = random.nextDouble() * 800;
        double y = random.nextDouble() * 600;
        Color color;

        return switch (random.nextInt(3)) {
            case 1 -> new EyebatMonster(x, y, Color.RED);
            case 2 -> new TentacleMonster(x, y, Color.YELLOW);
            default -> new TentacleMonster(x, y, Color.YELLOW);
        };
    }

    public static ArrayList<User> rankingByScore() {
        ArrayList<User> users = new ArrayList<>(App.getAllUsers());
        users.sort(new User.OrderByScore());
        return users;
    }

    public static ArrayList<User> rankingByKills() {
        ArrayList<User> users = new ArrayList<>(App.getAllUsers());
        users.sort(Comparator.comparingInt(u -> -u.killNum));
        return users;
    }

    public static ArrayList<User> rankingBySurvivalTime() {
        ArrayList<User> users = new ArrayList<>(App.getAllUsers());
        users.sort((u1, u2) -> Long.compare(u2.getSurvivalTimeInSeconds(),
                u1.getSurvivalTimeInSeconds()));
        return users;
    }

    public static ArrayList<User> rankingByUsername() {
        ArrayList<User> users = new ArrayList<>(App.getAllUsers());
        users.sort(Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER));
        return users;
    }

}
