// src/main/java/unibo/javafxmvc/controller/ClassificaGeneraleController.java
package unibo.javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.DAO.PunteggioDBM;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassificaGeneraleController {
    @FXML
    private Circle circleAvatar;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private TableView<UserScore> tbView;
    @FXML
    private TableColumn<UserScore, Float> principianteCol;
    @FXML
    private TableColumn<UserScore, String> usernameCol;
    @FXML
    private TableColumn<UserScore, Float> espertoCol;
    @FXML
    private TableColumn<UserScore, Float> avanzatoCol;
    @FXML
    private TableColumn<UserScore, Float> intermedioCol;
    @FXML
    private TableColumn<UserScore, Float> totaleCol;


    @FXML
    public void initialize() {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        principianteCol.setCellValueFactory(new PropertyValueFactory<>("principiante"));
        intermedioCol.setCellValueFactory(new PropertyValueFactory<>("intermedio"));
        avanzatoCol.setCellValueFactory(new PropertyValueFactory<>("avanzato"));
        espertoCol.setCellValueFactory(new PropertyValueFactory<>("esperto"));
        totaleCol.setCellValueFactory(new PropertyValueFactory<>("totale"));
        ObservableList<UserScore> userScores = FXCollections.observableArrayList(getUserScores());
        tbView.getColumns().stream().forEach(col -> col.getStyleClass().add("table-cell")); // Aggiunge la classe CSS table-cell a tutte le colonne
        tbView.setItems(userScores);
    }
    private void esci() { Main.changeScene("View/UserHome.fxml"); }
    private List<UserScore> getUserScores() {
        return UserDBM.getAllUsersOrderedByUsername().stream().map(user -> {
            try{
                Float principiante = Punteggio.getSafePunteggio(PunteggioDBM.getPunteggiByUserGrado(Objects.requireNonNull(user), Grado.PRINCIPIANTE));
                Float intermedio = Punteggio.getSafePunteggio(PunteggioDBM.getPunteggiByUserGrado(Objects.requireNonNull(user), Grado.INTERMEDIO));
                Float avanzato = Punteggio.getMaxPunteggio(user, Grado.AVANZATO);
                Float esperto = Punteggio.getMaxPunteggio(user, Grado.ESPERTO);
                Float totale = principiante + intermedio + avanzato + esperto;
                return new UserScore(user, principiante, intermedio, avanzato, esperto, totale);
            } catch (ConnectionException e  ) { Main.changeScene("View/ErroreDatabase.fxml");
            } catch (Exception e            ) { e.printStackTrace();}
            return null;
        }).collect(Collectors.toList());
    }
    @FXML
    void EsciOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) esci(); }
    @FXML
    void EsciOnMouseClicked(MouseEvent event) { esci(); }
}
// src/main/java/unibo/javafxmvc/model/UserScore.java
