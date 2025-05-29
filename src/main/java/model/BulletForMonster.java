package model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class BulletForMonster {
    private Circle shape;
    private double dx, dy;
    private int damage;
    private Weapon sourceWeapon;
    private Line pathLine;
    private static final double SPEED = 5.0;
    private Object target;

    public BulletForMonster(double startX, double startY, double targetX, double targetY) {
        this.shape = new Circle(8, Color.RED);
        this.shape.setCenterX(startX);
        this.shape.setCenterY(startY);

        this.pathLine = new Line(startX, startY, targetX, targetY);
        this.pathLine.setStroke(Color.YELLOW.deriveColor(0, 1, 1, 0.3));
        this.pathLine.setStrokeWidth(1);

        double angle = Math.atan2(targetY - startY, targetX - startX);
        this.dx = SPEED * Math.cos(angle);
        this.dy = SPEED * Math.sin(angle);
    }

    public void update() {
        shape.setCenterX(shape.getCenterX() + dx);
        shape.setCenterY(shape.getCenterY() + dy);

        pathLine.setEndX(shape.getCenterX());
        pathLine.setEndY(shape.getCenterY());
    }

    public boolean isOutOfBounds(double width, double height) {
        double x = shape.getCenterX();
        double y = shape.getCenterY();
        double radius = shape.getRadius();
        return (x + radius < 0) || (x - radius > width) ||
                (y + radius < 0) || (y - radius > height);
    }

    public Circle getShape() {
        return shape;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}