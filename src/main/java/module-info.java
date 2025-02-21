module Finanztracker {
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires javafx.controls;
    requires jdk.compiler;
    requires java.desktop;

    exports finanztracker.main;

    opens finanztracker.main to javafx.fxml;
}