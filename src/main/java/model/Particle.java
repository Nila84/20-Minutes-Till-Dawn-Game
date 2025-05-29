package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Particle {
    private Circle shape;
    private double dx, dy;
    private double originalX, originalY;
    private double speed;
    private boolean returning;

    public Particle(double x, double y, Color color) {
        this.shape = new Circle(2 + Math.random() * 3, color);
        this.shape.setCenterX(x);
        this.shape.setCenterY(y);
        this.originalX = x;
        this.originalY = y;
        this.speed = 2 + Math.random() * 3;
        this.returning = false;

        double angle = Math.random() * Math.PI * 2;
        this.dx = Math.cos(angle) * speed;
        this.dy = Math.sin(angle) * speed;
    }


    public void update() {
        if (!returning) {
            shape.setCenterX(shape.getCenterX() + dx);
            shape.setCenterY(shape.getCenterY() + dy);

            if (Math.random() < 0.05) {
                returning = true;
            }
        } else {
            double moveX = (originalX - shape.getCenterX()) * 0.1;
            double moveY = (originalY - shape.getCenterY()) * 0.1;

            shape.setCenterX(shape.getCenterX() + moveX);
            shape.setCenterY(shape.getCenterY() + moveY);
        }
    }

    public boolean isFinished() {
        double distance = Math.sqrt(
                Math.pow(originalX - shape.getCenterX(), 2) +
                        Math.pow(originalY - shape.getCenterY(), 2)
        );
        return distance < 5 && returning;
    }

    public Circle getShape() {
        return shape;
    }
}