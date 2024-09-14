package unibo.javafxmvc.controller;

import javafx.scene.input.KeyEvent;
import unibo.javafxmvc.DAO.EsercizioEspertoDBM;
import javafx.scene.input.MouseEvent;
import unibo.javafxmvc.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.EsercizioEsperto;
import unibo.javafxmvc.model.Grado;

import java.net.URL;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable {
    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Circle circle;
    @FXML
    private Label lblEsercizioAvanzato;
    @FXML
    private Label lblEsercizioEsperto;
    private EsercizioEsperto esEsperto;
    private EsercizioEsperto esAvanzato;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);

        caricaEsercizi();
        if(esEsperto != null) lblEsercizioEsperto.setText(esEsperto.getRegola().getTitolo());
        else System.err.println("esEsperto è null");
        if(esAvanzato != null) lblEsercizioAvanzato.setText(esAvanzato.getRegola().getTitolo());
        else System.err.println("esAvanzato è null");
    }
    @FXML
    void AvanzatoOnMouseClicked(MouseEvent event) {
        if(esAvanzato == null){ //  in caso di fallimento nella ricerca di esercizi esperti incompleti
            AuxiliaryController.alertWindow("Errore", "Non è stato possibile creare un nuovo esercizio per il grado avanzato", "Riprova più tardi");
        } else{
            Main.esercizioCorrente = esAvanzato;
            Main.gradoAttuale = Grado.AVANZATO;
            Main.changeScene("View/Regola.fxml");
        }
    }
    @FXML
    void EspertoOnMouseClicked(MouseEvent event) {
        if(esEsperto == null){ //  in caso di fallimento nella ricerca di esercizi esperti incompleti
            AuxiliaryController.alertWindow("Errore", "Non è stato possibile creare un nuovo esercizio per il grado esperto", "Riprova più tardi");
        } else{
            Main.esercizioCorrente = esEsperto;
            Main.gradoAttuale = Grado.ESPERTO;
            Main.changeScene("View/Regola.fxml");
        }
    }
    @FXML
    private void OrdinaPassiOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Commenti.fxml");
    }
    @FXML
    private void QuizOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Quiz.fxml");
    }
    @FXML
    private void ScollegatiOnKeyPressed(KeyEvent event) {if(AuxiliaryController.keyEnterPressed(event)) scollega();}
    @FXML
    private void ScollegatiOnMouseClicked(MouseEvent event) {scollega();}
    private void scollega(){
        Main.gradoAttuale = null;
        Main.currentUser = null;
        Main.esercizioCorrente = null;
        Main.changeScene("View/Accesso.fxml");
    }
    public void caricaEsercizi(){
        //getEsercizioEspertoWithMaxIdByGrado
        try{
            esEsperto = EsercizioEspertoDBM.getOrCreateEsercizioEspertoForUser(Main.currentUser, Grado.ESPERTO);
            esAvanzato = EsercizioEspertoDBM.getOrCreateEsercizioEspertoForUser(Main.currentUser, Grado.AVANZATO);
            if(esEsperto == null) esAvanzato = EsercizioEspertoDBM.getEsercizioEspertoWithMaxIdByGradoAndUser(Grado.ESPERTO, Main.currentUser);
            if(esAvanzato == null) esAvanzato = EsercizioEspertoDBM.getEsercizioEspertoWithMaxIdByGradoAndUser(Grado.AVANZATO, Main.currentUser);
        } catch (ConnectionException ce){
            Main.changeScene("View/ErroreDatabase.fxml");
        }
    }
    @FXML
    private void ClassificaGeneraleOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) classificaGenerale();}
    @FXML
    private void ClassificaGeneraleOnMouseClicked(MouseEvent event) { classificaGenerale();}
    @FXML
    private void ClassificaUtenteOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) classificaUtente();}
    @FXML
    private void ClassificaUtenteOnMouseClicked(MouseEvent event) {classificaUtente();}
    private void classificaGenerale(){  Main.changeScene("View/ClassificaGenerale.fxml");}
    private void classificaUtente(){    Main.changeScene("View/ClassificaUtente.fxml");}
}
