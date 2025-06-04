package model;

import javafx.scene.paint.Color;

public class ElderMonster extends Monster {
    private long lastDashTime;
    private static final long DASH_COOLDOWN = 5000;
    private boolean shieldActive;
    private double shieldSize;
    private long lastShotTime;
    private final long shotCooldown = 5000;

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

    public boolean canShoot(long now) {
        return now - lastShotTime >= shotCooldown;
    }

    public void shot(long now) {
        lastShotTime = now;
    }

    @Override
    public void checkSpawn(){

    }

    @Override
    public void attack() {
    }

    @Override
    protected double getOriginalRadius() {
        return 20;
    }

}