module unibo.javafxmvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;

    opens unibo.javafxmvc to javafx.fxml;
    opens unibo.javafxmvc.model to com.google.gson; //  gson accede agli attributi privati dei model

    exports unibo.javafxmvc.model;
    exports unibo.javafxmvc;
    exports unibo.javafxmvc.controller;
    opens unibo.javafxmvc.controller to javafx.fxml;
    exports unibo.javafxmvc.DAO;
    opens unibo.javafxmvc.DAO to com.google.gson;
}