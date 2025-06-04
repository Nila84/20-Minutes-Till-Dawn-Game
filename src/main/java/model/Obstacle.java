package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Obstacle {
    private Circle shape;
    private int damageOnCollision;

    public Obstacle(double x, double y, double radius, int damage) {
        this.shape = new Circle(radius, Color.DARKGREEN);
        this.shape.setCenterX(x);
        this.shape.setCenterY(y);
        this.damageOnCollision = damage;
    }

    public Circle getShape() {
        return shape;
    }

    public int getDamageOnCollision() {
        return damageOnCollision;
    }

    public double getX() {
        return shape.getCenterX();
    }

    public double getY() {
        return shape.getCenterY();
    }

    public void setX(double x) {
        shape.setCenterX(x);
    }

    public void setY(double y) {
        shape.setCenterY(y);
    }

    public double getRadius() {
        return shape.getRadius();
    }
}
