package unibo.javafxmvc.DAO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import unibo.javafxmvc.exception.ConnectionException;

import javax.imageio.ImageIO;

/* Da un'analisi del log di refresh del database è risultato che:
    Inizio introspezione: 20:12:27
    Esecuzione della prima query: 20:12:28
    Tempo di esecuzione della query: 18 ms
    Tempo di recupero dei dati: 47 ms
    Connessione al database stabilita: 20:12:28

    ⚠️ per resettare gli id della tabella user: ALTER TABLE "User" ALTER COLUMN ID RESTART WITH 1;
 */
public class DatabaseManager {
    protected static String dbURL;
    protected static Connection connection;
    protected static final String connectionExceptionMessage;
    protected static final String npeMessage;

    static{
        connection = null;
        connectionExceptionMessage = "Errore di connessione al database: connessione nulla. \n DatabaseManager.inizialize() non è stato eseguito o è stato invocato un metodo senza prevedere la connessione";
        npeMessage = "Connection point to null";
    }
    /**inizializza la connessione al database con l'URL specificato
     * @param dbURL URL del database
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static void inizialize(String dbURL) throws ConnectionException {
        DatabaseManager.dbURL = dbURL;
        try {
            connection = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            throw new ConnectionException("Errore di connessione al database: " + e.getMessage(), e);
        }
        informStatus(Status.STABILITA);
        createAllTablesIfNotExists();
    }
    public static void createAllTablesIfNotExists(){
        try{// I metodi di creazione devono essere chiamati in ordine di dipendenza: PK --> FK (PK) --> FK...
            createUserIfNotExists();
            createAdminIfNotExists();
            createRegolaIfNotExists();
            createEsercizioGenericoIfNotExists();
            createBloccoGenericoIfNotExists();
            createEsercizioEspertoIfNotExists();
            createBloccoEspertoIfNotExists();
            createPunteggioIfNotExists();
            createPuntoIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void informStatus(Status st){
        System.out.println(String.format("Connessione al database %-10s con successo", st));
    }
    /**Chiude la connessione al database
     * @throws SQLException se si verifica un errore durante la chiusura della connessione
     * @see DatabaseManager#inizialize(String)
     * */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
                informStatus(Status.CHIUSA);
            } catch (SQLException e) {
                System.out.println("Fallimento durante la chiusura della connessione");
                throw e;
            }
        }
    }
    private static void createUserIfNotExists() throws SQLException {;
        try (PreparedStatement prstmt = connection.prepareStatement(
            "create table if not exists     \"User\" (" +
                "    ID       INTEGER auto_increment," +
                "    NOME     CHARACTER VARYING not null," +
                "    COGNOME  CHARACTER VARYING not null," +
                "    USERNAME CHARACTER VARYING not null" +
                "        unique," +
                "    PASSWORD CHARACTER(64)     not null," +
                "    AVATAR   BINARY LARGE OBJECT," +
                "    COLOR    CHARACTER(10)," +
                "    constraint PK_USER" +
                "        primary key (ID)" +
                ");")) {
            prstmt.execute();
        }
    }
    private static void createAdminIfNotExists() throws SQLException {;
        try (PreparedStatement prstmt = connection.prepareStatement(
                "create table if not exists     Admin (" +
                        "    ID       INTEGER auto_increment," +
                        "    NOME     CHARACTER VARYING not null," +
                        "    COGNOME  CHARACTER VARYING not null," +
                        "    USERNAME CHARACTER VARYING not null" +
                        "        unique," +
                        "    PASSWORD CHARACTER(64)     not null," +
                        "    AVATAR   BINARY LARGE OBJECT," +
                        "    COLOR    CHARACTER(10)," +
                        "    constraint PK_ADMIN" +
                        "        primary key (ID)" +
                        ");")) {
            prstmt.execute();
        }
    }
    public static void createBloccoEspertoIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
            "create table if not exists BLOCCO_ESPERTO(ID INTEGER auto_increment, ESERCIZIO_ESPERTO_ID INTEGER not null, CODICE_UTENTE CHARACTER VARYING, SUPERATO BOOLEAN, BLOCCO_GENERICO_ID INTEGER, constraint BLOCCO_ESPERTO_PK primary key (ID), constraint BLOCCO_ESPERTO_BLOCCO_GENERICO_FK foreign key (BLOCCO_GENERICO_ID) references BLOCCO_GENERICO on update cascade on delete cascade, constraint ESERCIZIO_ESPERTO_FK foreign key (ESERCIZIO_ESPERTO_ID) references ESERCIZIO_ESPERTO on update cascade on delete cascade );"
        )){prstmt.execute();}
    }
    public static void createBloccoGenericoIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
                "create table if not exists BLOCCO_GENERICO(ID INTEGER auto_increment, ESERCIZIO_GENERICO_ESPERTO_ID INTEGER not null, CONSEGNA CHARACTER VARYING not null, CODICE CHARACTER VARYING not null, BLOCCO_CORRETTO CHARACTER VARYING, constraint BLOCCO_PK primary key (ID), constraint ESERCIZIO_GENERICO_ESPERTO_FK foreign key (ESERCIZIO_GENERICO_ESPERTO_ID) references ESERCIZIO_GENERICO on update cascade on delete cascade);"
        )){prstmt.execute();}
    }
    public static void createEsercizioEspertoIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
        "create table if not exists ESERCIZIO_ESPERTO (ID INTEGER auto_increment, ESERCIZIO_GENERICO_ID INTEGER, UTENTE_ID INTEGER, constraint ESERCIZIO_ESPERTO_PK primary key (ID), constraint ESERCIZIO_ESPERTO_GENERICO_FK foreign key (ESERCIZIO_GENERICO_ID) references ESERCIZIO_GENERICO on update cascade on delete cascade, constraint ESERCIZIO_ESPERTO_UTENTE_FK foreign key (UTENTE_ID) references \"User\" on update cascade on delete cascade );"
        )){prstmt.execute();}
    }
    public static void createEsercizioGenericoIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
        "create table if not exists ESERCIZIO_GENERICO ( ID INTEGER auto_increment, CREATORE_ID INTEGER not null, REGOLA_ID   INTEGER, constraint ESERCIZIO_GENERICO_ID primary key (ID), constraint ADMIN_ID foreign key (CREATORE_ID) references ADMIN on update cascade on delete cascade, constraint REGOLA_ID foreign key (REGOLA_ID) references REGOLA_GENERICA on update cascade on delete cascade );"
        )){prstmt.execute();}
    }
    public static void createRegolaIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
        "create table if not exists REGOLA_GENERICA ( ID INTEGER auto_increment, TITOLO CHARACTER VARYING, DOMANDA CHARACTER VARYING, DESCRIZIONE CHARACTER VARYING, GRADO CHARACTER VARYING, constraint ID primary key (ID));"
        )){prstmt.execute();}
    }
    public static void createPunteggioIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
                "create table PUNTEGGIO ( ID INTEGER auto_increment, UTENTE INTEGER, GRADO  INTEGER, TITOLO CHARACTER VARYING, constraint PUNTEGGIO_PK primary key (ID), constraint UTENTE_ID foreign key (UTENTE) references \"User\" on update cascade on delete cascade);"
        )){prstmt.execute();}
    }
    public static void createPuntoIfNotExists() throws SQLException{
        try (PreparedStatement prstmt = connection.prepareStatement(
                "create table if not exists PUNTO ( ID INTEGER auto_increment, VALORE       INTEGER, PUNTEGGIO_FK INTEGER, constraint PUNTO_PK primary key (ID), constraint PUNTEGGIO_ID foreign key (PUNTEGGIO_FK) references PUNTEGGIO on update cascade on delete cascade);"
        )){prstmt.execute();}
    }
    /**@return byte[] che rappresenta l'immagine o <br> null se si verifica un errore durante la conversione
     * @param img Immagine da convertire in byte[]
    */
    public static byte[] convertImageToByteArray(Image img) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
            ImageIO.write(bImage, "png", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**@return Image che rappresenta il byte[] o <br> null se si verifica un errore durante la conversione
     * @param img un byte[] da convertire in Image
    */
    public static Image convertByteArrayToImage(byte[] img) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(img)) {
            BufferedImage bImage = ImageIO.read(bis);
            return SwingFXUtils.toFXImage(bImage, null);
        } catch (IOException e) {
            System.err.println("Errore durante la conversione del byte[] in Image");
            e.printStackTrace();
        }
        return null;
    }
}
