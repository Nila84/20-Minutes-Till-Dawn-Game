package model;

import javafx.scene.paint.Color;

public class ElderMonster extends Monster {
    private long lastDashTime;
    private static final long DASH_COOLDOWN = 5000; // 5 ثانیه
    private boolean shieldActive;
    private double shieldSize;

    public ElderMonster(double x, double y, Color color) {
        super(400, 3, 0.1, true, x, y, color);
        this.lastDashTime = 0;
        this.shieldActive = true;
        this.shieldSize = 1.0;
    }

    @Override
    public void move() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastDashTime >= DASH_COOLDOWN) {
            lastDashTime = currentTime;
        } else {
        }
    }

    @Override
    public void checkSpawn(){

    }

    @Override
    public void attack() {
    }

    public void updateShield(double gameProgress) {
        this.shieldSize = 1.0 - gameProgress;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public double getShieldSize() {
        return shieldSize;
    }

    public void deactivateShield() {
        this.shieldActive = false;
    }
}