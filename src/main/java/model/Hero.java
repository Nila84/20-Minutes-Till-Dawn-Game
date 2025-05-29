package model;

public class Hero {
    private final String name;
    private int hp;
    private int speed;

    public Hero(String name, int hp, int speed) {
        this.name = name;
        this.hp = hp;
        this.speed = speed;
    }

    public Hero(String selectedHero) {
        this.name = selectedHero;
        switch (selectedHero) {
            case "SHANA":
                this.hp = 8;
                this.speed = 8;
                break;
            case "DIAMOND":
                this.hp = 14;
                this.speed = 2;
                break;
            case "LILITH":
                this.hp = 10;
                this.speed = 6;
                break;
            case "SCARLET":
                this.hp = 6;
                this.speed = 10;
                break;
            case "DASHER":
                this.hp = 12;
                this.speed = 20;
                break;
            default:
                throw new IllegalArgumentException("Unknown hero: " + selectedHero);
        }
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setHp(int hp) {
        this.hp += hp;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
