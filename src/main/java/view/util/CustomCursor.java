package view.util;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

import java.util.Objects;

public class CustomCursor {
    private static ImageCursor gameCursor;

    public static void initialize() {
        try {
            Image cursorImage = new Image(Objects.requireNonNull(CustomCursor.class.getResourceAsStream(
                    "/images/Cursor.png")));
            gameCursor = new ImageCursor(cursorImage);
        } catch (Exception e) {
            System.err.println("Failed to load custom cursor: " + e.getMessage());
        }
    }

    public static void setGameCursor(javafx.scene.Scene scene) {
        if (gameCursor != null) {
            scene.setCursor(gameCursor);
        } else {
            scene.setCursor(Cursor.HAND);
        }
    }

    public static void setDefaultCursor(javafx.scene.Scene scene) {
        scene.setCursor(Cursor.DEFAULT);
    }
}