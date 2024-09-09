package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import unibo.javafxmvc.Main;

public class PunteggiController {

    @FXML
    private Label lbPunti;

    @FXML
    private Label lbUsername;


    @FXML
    void IndietroOnMouseClicked() {
        Main.changeScene("View/Home.fxml");
    }

}
