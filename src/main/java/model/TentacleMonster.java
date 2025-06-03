package model;

import javafx.scene.paint.Color;

public class TentacleMonster extends Monster {
    private static long lastSpawnTime = 0;
    private long lastSpawnTimeChecked = -1;
    public TentacleMonster(double x, double y, Color color) {
        super(25, 1, 0.5, false, x, y, color);

    }
    @Override
    protected double getOriginalRadius() {
        return 15;
    }

    @Override
    public void move() {
    }
    @Override
    public void checkSpawn() {
    }
    @Override
    public void attack() {
    }
}