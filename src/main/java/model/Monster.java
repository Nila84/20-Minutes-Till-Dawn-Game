package model;//package model;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.menu.GameScreen;

public abstract class Monster {
    protected int hp;
    protected int damage;
    private boolean isKnockback = false;
    private double knockbackDistance = 30;
    private double knockbackSpeed = 10;
    private double originalX, originalY;
    protected transient Timeline pulseTimeline;
    protected boolean isPulsing = false;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    private double speed;
    protected boolean isBoss;

    protected double x, y;
    protected Circle shape;

    public Monster(int health, int damage, double speed, boolean isBoss, double startX, double startY, Color color) {
        this.hp = health;
        this.damage = damage;
        this.speed = speed;
        this.isBoss = isBoss;
        this.x = startX;
        this.y = startY;
        if (isBoss) this.shape = new Circle(20, color);
        else this.shape = new Circle(15, color);

        this.shape.setCenterX(x);
        this.shape.setCenterY(y);
    }

    public void move(double targetX, double targetY) {
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;

            shape.setCenterX(this.x);
            shape.setCenterY(this.y);
        }
    }

    public Circle getShape() {
        return shape;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void moveTowards(Circle player) {
        double playerX = player.getCenterX();
        double playerY = player.getCenterY();
//
//        double dx = playerX - x;
//        double dy = playerY - y;
//        double distance = Math.sqrt(dx * dx + dy * dy);
//
//        if (distance > 0) {
//            x += (dx / distance) * speed;
//            y += (dy / distance) * speed;
//        }
//
//        shape.setCenterX(x);
//        shape.setCenterY(y);
        double dx = playerX - getX();
        double dy = playerY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }

//        move(dx, dy);
        shape.setCenterX(x);
        shape.setCenterY(y);
//        getShape().setTranslateX(getX() -playerX- GameScreen.cameraOffsetX);
//        getShape().setTranslateY(getY() -playerX- GameScreen.cameraOffsetY);
    }

    public void takeDamage(int amount) {
        this.hp -= amount;
        if (this.hp <= 0) this.hp = 0;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }


    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void checkSpawn() {

    }

    public void startKnockback(double fromX, double fromY) {
        this.isKnockback = true;
        this.originalX = getX();
        this.originalY = getY();

        double dx = getX() - fromX;
        double dy = getY() - fromY;
        double length = Math.sqrt(dx*dx + dy*dy);

        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        TranslateTransition knockback = new TranslateTransition(Duration.millis(100), getShape());
        knockback.setByX(dx * knockbackDistance);
        knockback.setByY(dy * knockbackDistance);

        TranslateTransition returnAnim = new TranslateTransition(Duration.millis(300), getShape());
        returnAnim.setByX(-dx * knockbackDistance);
        returnAnim.setByY(-dy * knockbackDistance);

        SequentialTransition sequence = new SequentialTransition(knockback, returnAnim);
        sequence.play();
    }
    public void updateKnockback() {
        if (!isKnockback) return;

        double dx = (originalX - getX()) * 0.2;
        double dy = (originalY - getY()) * 0.2;

        setX(getX() + dx);
        setY(getY() + dy);

        if (Math.abs(getX() - originalX) < 1 && Math.abs(getY() - originalY) < 1) {
            setX(originalX);
            setY(originalY);
            isKnockback = false;
        }
    }

    public void updatePosition() {
        shape.setCenterX(this.x);
        shape.setCenterY(this.y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void startPulseAnimation() {
        if (pulseTimeline != null) {
            pulseTimeline.stop();
        }

        double originalRadius = getShape().getRadius();
        Color originalColor = (Color) getShape().getFill();

        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(getShape().radiusProperty(), originalRadius),
                        new KeyValue(getShape().fillProperty(), originalColor)
                ),
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(getShape().radiusProperty(), originalRadius * 0.9),
                        new KeyValue(getShape().fillProperty(), originalColor.brighter())
                ),
                new KeyFrame(Duration.seconds(0.4),
                        new KeyValue(getShape().radiusProperty(), originalRadius),
                        new KeyValue(getShape().fillProperty(), originalColor)
                )
        );

        pulseTimeline.setCycleCount(Animation.INDEFINITE);
        pulseTimeline.setAutoReverse(true);
        pulseTimeline.play();
        isPulsing = true;
    }

    public void stopPulseAnimation() {
        if (pulseTimeline != null) {
            pulseTimeline.stop();
            getShape().setRadius(getOriginalRadius());
            isPulsing = false;
        }
    }

    protected abstract double getOriginalRadius();

    public abstract void move();
    public abstract void attack();

    public boolean isPulsing() {
        return isPulsing;
    }
}
