module unibo.javafxmvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;

    requires java.compiler;
    requires com.h2database;

    opens unibo.javafxmvc to javafx.fxml;

    opens unibo.javafxmvc.controller to javafx.fxml;

    exports unibo.javafxmvc.exception;
    exports unibo.javafxmvc.model;
    exports unibo.javafxmvc;
    exports unibo.javafxmvc.controller;
    exports unibo.javafxmvc.DAO;
}