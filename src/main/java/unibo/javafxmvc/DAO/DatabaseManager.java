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
        npeMessage = "Connection poit to null";
    }
    /**inizializza la connessione al database con l'URL specificato
     * @param dbURL URL del database
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static void inizialize(String dbURL) throws ConnectionException {
        DatabaseManager.dbURL = dbURL;
        try {
            Connection connection = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            throw new ConnectionException("Errore di connessione al database: " + e.getMessage(), e);
        }
    }
    /**@return true se la connessione è stata stabilita correttamente e l'inserimento è andato a termine
     * @return false se l'inserimento non è andato a termine correttamente
     * @trhow ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Boolean insertUser(User usr) throws ConnectionException{
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
        "INSERT INTO User (NOME, COGNOME, USERNAME, PASSSWORD) VALUES (?, ?, ?, ?)")){
                pstmt.setString(1, usr.getNome());
                pstmt.setString(2, usr.getCognome());
                pstmt.setString(3, usr.getUsername());
                pstmt.setString(4, usr.getPassword());
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return true se la password dell'utente corrisponde a quella memorizzata nel database
     * @return false non corrisponde
     * @trhow ConnectionException se la connessione non è stata stabilita correttamente
     * @trhow SQLException se si verifica un errore durante l'esecuzione della query di recupero o l'Utente non è stato trovato o il dato è corrotto
     * */
    public static Boolean checkPasswordForUser(String username, String password) throws ConnectionException, SQLException {
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
        "SELECT PASSWORD FROM User WHERE USERNAME = ?")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("PASSWORD").equals(password);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw e;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return l'utente con lo username specificato
     * @return null se l'utente non è stato trovato
     * @trhow ConnectionException se la connessione non è stata stabilita correttamente
     * @trhow SQLException se si verifica un errore durante l'esecuzione della query di recupero
     * */
    public static User getUser(String username) throws ConnectionException, SQLException {
        if(connection != null) {
            try (PreparedStatement pstmt = connection.prepareStatement(
        "SELECT NOME, COGNOME, USERNAME, PASSWORD FROM User WHERE USERNAME = ?")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String nome = rs.getString("NOME");
                    String cognome = rs.getString("COGNOME");
                    String user = rs.getString("USERNAME");
                    String password = rs.getString("PASSWORD");
                    return new User(nome, cognome, user, password);
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
     * @trhow SQLException se si verifica un errore durante la chiusura della connessione
     * @see DatabaseManager#inizialize(String)
     * */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw e;
            }
        }
    }
}
