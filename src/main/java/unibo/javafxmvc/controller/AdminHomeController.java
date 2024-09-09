package unibo.javafxmvc.controller;

import unibo.javafxmvc.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
public class AdminHomeController {
    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private ListView<?> lvEsercizi;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ImageView userAvatar;

    @FXML
    protected void AggiungiEsercizioBtnOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) AggiungiEsercizio();
    }
    @FXML
    protected void AggiungiEsercizioBtnOnMouseClicked(MouseEvent event) {
        AggiungiEsercizio();
    }
    private void AggiungiEsercizio() {
        Main.changeScene("View/AggiungiRegola.fxml");
    }
    @FXML
    void btnSchermataAccessoOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }
}