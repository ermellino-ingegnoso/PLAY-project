package unibo.javafxmvc.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.CommentiFactory;
import unibo.javafxmvc.model.CommentiModel;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentiController implements Initializable {
    @FXML
    private ImageView ivFoto;
    @FXML
    private ImageView ivAvatar;
    @FXML
    private Label lbUsername;
    @FXML
    private Label lbPunti;
    @FXML
    private Label lbPunti2;
    @FXML
    private Circle cCircle;
    @FXML
    private Button lbIndietro;
    @FXML
    private Button lbInvia;
    @FXML
    private Label lbTitolo;
    @FXML
    private RadioButton rbOpzione1;
    @FXML
    private RadioButton rbOpzione2;
    @FXML
    private RadioButton rbOpzione3;
    @FXML
    private GridPane gpGridPane;
    @FXML
    private ProgressBar pbBarra;

    private CommentiModel esercizio;
    private int prossimoEsercizio = 0;
    private Punteggio punteggio;
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("CommentiController.initialize");
        CaricaEsercizio();
        punteggio = new Punteggio(Grado.INTERMEDIO, Main.currentUser, "Commenti");
       // AuxiliaryController.initAvatar(Main.currentUser, ivAvatar, cCircle, lbUsername, gpGridPane);
    }

    public void updateProgressBar() {
        pbBarra.setProgress(0.25F);
    }
    @FXML
    void InviaPressed() {
        if (rbOpzione1.isSelected() && esercizio.check(rbOpzione1.getText())) {
            punteggio.addPunteggio(1);
            dueSecondi();
            updateProgressBar();
        } else if (rbOpzione2.isSelected() && esercizio.check(rbOpzione2.getText())) {
            punteggio.addPunteggio(2);
            dueSecondi();
            updateProgressBar();
        } else if (rbOpzione3.isSelected() && esercizio.check(rbOpzione3.getText())) {
            punteggio.addPunteggio(3);
            dueSecondi();
            updateProgressBar();
        } else {
            punteggio.addPunteggio(0);
            treSecondi();
            updateProgressBar();
        }
        CaricaEsercizio();
    }
    @FXML
    void InviaOnKeyPressed() {
        InviaPressed();
    }
    @FXML
    void InviaOnMouseClicked() {
        InviaPressed();
    }

    @FXML
    void IndietroOnMouseClicked() {
        Main.changeScene("View/UserHome.fxml");
    }

    @FXML
    public void CaricaEsercizio() {
        CommentiFactory factory1 = new CommentiFactory();
        this.esercizio = factory1.getCommentiModel(prossimoEsercizio);
        prossimoEsercizio += 1;
        if (esercizio == null) {
            Main.punteggio = punteggio;
            Main.changeScene("View/PunteggiEsercizio.fxml");
            return;
        }
        System.out.println(esercizio.getOpzioni().get(0));
        System.out.println(esercizio.getPercorsoFoto());

        ivFoto.setImage(new Image(getClass().getResource(esercizio.getPercorsoFoto()).toExternalForm()));
        rbOpzione1.setText(esercizio.getOpzioni().get(0));
        rbOpzione2.setText(esercizio.getOpzioni().get(1));
        rbOpzione3.setText(esercizio.getOpzioni().get(2));
    }
    private void dueSecondi() {
        lbPunti.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbPunti.setVisible(false));
        pause.play();
    }
    private void treSecondi() {
        lbPunti2.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbPunti2.setVisible(false));
        pause.play();
    }
    /*
    Problema funzione 2 secondi non viene visualizzato nell'ultimo esercizio
    altro passo importante gestire i punti
    */
}
