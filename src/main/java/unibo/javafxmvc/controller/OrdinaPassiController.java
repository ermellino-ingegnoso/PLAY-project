package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.Main;

public class OrdinaPassiController {

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
        if (tfInput.getText().equals("132")) {
            System.out.println("Corretto");
            Image im1 = new Image("/unibo/javafxmvc/Images/Ok.JPG");

            System.out.println("Corretto00000");
            ivProva1.setImage(im1);
        }
    }


}
