package model;

import javafx.scene.paint.Color;

public class EyebatMonster extends Monster {
    private static final long ATTACK_COOLDOWN = 3000;
    private long lastShotTime;
    private final long shotCooldown = 3000;

    public EyebatMonster(double x, double y, Color color) {
        super(50, 1, 0.1, false, x, y, color);
        this.lastShotTime = System.currentTimeMillis();
    }

    @Override
    public void move() {
    }
    @Override
    public void checkSpawn(){

    }

    public boolean canShoot(long now) {
        return now - lastShotTime >= shotCooldown;
    }


    public void shot(long now) {
        lastShotTime = now;
    }


    @Override
    public void attack() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastShotTime >= ATTACK_COOLDOWN) {
            lastShotTime = currentTime;
        }
    }
}