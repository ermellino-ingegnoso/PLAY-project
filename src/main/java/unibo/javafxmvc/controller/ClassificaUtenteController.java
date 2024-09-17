package unibo.javafxmvc.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;
import unibo.javafxmvc.model.PunteggioEsercizio;

import java.util.ArrayList;
import java.util.List;

public class ClassificaUtenteController {
    @FXML
    private Circle circleAvatar;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Label lblTot;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private TableView<PunteggioEsercizio> tbView;
    @FXML
    private TableColumn<PunteggioEsercizio, Float> totaleColumn;
    @FXML
    private TableColumn<PunteggioEsercizio, String> esercizioColumn;
    private Punteggio punteggioPrincipiante;
    private Punteggio punteggioIntermedio;
    private Punteggio punteggioAvanzato;
    private Punteggio punteggioEsperto;

    @FXML
    private void initialize() {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);
        int colNum = 3;
        List<PunteggioEsercizio> punteggiEsercizi = new ArrayList<>();


        try{
            punteggioPrincipiante = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.PRINCIPIANTE);
            punteggioIntermedio = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.INTERMEDIO);
            punteggioAvanzato = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.AVANZATO);
            punteggioEsperto = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.ESPERTO);
        } catch(ConnectionException ce){Main.changeScene("View/ErroreDatabase");}
        if(punteggioAvanzato != null){
            if(punteggioAvanzato.getNPunti() > colNum) colNum = punteggioAvanzato.getNPunti();
            punteggiEsercizi.add(new PunteggioEsercizio(punteggioAvanzato.getGrado(), FXCollections.observableArrayList(punteggioAvanzato.getPuntiPonderati())));
        }
        if(punteggioEsperto != null){
            if(punteggioEsperto.getNPunti() > colNum) colNum = punteggioEsperto.getNPunti();
            punteggiEsercizi.add(new PunteggioEsercizio(punteggioEsperto.getGrado(), FXCollections.observableArrayList(punteggioEsperto.getPuntiPonderati())));
        }
        if(punteggioIntermedio != null){
            punteggiEsercizi.add(new PunteggioEsercizio(punteggioIntermedio.getGrado(), FXCollections.observableArrayList(punteggioIntermedio.getPuntiPonderati())));
        }
        if(punteggioPrincipiante != null){
            punteggiEsercizi.add(new PunteggioEsercizio(punteggioPrincipiante.getGrado(), FXCollections.observableArrayList(punteggioPrincipiante.getPuntiPonderati())));
        }
        for (int i = 0; i < colNum; i++) {
            final int colIndex = i;
            TableColumn<PunteggioEsercizio, Float> col = new TableColumn<>("Punteggio " + (i + 1));
            col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().punteggiProperty().get(colIndex)));
            col.getStyleClass().add("table-cell");
            tbView.getColumns().add(col);
        }
        esercizioColumn.setCellValueFactory(cellData -> cellData.getValue().gradoProperty());
        totaleColumn.setCellValueFactory(cellData -> cellData.getValue().totaleProperty().asObject());

        ObservableList<PunteggioEsercizio> righe = FXCollections.observableArrayList(punteggiEsercizi);
        tbView.setItems(righe);
        lblTot.setText("Il tuo punteggio totale è: " + getTotaleSomma());
    }
    public void ridimensinaColonne(TableView<?> tableView) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().forEach(column -> {
            column.setPrefWidth(tableView.getWidth() / tableView.getColumns().size());
        });
    }
    public float getTotaleSomma() {
        ObservableList<PunteggioEsercizio> items = tbView.getItems();
        float somma = 0.0f;
        for (PunteggioEsercizio item : items) {
            somma += item.totaleProperty().get();
        }
        return somma;
    }
    private void esci(){ Main.changeScene("View/UserHome.fxml");}
    @FXML
    private void EsciOnKeyPressed(KeyEvent event) {if(AuxiliaryController.keyEnterPressed(event)) esci();}
    @FXML
    private void EsciOnMouseClicked(MouseEvent event) {esci();}
}
