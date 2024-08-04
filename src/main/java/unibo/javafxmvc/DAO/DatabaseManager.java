package unibo.javafxmvc.DAO;

import java.sql.*;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;
/* Da un'analisi del log di refresh del database è risultato che:
    Inizio introspezione: 20:12:27
    Esecuzione della prima query: 20:12:28
    Tempo di esecuzione della query: 18 ms
    Tempo di recupero dei dati: 47 ms
    Connessione al database stabilita: 20:12:28
 */
public class DatabaseManager {
    private static String dbURL;
    private static Connection connection;
    private static final String connectionExceptionMessage;
    private static final String npeMessage;

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
    }
    public static void informStatus(Status st){
        System.out.println(String.format("Connessione al database %-10s con successo", st));
    }
    /**@return true se la connessione è stata stabilita correttamente e l'inserimento è andato a termine; <br> false se l'inserimento non è andato a termine correttamente
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Boolean insertUser(User usr) throws ConnectionException{
        if(connection != null){
            int affectedRows = 0;
            try(PreparedStatement pstmt = connection.prepareStatement(
        "INSERT INTO \"User\" (NOME, COGNOME, USERNAME, PASSWORD, AVATAR, COLOR) VALUES (?, ?, ?, ?, ?, ?)")){
                pstmt.setString(1, usr.getNome());
                pstmt.setString(2, usr.getCognome());
                pstmt.setString(3, usr.getUsername());
                pstmt.setString(4, usr.getPassword());
                pstmt.setBytes(5, usr.getAvatar());
                pstmt.setString(6, usr.getColor());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();

                return false;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return true se la password dell'utente corrisponde a quella memorizzata nel database; <br> false non corrisponde
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query di recupero o l'Utente non è stato trovato o il dato è corrotto
     * */
    public static Boolean userExists(String username) throws ConnectionException, SQLException {
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
        "SELECT USERNAME FROM \"User\" WHERE USERNAME = ?")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                throw e;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    public static Boolean checkPasswordForUser(String username, String password) throws ConnectionException, SQLException {
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
        "SELECT PASSWORD FROM \"User\" WHERE USERNAME = ?")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("PASSWORD").equals(password);
                } else {
                    throw new SQLException("Utente non trovato");
                }
            } catch (SQLException e) {
                throw e;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return l'utente con lo username specificato; <br> null se l'utente non è stato trovato
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query di recupero
     * */
    public static User getUser(String username) throws ConnectionException, SQLException {
        if(connection != null) {
            try (PreparedStatement pstmt = connection.prepareStatement(
        "SELECT NOME, COGNOME, USERNAME, PASSWORD FROM \"User\" WHERE USERNAME = ?")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBytes("avatar"),
                            rs.getString("color")
                    );
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw e;
            }
        }
        else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
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
}
