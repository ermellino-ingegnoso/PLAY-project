package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.BloccoEspertoDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.BloccoEsperto;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.util.Compiler;
import unibo.javafxmvc.util.SignatureFinder;

import static unibo.javafxmvc.util.CodeValidator.combineCode;

public class BloccoEspertoController{
    private BloccoEsperto be;
    @FXML
    private Label lblConsegna;
    @FXML
    private Label lblIndice;
    @FXML
    private Label lblFirma;
    @FXML
    private Label lblTitolo;
    @FXML
    private TextArea tfClasse;
    @FXML
    private TextArea tfMetodo;
    @FXML
    private Button btnProsegui;
    @FXML
    private SplitPane splitPaneBlocco;

    @FXML
    public void initialize() {    //  Logica di inizializzazione iterativa
        AuxiliaryController.addTooltipTo(lblFirma,  Duration.millis(500), "Reimposta la firma del metodo ed elimina il corpo");
        Main.generalCounter = Main.esercizioCorrente.getNblocchiEsperto();
        tfClasse.setEditable(false);
        fillFields();
    }
    @FXML
    void AnnullaOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) viewPunteggio();}
    @FXML
    void AnnullaOnMouseClicked(MouseEvent event) {
        viewPunteggio();
    }
    @FXML
    void SaveOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) save();}
    @FXML
    void SaveOnMouseClicked(MouseEvent event) {
        save();
    }
    private void save(){
        btnProsegui.setDisable(true);
        be.setCodiceUtente(tfMetodo.getText().trim());
        be.setSuperato(checkCode());
        try{ BloccoEspertoDBM.updateBloccoEsperto(be);}
        catch (ConnectionException ce){
            AuxiliaryController.alertWindow("Errore", "Errore di connessione", "EsercizioEsperto non inserito");
            Main.changeScene("View/ErroreDatabase.fxml");
        }
        if(Main.bloccoIndex >= Main.esercizioCorrente.getNblocchiEsperto()-1) viewPunteggio();
        else {
            Main.bloccoIndex++;
            fillFields();
        }
    }
    @FXML
    private void lblReimpostaFirmaOnMouseClicked(MouseEvent event) {reimpostaFirma();}

    private void reimpostaFirma(){
        tfMetodo.setText((Main.gradoAttuale == Grado.ESPERTO) ? SignatureFinder.extractSignature(be.getBloccoGenerico().getMetodo())+"{\n\n}" : be.getBloccoGenerico().getMetodo());
    }
    private void fillFields(){
        Main.punteggio = Main.esercizioCorrente.getPunteggi();
        Main.changeScene("View/PunteggiEsercizio.fxml");
        /*
        try {
            be = Main.esercizioCorrente.getBloccoEsperto(Main.bloccoIndex);
            if (be.isSuperato()) {    // se il blocco è già stato superato non viene mostrato
                if (Main.bloccoIndex >= Main.generalCounter - 1) viewPunteggio();
                else {
                    Main.bloccoIndex++;
                    fillFields();
                }
            } else {
                lblConsegna.setText(be.getBloccoGenerico().getConsegna());
                lblTitolo.setText(Main.esercizioCorrente.getRegola().getTitolo());
                lblIndice.setText("Blocco " + (Main.bloccoIndex + 1) + " di " + Main.esercizioCorrente.getNblocchiEsperto());
                tfClasse.setText(SignatureFinder.extractClassWithoutMain((be.getBloccoGenerico().getCodice())));
                if (Main.gradoAttuale == Grado.AVANZATO) {
                    tfClasse.setDisable(true);
                    tfClasse.setVisible(false);
                    splitPaneBlocco.setDividerPositions(100.0);
                }
                reimpostaFirma();
                btnProsegui.setDisable(false);
            }
        } catch(Exception e) {viewPunteggio();}

         */
    }
    private void viewPunteggio(){
        System.out.println("Fine esercizio esperto");
        Main.bloccoIndex = 0;
        Main.generalCounter = 0;
        Main.punteggio = Main.esercizioCorrente.getPunteggi();
        Main.esercizioCorrente = null;
        Main.changeScene("View/PunteggiEsercizio.fxml");
    }
    private Boolean checkCode() {
        if(Main.gradoAttuale == Grado.ESPERTO){
            try {
                String userOutput = Compiler.compileAndRunCode(combineCode(be.getBloccoGenerico().getCodice(), be.getCodiceUtente()));
                String correctOutput = Compiler.compileAndRunCode(combineCode(be.getBloccoGenerico().getCodice(), be.getBloccoGenerico().getMetodo())); // Classe intera corretta);
                // Debug: stampa gli output
                System.out.println("User Output: " + userOutput);
                System.out.println("Correct Output: " + correctOutput);
                return userOutput.equals(correctOutput);
            } catch (Exception e) {
                System.err.println("Errore per la compilazione o esecuzione durante la verifica del Blocco Esperto: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else{
            try {
                String userOutput = Compiler.compileAndRunCode(combineCode(SignatureFinder.removeMethodFromClass(be.getBloccoGenerico().getCodice(), be.getBloccoGenerico().getMetodo()), be.getCodiceUtente()));
                String correctOutput = Compiler.compileAndRunCode(be.getBloccoGenerico().getCodice());
                // Debug: stampa gli output
                System.out.println("User Output: " + userOutput);
                System.out.println("Correct Output: " + correctOutput);
                System.out.println("Risultato di verifica: " + userOutput.equals(correctOutput));
                return userOutput.equals(correctOutput);
            } catch (Exception e) {
                System.err.println("Errore per la compilazione o esecuzione durante la verifica del Blocco Esperto: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
    }
}

