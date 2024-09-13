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
import unibo.javafxmvc.Main;

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


    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        Main.changeScene("View/UserHome.fxml");
    }

    @FXML
    public void initialize() {
        System.out.println("CommentiController.initialize");
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova10.png"));
        ivFoto.setImage(image);

    }

    @FXML
    public void InviaOnMouseClicked() {
        switch (i) {
            case 0:
                if (tfText.getText().equals("10")) {
                    dueSecondi();
                    SecondaScena();
                } else {
                    treSecondi();
                    SecondaScena();
                }
                i++;
                break;
            case 1:
                if (rbOpzione2.isSelected()) {
                    System.out.println("ciao");
                    dueSecondi();
                    TerzaScena();
                } else {
                    treSecondi();
                    TerzaScena();
                }
                i++;
                break;
            case 2:
                if (rbOpzione3.isSelected()) {
                    dueSecondi();
                } else {
                    treSecondi();
                }
                break;
        }

    }

    @FXML
    public void SecondaScena() {
        tfText.setVisible(false);
        hbHbox.setVisible(true);
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova11.png"));
        ivFoto.setImage(image);
    }

    @FXML
    public void TerzaScena() {
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova9.png"));
        ivFoto.setImage(image);
    }


    private void dueSecondi() {
        lbCorretto.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbCorretto.setVisible(false));
        pause.play();
    }

    private void treSecondi() {
        lbSbagliato.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbSbagliato.setVisible(false));
        pause.play();
    }


}