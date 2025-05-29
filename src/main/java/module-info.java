module tillDawn {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires javafx.media;

    exports view.menu;
    exports controller;
    exports model;
    opens view.menu to javafx.fxml , com.google.gson;
    opens controller to com.google.gson;
    opens model to com.google.gson;
}