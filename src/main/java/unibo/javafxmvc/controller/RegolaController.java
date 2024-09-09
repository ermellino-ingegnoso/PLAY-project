
package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import unibo.javafxmvc.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class RegolaController implements Initializable {
    @FXML
    private Label lblDescrizione;
    @FXML
    private Label lblDomanda;
    @FXML
    private Label lblTitolo;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.bloccoIndex = 0;
        lblTitolo.setText(Main.esercizioCorrente.getRegola().getTitolo());
        lblDescrizione.setText(Main.esercizioCorrente.getRegola().getDescrizione());
        lblDomanda.setText(Main.esercizioCorrente.getRegola().getDomanda());
        Main.generalCounter = Main.esercizioCorrente.getBlocchiEsperto().size();
        System.out.println("RegolaController:initialize: esercizioCorrente: " + Main.esercizioCorrente);
        System.out.println("RegolaController:initialize: blocchi: " + Main.esercizioCorrente.getBlocchiEsperto());
    }
    @FXML
    void AvviaOnKeyPressed(KeyEvent event) {if(AuxiliaryController.keyEnterPressed(event)) Avvia();}
    @FXML
    void AvviaOnMouseClicked(MouseEvent event) {Avvia();}
    @FXML
    void AnnullaOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) annulla();}
    @FXML
    void AnnullaOnMouseClicked(MouseEvent event) {annulla();}
    private void Avvia(){
        Main.changeScene("View/BloccoEsperto.fxml");
    }
    private void annulla(){
        Main.changeScene("View/UserHome.fxml");
    }
}

