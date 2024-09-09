package unibo.javafxmvc.controller;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import unibo.javafxmvc.DAO.EsercizioEspertoDBM;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import unibo.javafxmvc.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.EsercizioEsperto;

import java.net.URL;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable {

    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Circle circle;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userAvatar.setImage(Main.currentUser.getAvatar());

        circle = new Circle(userAvatar.getFitWidth() / 2, userAvatar.getFitHeight() / 2, Math.min(userAvatar.getFitWidth(), userAvatar.getFitHeight()) / 2);
        circle.setStroke(Color.web(Main.currentUser.getColor()));
        circle.setStrokeWidth(0);
        userAvatar.setClip(circle);
        drawBorders(Color.web(Main.currentUser.getColor()));
        lblUsername.setText(Main.currentUser.getUsername());
        
        Rotate rotate = new Rotate();
        rotate.setAngle(270);
        rotate.setPivotX(userAvatar.getFitWidth() / 2);
        rotate.setPivotY(userAvatar.getFitHeight() / 2);
        userAvatar.getTransforms().add(rotate);

    }
    @FXML
    void AvanzatoOnMouseClicked(MouseEvent event) {
        AuxiliaryController.alertWindow("Errore", "Funzionalità non disponibile", "Questa funzionalità non è ancora stata implementata");
    }
    @FXML
    void EspertoOnMouseClicked(MouseEvent event) {
        try{
            EsercizioEsperto ee = EsercizioEspertoDBM.getOrCreateEsercizioEspertoForUser(Main.currentUser);
            if(ee == null){ //  in caso di fallimento nella ricerca di esercizi esperti incompleti
                AuxiliaryController.alertWindow("Errore", "Non è stato possibile creare un nuovo esercizio esperto", "Riprova più tardi");
            } else{
                Main.esercizioCorrente = ee;
                Main.changeScene("View/Regola.fxml");
            }
        } catch (ConnectionException ce){
            Main.changeScene("View/ErroreDatabase.fxml");
        }
    }
    private void drawBorders(Color borderColor){
        circleAvatar.setStroke(borderColor);
        lblUsername.setTextFill(borderColor);
        Line line = new Line(0, 0, mainGridPane.getWidth(), 0);
        line.setStroke(borderColor);
        line.setStrokeWidth(2);

        Pane linePane = new Pane();
        linePane.getChildren().add(line);
        linePane.setPrefSize(mainGridPane.getWidth(), line.getStrokeWidth());
        mainGridPane.add(linePane, 0, 1);
    }
    @FXML
    private void OrdinaPassiOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Commenti.fxml");
    }
    @FXML
    private void QuizOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Quiz.fxml");
    }
}