package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.OrdinaPassiModel;

import java.awt.event.ActionEvent;

public class OrdinaPassiController {
    private OrdinaPassiModel esercizioCorrente;

    @FXML
    private Circle cCerchio;

    @FXML
    private ImageView ivAvatar;

    @FXML
    private ImageView ivProva1;

    @FXML
    private ImageView ivProva2;

    @FXML
    private ImageView ivProva3;

    @FXML
    private Label lbID;

    @FXML
    private Label lbIndicazione;

    @FXML
    private Label lbNumero1;

    @FXML
    private Label lbNumero2;

    @FXML
    private GridPane lbNumero3;

    @FXML
    private Label lbTitolo;

    @FXML
    private TextField tfInput;

    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Home.fxml");
    }
     @FXML
    private void ControllaOnMousePressed(MouseEvent event) {
        int a = 132;
         System.out.println("sCorretto");
        if (tfInput.getText().equals("132")) {

            Image im1 = new Image("/unibo/javafxmvc/Images/Ok.JPG");

            System.out.println("Corretto00000");
            ivProva1.setImage(im1);
        }
    }

    @FXML
    void InviaOnKeyPressed() {
        int a = 132;
        System.out.println("Corretto");
        if (tfInput.getText().equals("132")) {
            // Controlla();
            NuovoEsercizio();
            System.out.println("Corretto00000");
        } else {
            System.out.println("Errore!");
        }
    }


    public void NuovoEsercizio(){

        Image im4 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova4.png").toExternalForm());
        ivProva1.setImage(im4);
        Image im5 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova5.png").toExternalForm());
        ivProva2.setImage(im5);
        Image im6 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova6.png").toExternalForm());
        ivProva3.setImage(im6);

    }


}
