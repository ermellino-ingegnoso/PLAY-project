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

import unibo.javafxmvc.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
// ricorda di impostare main.punteggio prima di chiamare la vista associata a questo controller
public class PunteggiEsercizioController implements Initializable {
    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private TableView<List<Integer>> tbView;
    @FXML
    private ImageView userAvatar;
    //  alert.showAndWait();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblInfo.setText("Nell' esercizio "+ Main.punteggio.getTitolo() +" di difficoltà "+ Main.punteggio.getGrado().toString().toLowerCase() +" hai ottenuto un totale di: "+ Main.punteggio.getPunteggio() +" punti");

        // Creiamo le colonne dinamiche basate sulla dimensione dell'array
        for (int i = 0; i < Main.punteggio.getPunteggi().size(); i++) {
            final int colIndex = i;
            TableColumn<List<Integer>, Integer> column = new TableColumn<>("Col " + i);

            // Ogni colonna utilizza i valori dell'Main.punteggio.punteggi in base al suo indice
            column.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().get(colIndex))
            );

            tbView.getColumns().add(column);
        }

        // Creiamo le righe: ogni riga è un Main.punteggio.punteggi con gli indici e i punteggi
        List<Integer> indicesRow = new ArrayList<Integer>();
        List<Integer> scoresRow = new ArrayList<Integer>();

        for (int i = 0; i < Main.punteggio.getPunteggi().size(); i++) {
            indicesRow.add(i);        // Prima riga: gli indici
            scoresRow.add(Main.punteggio.getPunto(i));  // Seconda riga: i punteggi
        }
        ObservableList<List<Integer>> rows = FXCollections.observableArrayList();
        rows.add(indicesRow);  // Aggiungi la riga con gli indici
        rows.add(scoresRow);   // Aggiungi la riga con i punteggi

        tbView.setItems(rows);  // Settiamo i dati nella tabella
    }
}
