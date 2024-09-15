package unibo.javafxmvc.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.PunteggioDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;

public class Quiz2Controller {

    @FXML
    private Button bIndietro;

    @FXML
    private Button bInvia;

    @FXML
    private ImageView iv1;

    @FXML
    private ImageView iv2;

    @FXML
    private ImageView iv3;

    @FXML
    private ImageView iv4;

    @FXML
    private Label lbIstruzione;

    @FXML
    private Label lbTitolo;

    @FXML
    private RadioButton rb1;

    @FXML
    private RadioButton rb2;

    @FXML
    private RadioButton rb3;

    @FXML
    private RadioButton rb4;

    @FXML
    private StackPane spStack;

    @FXML
    private Label lbCorretto;
    private ImageView selectedImage;


    @FXML
    public void initialize() {
        System.out.println("Quiz2Controller.initialize");
        iv1.setOnMouseClicked(this::handleImageClick);
        iv2.setOnMouseClicked(this::handleImageClick);
        iv3.setOnMouseClicked(this::handleImageClick);
        iv4.setOnMouseClicked(this::handleImageClick);
        Image image = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova15.png"));
        Image imgae2 = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova16.png"));
        Image image3 = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova17.png"));
        Image image4 = new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/Ordina/Prova18.png"));
        iv1.setImage(image);
        iv2.setImage(imgae2);
        iv3.setImage(image3);
        iv4.setImage(image4);
    }
    private void handleImageClick(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (selectedImage != null) {
            // Reset the previous selected image (e.g., remove border or reset opacity)
            spStack.setStyle("");
        }
        // Set the new selected image
        selectedImage = clickedImage;
        // Change the visual state of the selected image (e.g., add border or change opacity)
        spStack.setStyle("-fx-border-color: blue; -fx-border-width: 2;");

    }

    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        Main.changeScene("View/UserHome.fxml");
    }

    @FXML
    private void InviaOnMOusePressed(MouseEvent event) {
        if (iv2.equals(selectedImage)) {
            lbCorretto.setText("Corretto!");
            lbCorretto.setVisible(true);
            lbCorretto.setStyle("-fx-text-fill: green");
            Main.punteggio.addPunteggio(3);
            aspetta();
        } else {
            lbCorretto.setText("Sbagliato!");
            lbCorretto.setVisible(true);
            lbCorretto.setStyle("-fx-text-fill: red");
            Main.punteggio.addPunteggio(0);
            aspetta();
        }
    }

    public void aspetta() {
        try {
            PunteggioDBM.insertPunteggio(Main.punteggio);
        } catch (ConnectionException e) {
            Main.changeScene("View/ErroreDatabase.fxml");
        }
        Main.gradoAttuale = Main.punteggio.getGrado();
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.play();
        pause.setOnFinished(event -> Main.changeScene("View/PunteggiEsercizio.fxml"));
    }



}
