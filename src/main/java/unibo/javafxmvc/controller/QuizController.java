package unibo.javafxmvc.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.PunteggioDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizController {

    @FXML
    private ToggleGroup Opzioni;

    @FXML
    private ImageView ivFoto;

    @FXML
    private Label lbCorretto;

    @FXML
    private Button lbIndietro;

    @FXML
    private Button lbInvia;

    @FXML
    private Label lbIstruzione;

    @FXML
    private Label lbSbagliato;

    @FXML
    private Label lbTitolo;

    @FXML
    private GridPane mainGridPane;

    @FXML
    private RadioButton rbOpzione1;

    @FXML
    private RadioButton rbOpzione2;

    @FXML
    private RadioButton rbOpzione3;

    @FXML
    private RadioButton rbOpzione4;

    @FXML
    private TextField tfText;

    @FXML
    private HBox hbHbox;
    private int i = 0;
    private Punteggio punteggio;


    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        if (AuxiliaryController.confirmSave("Conferma abbandono", "Sei sicuro di voler abbandonare l'esercizio?", "I tuoi progressi andranno persi.")) {
            Main.changeScene("View/UserHome.fxml");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("QuizController.initialize");
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova13.png"));
        ivFoto.setImage(image);
        punteggio = new Punteggio(Grado.PRINCIPIANTE, Main.currentUser, "Quiz");

    }

    @FXML
    public void InviaOnMouseClicked() {
        switch (i) {
            case 0:
                if (tfText.getText().equals("9")) {
                    dueSecondi();
                    SecondaScena();
                    punteggio.addPunteggio(1);
                } else {
                    punteggio.addPunteggio(0);
                    treSecondi();
                    SecondaScena();
                }
                i++;
                break;
            case 1:
                if (rbOpzione3.isSelected()) {
                    punteggio.addPunteggio(2);
                    dueSecondi();
                    TerzaScena();
                } else {
                    punteggio.addPunteggio(0);
                    treSecondi();
                    TerzaScena();
                }
                i++;
                break;

        }

    }

    @FXML
    public void SecondaScena() {
        lbIstruzione.setText("Individuare per quali valori è importante fare il test");
        tfText.setVisible(false);
        hbHbox.setVisible(true);
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova14.png"));
        ivFoto.setImage(image);
    }

    @FXML
    public void TerzaScena() {
        Main.punteggio = punteggio;
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> Main.changeScene("View/Quiz2.fxml"));
        pause.play();
    }

    private void dueSecondi() {
        lbCorretto.setText("Corretto!");
        lbCorretto.setStyle("-fx-text-fill: green");
        lbCorretto.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbCorretto.setVisible(false));
        pause.play();
    }

    private void treSecondi() {
        lbCorretto.setText("Sbagliato!");
        lbCorretto.setStyle("-fx-text-fill: red");
        lbCorretto.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbCorretto.setVisible(false));
        pause.play();
    }

}