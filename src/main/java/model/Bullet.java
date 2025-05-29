package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet {
    private static final double SPEED = 4.0;
    private Circle shape;
    private double dx, dy;
    private int damage;

    private Object target;

    public Bullet(double startX, double startY, double targetX, double targetY) {
        shape = new Circle(5, Color.YELLOW);
        shape.setCenterX(startX);
        shape.setCenterY(startY);

        double angle = Math.atan2(targetY - startY, targetX - startX);
        dx = SPEED * Math.cos(angle);
        dy = SPEED * Math.sin(angle);
    }

    public Circle getShape() {
        return shape;
    }

    public void update() {
        shape.setCenterX(shape.getCenterX() + dx);
        shape.setCenterY(shape.getCenterY() + dy);
    }

    public boolean isOutOfBounds(double width, double height) {
        double x = shape.getCenterX();
        double y = shape.getCenterY();
        return x < 0 || x > width || y < 0 || y > height;
    }

    public void setColor(Color color) {
        shape.setFill(color);
    }
}

