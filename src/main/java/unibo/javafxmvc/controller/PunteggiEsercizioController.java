package unibo.javafxmvc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import unibo.javafxmvc.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/** Ricorda di impostare main.punteggio di chiamare la vista associata a questo controller */
public class PunteggiEsercizioController implements Initializable {
    @FXML
    private Circle circleAvatar;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private TableView<List<Integer>> tbView;
    @FXML
    private Label lblInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AuxiliaryController.setAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);

        lblInfo.setText("Nell' esercizio "+ Main.punteggio.getTitolo() +" di difficoltà "+ Main.punteggio.getGrado().toString().toLowerCase() +" hai ottenuto un totale di: "+ Main.punteggio.getPunteggio() +" punti");

        for (int i = 0; i < Main.punteggio.getPunteggi().size(); i++) {
            final int colIndex = i;
            TableColumn<List<Integer>, Integer> column = new TableColumn<>("Esercizio " + i);
            column.setCellValueFactory(cellData ->new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().get(colIndex)));
            tbView.getColumns().add(column);
        }
        List<Integer> punti = new ArrayList<Integer>();
        for (int i = 0; i < Main.punteggio.getPunteggi().size(); i++) {
            punti.add(Main.punteggio.getPunto(i));
        }
        ObservableList<List<Integer>> righe = FXCollections.observableArrayList();
        righe.add(punti);
        tbView.setItems(righe);
    }
    @FXML
    void EsciOnKeyPressed(KeyEvent event) {Main.changeScene("View/UserHome.fxml");}
    @FXML
    void EsciOnMouseClicked(MouseEvent event) {Main.changeScene("View/UserHome.fxml");}
}
