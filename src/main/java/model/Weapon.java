package model;

import controller.GameViewController;
import view.menu.GameMenu;

public class Weapon {
    private final String name;
    private int maxAmmo;
    private double reloadTime; // به ثانیه
    private int projectileCount;
    private int damagePerProjectile;
    private int currentAmmo;
    private boolean isReloading;
    private long reloadStartTime;
    private boolean autoReload;

    public static final Weapon REVOLVER = new Weapon
            ("Revolver", 6, 1.0, 1, 20);
    public static final Weapon SHOTGUN = new Weapon
            ("Shotgun", 2, 1.0, 4, 10);
    public static final Weapon SMG_DUAL = new Weapon
            ("SMGs Dual", 24, 2.0, 1, 8);

    public Weapon(String name, int maxAmmo, double reloadTime, int projectileCount, int damagePerProjectile) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
        this.projectileCount = projectileCount;
        this.damagePerProjectile = damagePerProjectile;
        this.currentAmmo = maxAmmo;
        this.isReloading = false;
        this.autoReload = false;
    }
    public Weapon(String name) {
        this.name = name;
        this.isReloading = false;
        this.autoReload = false;
        switch (name){
            case "Revolver" :
                this.maxAmmo = 12;
                this.currentAmmo = 12;
                this.reloadTime = 1.0;
                this.projectileCount = 1;
                this.damagePerProjectile = 20;
                break;
            case "Shotgun" :
                this.maxAmmo = 4;
                this.currentAmmo = 4;
                this.reloadTime = 1.0;
                this.projectileCount = 4;
                this.damagePerProjectile = 10;
                break;
            case "SMGs Dual" :
                this.maxAmmo = 48;
                this.currentAmmo = 48;
                this.reloadTime = 2.0;
                this.projectileCount = 1;
                this.damagePerProjectile = 8;
                break;
            default:
                throw new IllegalArgumentException("Unknown weapon: " + name);
        }
    }

    public boolean fire() {
        if (isReloading) {
            return false;
        }

        if (currentAmmo <= 0) {
            if (autoReload) {
                startReload();
            }
            return false;
        }

        currentAmmo--;
        return true;
    }

    public void startReload() {
        if (!isReloading && currentAmmo < maxAmmo) {
            isReloading = true;
            reloadStartTime = System.currentTimeMillis();
            System.out.println("Reload started for " + name + ". Time: " + reloadTime + "s");
        }
    }

    public void updateReload() {
        if (GameViewController.autoReload && currentAmmo <= 0) {
            completeReload();
        }
        else if (isReloading &&
                System.currentTimeMillis() - reloadStartTime >= reloadTime * 1000) {
            completeReload();
        }
    }

    private void completeReload() {
        currentAmmo = maxAmmo;
        isReloading = false;
        System.out.println("Reload completed for " + name);
    }

    public void setAutoReload(boolean enabled) {
        this.autoReload = enabled;
    }

    public String getName() { return name; }
    public int getMaxAmmo() { return maxAmmo; }
    public int getCurrentAmmo() { return currentAmmo; }
    public double getReloadTime() { return reloadTime; }
    public int getProjectileCount() { return projectileCount; }
    public int getDamagePerProjectile() { return damagePerProjectile; }
    public boolean isReloading() { return isReloading; }
    public boolean isAutoReload() { return autoReload; }
    public double getReloadProgress() {
        if (!isReloading) return 1.0;
        return Math.min(1.0, (System.currentTimeMillis() - reloadStartTime) / (reloadTime * 1000));
    }
    public void setDamagePerProjectile(int damagePerProjectile) {
        this.damagePerProjectile = damagePerProjectile;
    }

    public void setProjectileCount(int projectileCount) {
        this.projectileCount += projectileCount;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo += maxAmmo;
    }

    public int getTotalDamage() {
        return projectileCount * damagePerProjectile;
    }

    public static Weapon[] getAllWeapons() {
        return new Weapon[] { REVOLVER, SHOTGUN, SMG_DUAL };
    }
}
