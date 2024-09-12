package unibo.javafxmvc.controller;

import javafx.fxml.Initializable;
import unibo.javafxmvc.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.model.Grado;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);
    }
    @FXML
    void BloccoMancanteBtnOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) AggiungiEsercizioBloccoMancante();
    }
    @FXML
    void BloccoMancanteBtnOnMouseClicked(MouseEvent event) {
        AggiungiEsercizioBloccoMancante();
    }
    @FXML
    void TrovaErroreOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) AggiungiEsercizioTrovaErrore();
    }
    @FXML
    void TrovaErroreOnMouseClicked(MouseEvent event) {
        AggiungiEsercizioTrovaErrore();
    }
    private void AggiungiEsercizioTrovaErrore() {
        Main.gradoAttuale = Grado.AVANZATO;
        Main.changeScene("View/AggiungiRegola.fxml");
    }
    private void AggiungiEsercizioBloccoMancante() {
        Main.gradoAttuale = Grado.ESPERTO;
        Main.changeScene("View/AggiungiRegola.fxml");
    }
    @FXML
    void btnSchermataAccessoOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }


}