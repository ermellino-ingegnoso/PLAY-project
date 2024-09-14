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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassificaGeneraleController {
    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private TableView<UserScore> tbView;
    @FXML
    private ImageView userAvatar;

    @FXML
    public void initialize() {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);

        TableColumn<UserScore, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<UserScore, Float> principianteCol = new TableColumn<>("Principiante");
        principianteCol.setCellValueFactory(new PropertyValueFactory<>("principiante"));
        TableColumn<UserScore, Float> intermedioCol = new TableColumn<>("Intermedio");
        intermedioCol.setCellValueFactory(new PropertyValueFactory<>("intermedio"));
        TableColumn<UserScore, Float> avanzatoCol = new TableColumn<>("Avanzato");
        avanzatoCol.setCellValueFactory(new PropertyValueFactory<>("avanzato"));
        TableColumn<UserScore, Float> espertoCol = new TableColumn<>("Esperto");
        espertoCol.setCellValueFactory(new PropertyValueFactory<>("esperto"));
        TableColumn<UserScore, Float> totaleCol = new TableColumn<>("Totale");
        totaleCol.setCellValueFactory(new PropertyValueFactory<>("totale"));

        tbView.getColumns().addAll(usernameCol, principianteCol, intermedioCol, avanzatoCol, espertoCol, totaleCol);

        ObservableList<UserScore> userScores = FXCollections.observableArrayList(getUserScores());
        tbView.setItems(userScores);
        /*
        String[] columnNames = {"Username", "Principiante", "Intermedio", "Avanzato", "Esperto", "Totale"};
        for (String columnName : columnNames) {
            TableColumn<UserScore, Integer> column = new TableColumn<>(columnName);
            column.setCellValueFactory(new PropertyValueFactory<>(columnName.replace(" ", "").toLowerCase()));
            tbView.getColumns().add(column);
        }
        ObservableList<UserScore> userScores = FXCollections.observableArrayList(getUserScores());
        tbView.setItems(userScores);
         */
    }
    @FXML
    void EsciOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) esci(); }
    @FXML
    void EsciOnMouseClicked(MouseEvent event) { esci(); }
    private void esci() { Main.changeScene("View/UserHome.fxml"); }
    private List<UserScore> getUserScores() {
        return UserDBM.getAllUsersOrderedByUsername().stream().map(user -> {
            try{
                Float principiante = getSafePunteggio(PunteggioDBM.getPunteggiByUserGrado(Objects.requireNonNull(user), Grado.PRINCIPIANTE));
                Float intermedio = getSafePunteggio(PunteggioDBM.getPunteggiByUserGrado(Objects.requireNonNull(user), Grado.INTERMEDIO));
                Float avanzato = getMaxPunteggio(user, Grado.AVANZATO);
                Float esperto = getMaxPunteggio(user, Grado.ESPERTO);
                Float totale = principiante + intermedio + avanzato + esperto;
                return new UserScore(user, principiante, intermedio, avanzato, esperto, totale);
            } catch (ConnectionException e  ) { Main.changeScene("View/ErroreDatabase.fxml");
            } catch (Exception e            ) { e.printStackTrace();}
            return null;
        }).collect(Collectors.toList());
    }
    private Float getSafePunteggio(ArrayList<Punteggio> punteggi) throws ConnectionException {
        Punteggio maxPunteggio = Punteggio.getMaxPunteggio(punteggi);
        return maxPunteggio != null ? maxPunteggio.getPunteggio() : 0.0f;
    }
    private Float getMaxPunteggio(User user, Grado grado) throws ConnectionException {
        Punteggio punteggio = Punteggio.getMaxPunteggioByUserAndGrado(user, grado);
        return (punteggio != null) ? punteggio.getPunteggio() : 0.0f;
    }
}
// src/main/java/unibo/javafxmvc/model/UserScore.java
