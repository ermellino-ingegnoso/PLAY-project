package unibo.javafxmvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label label;

    public void onButtonClicked(ActionEvent event) {
        label.setText("Hello, MVC!");
    }
}