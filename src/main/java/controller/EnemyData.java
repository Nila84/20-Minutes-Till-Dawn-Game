package controller;

import java.io.Serializable;

public class EnemyData implements Serializable {
    private double x;
    private double y;
    private String type;

    public EnemyData(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getType() {
        return type;
    }
}
