package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import unibo.javafxmvc.Main;

public class ErroreDatabaseController {
    @FXML
    private Label lblDatabase;


    @FXML
    public void initialize() {
        lblDatabase.setText("Percorso indicato per il database: \n" + Main.getDbURLlikeAbsolutePath());
    }
}
