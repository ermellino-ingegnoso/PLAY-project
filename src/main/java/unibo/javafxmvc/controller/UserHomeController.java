package unibo.javafxmvc.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import javafx.util.Duration;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.EsercizioEsperto;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;
import unibo.javafxmvc.DAO.EsercizioEspertoDBM;

public class UserHomeController {
    @FXML
    private Circle circleAvatar;
    @FXML
    private Label lblEsercizioAvanzato;
    @FXML
    private Label lblEsercizioEsperto;
    @FXML
    private Label lblUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ProgressBar pbAvanzato;
    @FXML
    private ProgressBar pbPrincipiante;
    @FXML
    private ProgressBar pbIntermedio;
    @FXML
    private ProgressBar pbEsperto;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Circle circle;
    @FXML
    private Label code1;
    @FXML
    private Label code2;

    private EsercizioEsperto esEsperto;
    private EsercizioEsperto esAvanzato;
    private Punteggio punteggioPrincipiante;
    private Punteggio punteggioIntermedio;
    private Punteggio punteggioAvanzato;
    private Punteggio punteggioEsperto;
    @FXML
    public void initialize() {
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername, mainGridPane);
        caricaEsercizi();
        recuperaPunteggi();
        pbPrincipiante.setProgress((punteggioPrincipiante != null)? (punteggioPrincipiante.getPunteggio()/punteggioPrincipiante.getPunteggioTotale()) : 0.0f);
        pbIntermedio.setProgress((punteggioIntermedio != null)? (punteggioIntermedio.getPunteggio()/punteggioIntermedio.getPunteggioTotale()) : 0.0f);
        if(esEsperto != null){
            lblEsercizioEsperto.setText(esEsperto.getRegola().getTitolo());
            pbEsperto.setProgress((punteggioEsperto.getPunteggio()/punteggioEsperto.getPunteggioTotale()));
        }
        else pbEsperto.setProgress(0);
        if(esAvanzato != null){
            lblEsercizioAvanzato.setText(esAvanzato.getRegola().getTitolo());
            pbAvanzato.setProgress((punteggioAvanzato.getPunteggio()/punteggioAvanzato.getPunteggioTotale()));
        }
        else pbAvanzato.setProgress(0);
        startTextTypingEffect(code1, "User admin = new User(\"admin\", \"admin\", \"admin\", User.getSHA256Hash(\"admin\"), new Image(Objects.requireNonNull(Main.class.getResourceAsStream(\"/unibo/javafxmvc/Images/avatar.png\"))), \"0xffffffff\");        if (!UserDBM.userExists(\"admin\", true)) UserDBM.insertUser(admin, true);");
        startTextTypingEffect(code2, "eg.setId(Objects.requireNonNull(insertEsercizioGenerico(eg), \"ID Esercizio Generico Trova errore non recuperato\")); bgSomma.setId(Objects.requireNonNull(insertBloccoGenerico(bgSomma, eg), \"ID Blocco Generico Somma non recuperato\"));     ");
    }
    @FXML
    void AvatarOnMousePressed(MouseEvent event) {
        Main.changeScene("View/ModificaProfilo.fxml");
    }
    private void recuperaPunteggi(){
        try{
            punteggioPrincipiante = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.PRINCIPIANTE);
            punteggioIntermedio = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.INTERMEDIO);
            // punteggioAvanzato = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.AVANZATO);
            // punteggioEsperto = Punteggio.getMaxPunteggioByUserAndGrado(Main.currentUser, Grado.ESPERTO);
            punteggioEsperto = esEsperto.getPunteggi();
            punteggioAvanzato = esAvanzato.getPunteggi();
        } catch(ConnectionException ce){Main.changeScene("View/ErroreDatabase");
        } catch (NullPointerException npe){System.err.println("Recupero punteggi fallito");}
    }
    @FXML
    void AvanzatoOnMouseClicked(MouseEvent event) {
        esercizioAvanzato();
    }
    private void esercizioAvanzato(){
        if(esAvanzato == null){ //  in caso di fallimento nella ricerca di esercizi esperti incompleti
            AuxiliaryController.alertWindow("Errore", "Non è stato possibile creare un nuovo esercizio per il grado avanzato", "Riprova più tardi");
        } else{
            Main.esercizioCorrente = esAvanzato;
            Main.gradoAttuale = Grado.AVANZATO;
            Main.changeScene("View/Regola.fxml");
        }
    }
    @FXML
    void EspertoOnMouseClicked(MouseEvent event) {
        esercizioEsperto();
    }
    private void esercizioEsperto(){
        if(esEsperto == null){ //  in caso di fallimento nella ricerca di esercizi esperti incompleti
            AuxiliaryController.alertWindow("Errore", "Non è stato possibile creare un nuovo esercizio per il grado esperto", "Riprova più tardi");
        } else{
            Main.esercizioCorrente = esEsperto;
            Main.gradoAttuale = Grado.ESPERTO;
            Main.changeScene("View/Regola.fxml");
        }
    }
    @FXML
    private void OrdinaPassiOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Commenti.fxml");
    }
    @FXML
    private void QuizOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Quiz.fxml");
    }
    @FXML
    private void ScollegatiOnKeyPressed(KeyEvent event) {if(AuxiliaryController.keyEnterPressed(event)) scollega();}
    @FXML
    private void ScollegatiOnMouseClicked(MouseEvent event) {scollega();}
    private void scollega(){
        Main.gradoAttuale = null;
        Main.currentUser = null;
        Main.esercizioCorrente = null;
        Main.changeScene("View/Accesso.fxml");
    }
    public void caricaEsercizi(){
        try{
            esEsperto = EsercizioEspertoDBM.getOrCreateEsercizioEspertoForUser(Main.currentUser, Grado.ESPERTO);
            esAvanzato = EsercizioEspertoDBM.getOrCreateEsercizioEspertoForUser(Main.currentUser, Grado.AVANZATO);
            if(esEsperto == null) esAvanzato = EsercizioEspertoDBM.getEsercizioEspertoWithMaxIdByGradoAndUser(Grado.ESPERTO, Main.currentUser);
            if(esAvanzato == null) esAvanzato = EsercizioEspertoDBM.getEsercizioEspertoWithMaxIdByGradoAndUser(Grado.AVANZATO, Main.currentUser);
        } catch (ConnectionException ce){
            Main.changeScene("View/ErroreDatabase.fxml");
        }
    }
    @FXML
    void AvanzatoOnKeyPressed(KeyEvent event) {     if(AuxiliaryController.keyEnterPressed(event)) esercizioAvanzato();}
    @FXML
    void EspertoOnKeyPressed(KeyEvent event) {      if(AuxiliaryController.keyEnterPressed(event)) esercizioEsperto();}
    @FXML
    void PrincipianteOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/Quiz.fxml");}
    @FXML
    void IntermedioOnKeyPressed(KeyEvent event) {   if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/Commenti.fxml");}
    @FXML
    private void ClassificaGeneraleOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) classificaGenerale();}
    @FXML
    private void ClassificaGeneraleOnMouseClicked(MouseEvent event) { classificaGenerale();}
    @FXML
    private void ClassificaUtenteOnKeyPressed(KeyEvent event) { if(AuxiliaryController.keyEnterPressed(event)) classificaUtente();}
    @FXML
    private void ClassificaUtenteOnMouseClicked(MouseEvent event) {classificaUtente();}
    private void classificaGenerale(){  Main.changeScene("View/ClassificaGenerale.fxml");}
    private void classificaUtente(){    Main.changeScene("View/ClassificaUtente.fxml");}
    private void startTextTypingEffect(Label label, String text) {
        label.setText("");
        final int[] index = {0};

        final Timeline[] typingTimeline = new Timeline[1]; // Array per contenere la Timeline

        typingTimeline[0] = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (index[0] < text.length()-1) {
                label.setText(text.substring(0, index[0] + 1)+ "|");
                index[0]++;
            } else {
                typingTimeline[0].stop();
                label.setText(text);
                startTextDeletingEffect(label, text);
            }
        }));
        typingTimeline[0].setCycleCount(text.length());
        typingTimeline[0].play();
    }
    private void startTextDeletingEffect(Label label, String text) {
        final int[] index = {text.length() - 1};
        final Timeline[] deletingTimeline = new Timeline[1]; // Array to hold the Timeline

        deletingTimeline[0] = new Timeline(new KeyFrame(Duration.millis(60), e -> {
            if (index[0] >= 1) {
                label.setText(text.substring(0, index[0])+"|");
                index[0]--;
            } else {
                deletingTimeline[0].stop();
                label.setVisible(false);
            }
        }));
        deletingTimeline[0].setCycleCount(text.length());
        deletingTimeline[0].play();
    }
}
