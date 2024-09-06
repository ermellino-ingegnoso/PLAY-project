package unibo.javafxmvc.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.OrdinaPassiFactory;
import unibo.javafxmvc.model.OrdinaPassiModel;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class OrdinaPassiController implements Initializable {
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
    private Label lbPunti;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrdinaPassiFactory factory1 = new OrdinaPassiFactory();
        OrdinaPassiModel esercizio = factory1.getOrdinaPassiModel();

        ivProva1.setImage(new Image(getClass().getResource(esercizio.getFoto().get(1)).toExternalForm()));
        ivProva2.setImage(new Image(getClass().getResource(esercizio.getFoto().get(1)).toExternalForm()));
        ivProva3.setImage(new Image(getClass().getResource(esercizio.getFoto().get(2)).toExternalForm()));

        /*
        esercizioCorrente = Main.ordinaPassiModel;
        ivAvatar.setImage(Main.currentUser.getAvatar());
        cCerchio.setStroke(Color.web(Main.currentUser.getColor()));
        lbID.setText("ID: " + esercizioCorrente.getId());
        lbTitolo.setText(esercizioCorrente.getTitolo());
        lbIndicazione.setText(esercizioCorrente.getIndicazione());
        lbNumero1.setText(esercizioCorrente.getNumero1());
        lbNumero2.setText(esercizioCorrente.getNumero2());
        lbNumero3.setText(esercizioCorrente.getNumero3());

         */
    }

    @FXML
   private OrdinaPassiModel provaaa = new OrdinaPassiModel(new ArrayList<String>(Arrays.asList("/unibo/javafxmvc/Images/Ordina/Prova1.png","/unibo/javafxmvc/Images/Ordina/Prova2.png","/unibo/javafxmvc/Images/Ordina/Prova3.png")), 3, "132", "Enrico");
    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Home.fxml");
    }
    /*
     @FXML
    private void ControllaOnMousePressed(MouseEvent event) {
        int a = 132;
         System.out.println("sCorretto");
        if (tfInput.getText().equals("132")) {

            Image im1 = new Image("/unibo/javafxmvc/Images/Ok.JPG");

            ivProva1.setImage(im1);
        }
    }
    */


    @FXML
    void InviaOnKeyPressed() {
        int a = 132;
        System.out.println("Corretto");
        if (tfInput.getText().equals("132")) {
            // Controlla();

            System.out.println("Corretto00000");
            dueSecondi();
        } else {
            System.out.println("Errore!");
        }
        NuovoEsercizio();
    }


    private void dueSecondi() {
        lbPunti.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> lbPunti.setVisible(false));
        pause.play();
    }


    public void NuovoEsercizio(){

        Image im4 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova4.png").toExternalForm());
        ivProva1.setImage(im4);
        Image im5 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova5.png").toExternalForm());
        ivProva2.setImage(im5);
        Image im6 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova6.png").toExternalForm());
        ivProva3.setImage(im6);
        int b = 312;
        if (tfInput.getText().equals("312")) {
            System.out.println("CorrettoCorretto");
            dueSecondi();
            ultimoEsercizio();
        } else {
            System.out.println("Errore!");
        }
    }

    public void ultimoEsercizio(){
        Image im7 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova7.png").toExternalForm());
        ivProva1.setImage(im7);
        Image im8 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova8.png").toExternalForm());
        ivProva2.setImage(im8);
        Image im9 = new Image(getClass().getResource("/unibo/javafxmvc/Images/Ordina/Prova9.png").toExternalForm());
        ivProva3.setImage(im9);
        int c = 213;
        if (tfInput.getText().equals("213")) {
            System.out.println("Corretto");
            dueSecondi();
        } else {
            System.out.println("Errore!");
        }
    }


}
