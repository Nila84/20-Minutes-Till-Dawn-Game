package controller;

import java.io.Serializable;
import java.util.List;

public class SavedGameData implements Serializable {
    private String username;
    private int xp;
    private int hp;
    private int level;
    private int kills;
    private long remainingTime;
    private List<EnemyData> enemyDataList;
    private String heroName;
    private String weaponName;
    private String abilityName;

    public SavedGameData(String username, int hp, int xp, int level, int kills, long remainingTime,
                         List<EnemyData> enemyDataList, String heroName, String weaponName, String abilityName) {
        this.username = username;
        this.hp = hp;
        this.xp = xp;
        this.level = level;
        this.kills = kills;
        this.remainingTime = remainingTime;
        this.enemyDataList = enemyDataList;
        this.heroName = heroName;
        this.weaponName = weaponName;
        this.abilityName = abilityName;
    }
    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }

    public int getLevel() {
        return level;
    }

    public int getKills() {
        return kills;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public List<EnemyData> getEnemyDataList() {
        return enemyDataList;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeroName() {
        return heroName;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public String getAbilityName() {
        return abilityName;
    }
}
