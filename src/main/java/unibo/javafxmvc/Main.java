package unibo.javafxmvc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import unibo.javafxmvc.DAO.DatabaseManager;

import static unibo.javafxmvc.DAO.BloccoGenericoDBM.insertBloccoGenerico;
import static unibo.javafxmvc.DAO.EsercizioGenericoDBM.insertEsercizioGenerico;
import static unibo.javafxmvc.DAO.RegolaGenericaDBM.insertRegola;
import static unibo.javafxmvc.DAO.RegolaGenericaDBM.regolaExists;
import static unibo.javafxmvc.util.CodeValidator.checkCodice;
import static unibo.javafxmvc.util.CodeValidator.checkCodiceAvanzato;
import static unibo.javafxmvc.util.FileUtils.readFileFromResources;

import unibo.javafxmvc.DAO.PunteggioDBM;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.controller.AuxiliaryController;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.*;
import unibo.javafxmvc.controller.AppController;
import unibo.javafxmvc.util.PropertiesUtil;

public class Main extends Application {
    //  Propretà statiche di configurazione:
    private static final String iconPath;
    private static final String dbURL;
    private static final String windowTitle;

    //  Proprietà staiche di configurazione:
    private static String fxmlPath;
    public static Image icon;
    private static Boolean fullScreen;
    private static Boolean maximized;

    //  Proprietà statiche di stato:
        /** Utilizzaata per la gestione delle regole */
        public static Grado gradoAttuale;
    public static Punteggio punteggio;
    public static Scene currentScene;
    public static Stage thisStage;
    public static User currentUser;
    public static EsercizioGenerico ultimoEsercizioGenerico;
    public static EsercizioEsperto esercizioCorrente;
    public static int bloccoIndex;
    public static int generalCounter;   // Attenzione ad utilizzi multipli: gestire adeguatamente i controllers, controllare la gli usages in procedura (flow)

    static {
        fxmlPath = "View/Accesso.fxml";
        PropertiesUtil.initialize("src/main/resources/config.properties");
        dbURL = PropertiesUtil.getProperty("db.url");
        windowTitle = PropertiesUtil.getProperty("window.title");
        fullScreen = Boolean.parseBoolean(PropertiesUtil.getProperty("window.fullScreen"));
        maximized = Boolean.parseBoolean(PropertiesUtil.getProperty("window.maximized"));
        iconPath = PropertiesUtil.getProperty("icon.path");
        bloccoIndex = 0;

        Path playDir = Path.of(System.getProperty("java.io.tmpdir"), "PLAY");
        try {   // Creazione della cartella temporanea PLAY (necessario ad ogni avvio per assicurare la compatibilità con git | alternativa: gitignore sulla proprietà)
            Files.createDirectories(playDir);
            PropertiesUtil.setProperty("temp.path", playDir.toString() + FileSystems.getDefault().getSeparator());
        } catch (IOException e) {
            System.err.println("Errore durante la creazione della cartella PLAY: " + e.getMessage());
        }
    }
    public static String getDbURLlikeAbsolutePath() {
        return dbURL;
    }
    public void setFullScreen() {
        thisStage.setFullScreen(true);
    }
    public void delFullScreen() {
        thisStage.setFullScreen(false);
    }
    /** <code>changeScene</code> è un metodo che permette di cambiare la scena corrente della finestra principale
     * @param fxmlPath Percorso del file FXML della nuova scena da caricare
     * */
    public static void changeScene(String fxmlPath) {
        try {
            currentScene = new Scene((new FXMLLoader(Main.class.getResource(fxmlPath))).load());    // removeImageTags(fxmlStream)
            currentScene.getStylesheets().setAll(Objects.requireNonNull(Main.class.getResource("/unibo/javafxmvc/css/style.css")).toExternalForm());
            thisStage.setMaximized(maximized);
            thisStage.setScene(currentScene);
        } catch (Exception e) {
            System.err.println("Main.changeScene: Errore CRITICO durante il caricamento della scena: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**<code>removeImageTags</code> è stato pensato con lo scopo di rimuovere i tag <code>Image</code> dai file FXML, nel caso in cui potessero presentare problemi di caricamento
     * La regex è stata testata su un numero limitato di casi, potrebbe non funzionare con tutti i file FXML
     * @deprecated Questo metodo è stato deprecato in quanto non è più necessario: il caricamento delle immagini è stato risolto
     * @return InputStream contenente il file FXML senza i tag <code>Image</code>
     * */
    public static InputStream removeImageTags(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String content = reader.lines().collect(Collectors.joining("\n"));
            return new ByteArrayInputStream((content.replaceAll(
                    "<Image\\b[^>]*>(?:.*?)?</Image>|<Image\\b[^>]*/>\n", "")).getBytes(StandardCharsets.UTF_8));
        }
    }
    /**Aggiorna le dimensioni della finestra in base alla dimensione dello schermo allo scopo di consentire la corretta massimizzazione */
    public static void updateWindowBounds(){
        ObservableList<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(thisStage.getX(), thisStage.getY(), thisStage.getWidth(), thisStage.getHeight()));
        Rectangle2D bounds = screens.get(0).getVisualBounds();
        thisStage.setX(bounds.getMinX());
        thisStage.setY(bounds.getMinY());
        thisStage.setWidth((bounds.getWidth()/3)*2);
        thisStage.setHeight((bounds.getHeight()/3)*2);
        thisStage.centerOnScreen();
    }
    /**Configurazione iniziale di tutte le proprietà utilizzate in runtime*/
    public static void initConfig(){
        System.out.println("Main.initConfig: Inizializzazione delle proprietà dell'applicazione avviata");
        try{
            User admin = new User("admin", "admin", "admin", User.getSHA256Hash("admin"), new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/unibo/javafxmvc/Images/avatar.png"))), "0xffffffff");
            if(!UserDBM.userExists("admin", true)) UserDBM.insertUser(admin, true);
            Regola r = new Regola("Completa il blocco mancante", "quale codice è necessario per completare il blocco e compilare il codice?", "Ti verranno presentate delle classi che implementano un metodo. Il compito è quello di completare il blocco mancante nel metodo chiamato in modo da poter eseguire il programma", Grado.ESPERTO);
            if(!regolaExists(r.getTitolo())){
                insertRegola(r);
                String codice = readFileFromResources("/unibo/javafxmvc/Text/User.txt");
                String metodo = readFileFromResources("/unibo/javafxmvc/Text/UserMethod.txt");
                if(checkCodice(codice, metodo)){
                    System.out.println("Main.initConfig: Codice di User validato correttamente");
                    BloccoGenerico bgUser = new BloccoGenerico("Implementa il metodo userConMenoAnni() che prende in input una lista di User e ritorna lo User con meno anni.\n" +
                            "Avrai raggiunto lo scopo se il tuo metodo funzionerà correttamente", codice, metodo);

                    codice = readFileFromResources("/unibo/javafxmvc/Text/Prodotto.txt");
                    metodo = readFileFromResources("/unibo/javafxmvc/Text/ProdottoMethod.txt");
                    if(checkCodice(codice, metodo)){
                        System.out.println("Main.initConfig: Codice di Prodotto validato correttamente");
                        BloccoGenerico bgProdotto = new BloccoGenerico("Inserisci il codice nel metodo prodottoPiuCaro tale da rintracciare il prodotto più caro presente nella lista e ritornarlo come output del metodo.", codice, metodo);
                        EsercizioGenerico eg = new EsercizioGenerico(null, admin, r, new ArrayList<BloccoGenerico>(){{add(bgUser); add(bgProdotto);}});
                        try{
                            eg.setId(Objects.requireNonNull(insertEsercizioGenerico(eg), "ID Esercizio Generico non recuperato"));
                            bgUser.setId(Objects.requireNonNull(insertBloccoGenerico(bgUser, eg), "ID Blocco Generico User non recuperato"));     // Objects.requireNonNull lancia NullPointerException se il valore è null
                            bgProdotto.setId(Objects.requireNonNull(insertBloccoGenerico(bgProdotto, eg), "ID Blocco Generico Prodotto non recuperato"));
                        } catch(NullPointerException npe){System.err.println("Main.initConfig: Errore durante il recupero degli ID dal database: " + npe.getMessage());}
                        System.out.println("Main.initConfig: Inizializzazione Esercizio Esperto completata");
                    } else System.err.println("Main.initConfig: Errore di validazione del codice di Prodotto");
                } else System.err.println("Main.initConfig: Errore di validazione del codice di User");
            }
            Regola r2 = new Regola("Trova l'errore", "Quale riga di codice va modificata per assicurare il risultato?", "Ti verrà presentata una serie di classi con il codice della classe affiancato al codice del metodo sbagliato che dovrai correggere. La consegna dell' esercizio ti illustrerà il risultato che dovrai raggiungere", Grado.AVANZATO);
            if(!regolaExists(r2.getTitolo())){
                insertRegola(r2);
                String codice = readFileFromResources("/unibo/javafxmvc/Text/Somma.txt");
                String metodo = readFileFromResources("/unibo/javafxmvc/Text/SommaMethod.txt");
                if(checkCodiceAvanzato(codice, metodo)){
                    System.out.println("Main.initConfig: Codice di Somma validato correttamente");
                    BloccoGenerico bgSomma = new BloccoGenerico("Modifica il metodo in modo da ritornare la somma tra x ed y", codice, metodo);

                    codice = readFileFromResources("/unibo/javafxmvc/Text/SommaPari.txt");
                    metodo = readFileFromResources("/unibo/javafxmvc/Text/SommaPariMethod.txt");
                    if(checkCodiceAvanzato(codice, metodo)){
                        System.out.println("Main.initConfig: Codice di SommaPari validato correttamente");
                        BloccoGenerico bgSommaPari = new BloccoGenerico("Il metodo calcolaSommaPari dovrebbe restituire la somma di tutti e solamente i numeri pari dell' array numeri", codice, metodo);
                        codice = readFileFromResources("/unibo/javafxmvc/Text/SommaPariRicorsiva.txt");
                        metodo = readFileFromResources("/unibo/javafxmvc/Text/SommaPariRicorsivaMethod.txt");
                        if(checkCodiceAvanzato(codice, metodo)) {
                            System.out.println("Main.initConfig: Codice di SommaPariRicorsiva validato correttamente");
                            BloccoGenerico bgSommaPariRicorsiva = new BloccoGenerico("Il metodo calcolaSommaPari dovrebbe restituire la somma dei numeri pari nell' array numeri calcolata in maniera ricorsiva", codice, metodo);

                            EsercizioGenerico eg = new EsercizioGenerico(null, admin, r2, new ArrayList<BloccoGenerico>(){{add(bgSomma); add(bgSommaPari);}});
                            try{
                                eg.setId(Objects.requireNonNull(insertEsercizioGenerico(eg), "ID Esercizio Generico Trova errore non recuperato"));
                                bgSomma.setId(Objects.requireNonNull(insertBloccoGenerico(bgSomma, eg), "ID Blocco Generico Somma non recuperato"));     // Objects.requireNonNull lancia NullPointerException se il valore è null
                                bgSommaPari.setId(Objects.requireNonNull(insertBloccoGenerico(bgSommaPari, eg), "ID Blocco Generico SommaPari non recuperato"));
                                bgSommaPariRicorsiva.setId(Objects.requireNonNull(insertBloccoGenerico(bgSommaPariRicorsiva, eg), "ID Blocco Generico SommaPariRicorsiva non recuperato"));
                            } catch(NullPointerException npe){System.err.println("Main.initConfig: Errore durante il recupero degli ID dal database: " + npe.getMessage());}
                            AuxiliaryController.alertWindow("Inizializzazione", "Inizializzazione completata", "L'inizializzazione dell'applicazione è stata completata con successo");
                        } else System.err.println("Main.initConfig: Errore di validazione del codice di SommaPariRicorsiva");
                    } else System.err.println("Main.initConfig: Errore di validazione del codice di SommaPari");
                } else System.err.println("Main.initConfig: Errore di validazione del codice di Somma");
            }
        } catch(ConnectionException ce){
              Main.changeScene("View/ErroreDatabase.fxml");
              AuxiliaryController.alertWindow("Errore", "Errore di connessione al database: troppe connessioni inizializzate", "Errore durante la configurazione dell'utente Admin: chiudere e riavviare l'applicazione");
        } catch (SQLException e){ AuxiliaryController.alertWindow("Errore", "Errore nel caricamento degli utenti amministratori", "Chiudere e riavviare l'applicazione");
        } catch(IOException ioe){ AuxiliaryController.alertWindow("Errore", "Errore di I/O", "Errore durante la lettura del file di testo: " + ioe.getMessage());
        }
        System.out.println("Main.initConfig: Inizializzazione terminata");
    }
    /** Inizializza la connessione al database */
    public static void initializeDB() {
        try{DatabaseManager.inizialize(dbURL);}
        catch (ConnectionException e) {fxmlPath = "View/ErroreDatabase.fxml";}
    }
    /** Riporta il valore della proprietà <code>first.run</code>
     * @see Main#initConfig()
     * @see Main#setFirstRun(Boolean)
     * @return <code>true</code> se l'applicazione è stata avviata per la prima volta, <code>false</code> altrimenti
     * */
    public static Boolean isFirstRun() {
        return PropertiesUtil.getProperty("first.run").equals("true");
    }
    /**<code>first.run</code> indica se l'applicazione è stata avviata per la prima volta
     * @see Main#initConfig()
     * @param value Valore da assegnare alla proprietà <code>first.run</code>
     *  */
    public static void setFirstRun(Boolean value) {
        try{
            PropertiesUtil.setProperty("first.run", value.toString());
        } catch (IOException ioe) {
            System.err.println("Main.setFirstRun: Errore durante il salvataggio della proprietà first.run: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }
    @Override
    public void init() throws Exception {
        initializeDB();
        initConfig();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
        primaryStage.getIcons().add(icon);  //5a201c

        thisStage = primaryStage;
        thisStage.setTitle(windowTitle);
        thisStage.setOnCloseRequest(event -> AppController.handleWindowClose());
        thisStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {maximized = isNowMaximized;});
        thisStage.show();

        updateWindowBounds();
        changeScene(fxmlPath);
    }
    public static void main(String[] args) {
        launch(args);

    }
}