package model;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ExperienceOrb {
    private Circle shape;
    private double x;
    private double y;
    private double speed = 0.5;
    private boolean collected = false;

    public ExperienceOrb(double x, double y) {
        this.x = x;
        this.y = y;
        this.shape = new Circle(10, Color.GOLD);
        this.shape.setCenterX(x);
        this.shape.setCenterY(y);

        DropShadow glow = new DropShadow(10, Color.YELLOW);
        this.shape.setEffect(glow);
    }

    public Circle getShape() {
        return shape;
    }

    public void update() {
        y += speed;
        shape.setCenterY(y);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}