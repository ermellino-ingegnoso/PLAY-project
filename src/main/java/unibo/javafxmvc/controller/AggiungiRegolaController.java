package unibo.javafxmvc.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unibo.javafxmvc.DAO.RegolaGenericaDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.BloccoGenerico;
import unibo.javafxmvc.model.EsercizioGenerico;
import unibo.javafxmvc.model.Regola;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static unibo.javafxmvc.DAO.EsercizioGenericoDBM.insertEsercizioGenerico;

public class AggiungiRegolaController {
    @FXML
    private TextField tfDomanda;
    @FXML
    private TextField tfTitolo;
    @FXML
    private TextArea tfDidascalia;

    @FXML
    void AnnullaOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/AdminHome.fxml");
    }
    @FXML
    void AnnullaOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/AdminHome.fxml");
    }
    @FXML
    void SaveOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) save(event);
    }
    @FXML
    void SaveOnMouseClicked(MouseEvent event) {
        save(event);
    }
    @FXML
    private void save(Event event){
        if(checkFields()) {
            Regola regola = new Regola(tfTitolo.getText().trim(), tfDomanda.getText().trim(), tfDidascalia.getText().trim(), Main.gradoAttuale);
            EsercizioGenerico esercizioGenerico = new EsercizioGenerico(null, Main.currentUser, regola, new ArrayList<BloccoGenerico>());    // id = null potrebbe dare problemi
            Main.ultimoEsercizioGenerico = esercizioGenerico;
            try{
                if(RegolaGenericaDBM.insertRegola(regola)){
                    try{esercizioGenerico.setId(Objects.requireNonNull(insertEsercizioGenerico(esercizioGenerico)));}
                    catch(NullPointerException e){System.err.println("AggiungiRegolaController#save(): Errore di recupero dell'ID di esercizio generico:" + e.getMessage());}
                    recuperaNumeroBlocchi(event);
                } else {
                    AuxiliaryController.alertWindow("Errore", "Regola già esistente", "Inserire una regola con un titolo diverso");
                }
            } catch(ConnectionException e) {
                Main.changeScene("View/ErroreDatabase.fxml");
                AuxiliaryController.alertWindow("Errore", "Errore di connessione al database", "Regola non salvata");
            }
        }
    }
    @FXML
    private void recuperaNumeroBlocchi(Event event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/ImpostaNumeroBlocchi.fxml")); // Set the correct path to your FXML file
            Stage dialogStage = new Stage();
            dialogStage.setTitle("");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            ImpostaNumeroBlocchiController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            if (controller.isOkClicked()) {
                Main.generalCounter = controller.getNumber();
                Main.changeScene("View/AggiungiBlocco.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: eliminare l'ultimo esercizio
            AuxiliaryController.alertWindow("Errore", "Errore di caricamento della finestra di dialogo", "Chiudere e riavviare il programma");
        }
        event.consume();
    }
    private Boolean checkFields() {
        if(tfDomanda.getText().isEmpty() || tfTitolo.getText().isEmpty()) {
            AuxiliaryController.alertWindow("Errore", "Compilare tutti i campi", "Per favore, compila tutti i campi");
            return false;
        }
        return true;
    }
}
