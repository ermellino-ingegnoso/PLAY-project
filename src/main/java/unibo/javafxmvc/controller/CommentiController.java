package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.CommentiFactory;
import unibo.javafxmvc.model.CommentiModel;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentiController implements Initializable {

    @FXML
    private ImageView ivFoto;

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

    private CommentiModel esercizio;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("CommentiController.initialize");
        CommentiFactory factory1 = new CommentiFactory();
        this.esercizio = factory1.getCommentiModel();
        System.out.println(esercizio.getOpzioni().get(0));
        System.out.println(esercizio.getPercorsoFoto());

            ivFoto.setImage(new Image(getClass().getResource(esercizio.getPercorsoFoto()).toExternalForm()));
        rbOpzione1.setText(esercizio.getOpzioni().get(0));
        rbOpzione2.setText(esercizio.getOpzioni().get(1));
        rbOpzione3.setText(esercizio.getOpzioni().get(2));
    }
    @FXML
    void InviaPressed(){
        if (rbOpzione1.isSelected() && rbOpzione1.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Corretto00000");
        } else if  (rbOpzione2.isSelected() && rbOpzione2.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Cooretto");
        } else if (rbOpzione3.isSelected() && rbOpzione3.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Corretto");
        } else {
            System.out.println("Errore!");
        }
    }
    @FXML
    void InviaOnKeyPressed(){
        InviaPressed();
    }

    @FXML
    void InviaOnMouseClicked(){
        InviaPressed();
    }

    @FXML
    void IndietroOnMouseClicked(){
        Main.changeScene("View/Home.fxml");
    }





}
