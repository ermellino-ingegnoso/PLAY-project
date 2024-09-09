package unibo.javafxmvc.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private CommentiModel esercizio;
    private int prossimoEsercizio = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("CommentiController.initialize");
        ivAvatar.setImage(Main.currentUser.getAvatar());
        CaricaEsercizio();

        cCircle = new Circle(ivAvatar.getFitWidth() / 2, ivAvatar.getFitHeight() / 2, Math.min(ivAvatar.getFitWidth(), ivAvatar.getFitHeight()) / 2);
        cCircle.setStroke(Color.web(Main.currentUser.getColor()));
        cCircle.setStrokeWidth(0);
        ivAvatar.setClip(cCircle);
        drawBorders(Color.web(Main.currentUser.getColor()));
        lbUsername.setText(Main.currentUser.getUsername());

        Rotate rotate = new Rotate();
        rotate.setAngle(270); 
        rotate.setPivotX(ivAvatar.getFitWidth() / 2);
        rotate.setPivotY(ivAvatar.getFitHeight() / 2);
        ivAvatar.getTransforms().add(rotate);


    }

    private void drawBorders(Color borderColor) {
        cCircle.setStroke(borderColor);
        lbUsername.setTextFill(borderColor);
        Line line = new Line(0, 0, gpGridPane.getWidth(), 0);
        line.setStroke(borderColor);
        line.setStrokeWidth(2);

        Pane linePane = new Pane();
        linePane.getChildren().add(line);
        linePane.setPrefSize(gpGridPane.getWidth(), line.getStrokeWidth());
        gpGridPane.add(linePane, 0, 1);
    }

    @FXML
    void InviaPressed() {
        if (rbOpzione1.isSelected() && rbOpzione1.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Corretto");
            dueSecondi();
        } else if (rbOpzione2.isSelected() && rbOpzione2.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Coretto");
            dueSecondi();
        } else if (rbOpzione3.isSelected() && rbOpzione3.getText().equals(esercizio.getSoluzione())) {
            System.out.println("Corretto");
            dueSecondi();
        } else {
            System.out.println("Errore!");
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
        Main.changeScene("View/Home.fxml");
    }

    @FXML
    public void CaricaEsercizio() {
        CommentiFactory factory1 = new CommentiFactory();
        this.esercizio = factory1.getCommentiModel(prossimoEsercizio);
        prossimoEsercizio += 1;
        if (esercizio == null) {
            Main.changeScene("View/Home.fxml");
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

    /*
    Problema funzione 2 secondi non viene visualizzato nell'ultimo esercizio
    altro passo importante gestire i punti
    */


}
