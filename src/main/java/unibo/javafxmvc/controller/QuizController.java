package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import unibo.javafxmvc.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizController implements Initializable {

    @FXML
    private RadioButton rbA;

    @FXML
    private RadioButton rbB;

    @FXML
    private RadioButton rbC;

    @FXML
    private RadioButton rbD;

    @FXML
    private Circle lbCircle;

    @FXML
    private Label lbDomanda;

    @FXML
    private ImageView ivFoto;

    @FXML
    private Label lbTitolo;

    @FXML
    private Label lbUsername;

    @FXML
    private TextArea taOpzioni;

    @FXML
    private Label lbCorretta;

    @FXML
    private GridPane mainGridPane;


    @FXML
    private void IndietroOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Home.fxml");
    }
    @FXML
    private void ControllaRispostaOnMousePressed(MouseEvent event) {
        String a = "C";
        if (rbC.isSelected()) {
            lbCorretta.setVisible(true);
        } else {
            lbCorretta.setVisible(false);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivFoto.setImage(Main.currentUser.getAvatar());
        /*
        lbCircle = new Circle(ivFoto.getFitWidth() / 2, ivFoto.getFitHeight() / 2, Math.min(ivFoto.getFitWidth(), ivFoto.getFitHeight()) / 2);
        lbCircle.setStroke(Color.web(Main.currentUser.getColor()));
        lbCircle.setStrokeWidth(0);
        ivFoto.setClip(lbCircle);
        drawBorders(Color.web(Main.currentUser.getColor()));
        lbUsername.setText(Main.currentUser.getUsername());

         */
    }
    private void drawBorders(Color borderColor){
        lbCircle.setStroke(borderColor);
        lbUsername.setTextFill(borderColor);
        Line line = new Line(0, 0, mainGridPane.getWidth(), 0);
        line.setStroke(borderColor);
        line.setStrokeWidth(2);

        Pane linePane = new Pane();
        linePane.getChildren().add(line);
        linePane.setPrefSize(mainGridPane.getWidth(), line.getStrokeWidth());
        mainGridPane.add(linePane, 0, 1);
    }



}