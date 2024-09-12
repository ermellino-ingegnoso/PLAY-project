package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import unibo.javafxmvc.DAO.BloccoGenericoDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.BloccoGenerico;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.util.CodeValidator;
import unibo.javafxmvc.util.Compiler;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AggiungiBloccoController implements Initializable {
    @FXML
    private Button btnSave;
    @FXML
    private Label lblIndice;
    @FXML
    private Label lblTitle;
    @FXML
    private TextArea tfCodice;
    @FXML
    private TextArea tfCodiceMetodo;
    @FXML
    private TextArea tfConsegna;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.gradoAttuale == Grado.ESPERTO){
            lblTitle.setText("Aggiungi Esercizio Blocco Mancante");
            //tfCodice.setPromptText("Codice della Classe del blocco");
            tfCodiceMetodo.setPromptText("public static int nome_Metodo(Param p){}");
        }
        else{
            lblTitle.setText("Aggiungi Esercizio Trova l'errore");
            //tfCodice.setPromptText("Codice con errore");
            tfCodiceMetodo.setPromptText("public static int nome_Metodo(Param p){/*metodo con errore*/}");
        }
        lblIndice.setText("Blocco " + (Main.ultimoEsercizioGenerico.getNumeroBlocchiInseriti() + 1 ) + " di " + Main.generalCounter);
    }
    @FXML
    private void AnnullaOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) annulla();}
    @FXML
    private void AnnullaOnMouseClicked(MouseEvent event) { annulla();}
    @FXML
    void SaveOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) save();}
    @FXML
    void SaveOnMouseClicked(MouseEvent event) { save();}
    private void annulla(){ Main.changeScene("View/AdminHome.fxml");}
    @FXML
    void helpBtnOnMouseClicked(MouseEvent event) {
        if(Main.gradoAttuale == Grado.ESPERTO) AuxiliaryController.showInformation(
        "Guida",
    "Guida Aggiungi Blocco",
    "Inserisci la consegna dell'esercizio, il codice della classe e il codice del metodo non implementato.\n" +
                "Il codice della classe deve contenere un metodo main che chiama il metodo non implementato.\n" +
                "Nell'area di testo dedicata inserire firma e implementazione del metodo richiamato.\n" +
                "Il codice del metodo implementato dall'utente non deve essere implementato nell'area di testo della classe (si può inserire unicamente la firma()).\n");
        else AuxiliaryController.showInformation(
        "Guida",
    "Guida Aggiungi Blocco",
    "Inserisci la consegna dell'esercizio, il codice della classe con il metodo corretto ed il codice del metodo sbagliato.\n" +
                "Il codice della classe deve contenere un metodo main che chiama il metodo.\n" +
                "Nell'area di testo dedicata alla classe e in quella dedicata al metodo inserire firma e implementazione del metodo richiamato .\n");
    }
    private Boolean checkFields(){
        if(Main.gradoAttuale == Grado.ESPERTO) return !tfConsegna.getText().trim().isEmpty() &&
                !tfCodice.getText().trim().isEmpty() &&
                !tfCodiceMetodo.getText().trim().isEmpty() &&
                CodeValidator.checkCodice(tfCodice.getText().trim(), tfCodiceMetodo.getText().trim());
        else return !tfConsegna.getText().trim().isEmpty() &&
                !tfCodice.getText().trim().isEmpty() &&
                !tfCodiceMetodo.getText().trim().isEmpty() &&
                CodeValidator.checkCodiceAvanzato(tfCodice.getText().trim(), tfCodiceMetodo.getText().trim());
    }
    private void save() {
       if (checkFields()) {
           try{// differenziazione per Grado
               String result = Compiler.compileAndRunCode((Main.gradoAttuale == Grado.ESPERTO) ? CodeValidator.combineCode(tfCodice.getText().trim(), tfCodiceMetodo.getText().trim()) : tfCodice.getText().trim());
               AuxiliaryController.showResultModal("Risultato Esecuzione", !result.isEmpty() ? result : "Compilazione ed esecuzione completate con successo.");
               if (AuxiliaryController.confirmSave("Conferma", "Conferma l'inserimento dei campi", "Vuoi confermare l'inserimento dei campi?")) {
                   BloccoGenerico blocco = new BloccoGenerico(tfConsegna.getText().trim(), tfCodice.getText().trim(), tfCodiceMetodo.getText().trim());
                   Main.ultimoEsercizioGenerico.addBlocco(blocco);
                   if (Main.ultimoEsercizioGenerico.getNumeroBlocchiInseriti() == Main.generalCounter) {
                       try {
                           for (BloccoGenerico b : Main.ultimoEsercizioGenerico.getBlocchi()) {
                               b.setId(Objects.requireNonNull(BloccoGenericoDBM.insertBloccoGenerico(b, Main.ultimoEsercizioGenerico), "Errore nel recupero id blocco " + b.getConsegna()));
                           }
                           Main.changeScene("View/AdminHome.fxml");
                       } catch (ConnectionException e) {
                           AuxiliaryController.alertWindow("Errore", "Errore di connessione", "EsercizioGenerico non inserito");
                           Main.changeScene("View/ErroreDatabase.fxml");
                       }
                   } else Main.changeScene("View/AggiungiBlocco.fxml");
               }
           } catch(NullPointerException npe){
               System.err.println("Errore recupero ID: " + npe.getMessage());
               AuxiliaryController.alertWindow("Errore", "Errore durante il recupero dell'ID del blocco", "Eliminare l'esercizio corrente");
           } catch (IOException ioe){
               AuxiliaryController.alertWindow("Errore", "Errore di I/O", "Errore durante la compilazione o l'esecuzione del codice: " + ioe.getMessage());
           } catch (InterruptedException ie){
               AuxiliaryController.alertWindow("Errore", "Errore di interruzione", ie.getMessage());
           } catch (RuntimeException re){
               AuxiliaryController.alertWindow("Errore", "Errore di runtime", re.getMessage());
           }
       }
   }
}
/*
public static enum AlertType {NONE, INFORMATION, WARNING, CONFIRMATION, ERROR}




 */