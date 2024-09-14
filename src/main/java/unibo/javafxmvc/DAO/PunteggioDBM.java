package unibo.javafxmvc.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;
import unibo.javafxmvc.model.User;

public class PunteggioDBM extends DatabaseManager{
    /**Inserisce un nuovo <b>Punteggio</b> ed i punti ad esso asccociati nel database.
     * <p>Inserimento interno dei <code>Punto</code></p>
     * @param p l'oggetto <b>Punteggio</b> da inserire
     * @return - l'ID del <b>Punteggio</b> inserito o <br> -<code>null</code> se l'inserimento fallisce
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Integer insertPunteggio(Punteggio p) throws ConnectionException {
        if (connection != null) {
            String insertQuery = "INSERT INTO PUNTEGGIO (UTENTE, GRADO, TITOLO) VALUES (?, ?, ?)";

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS )) {
                insertStmt.setInt(1, UserDBM.getUserID(p.getUser().getUsername(), false));
                insertStmt.setInt(2, p.getGrado().ordinal());
                insertStmt.setString(3, p.getTitolo());
                if (insertStmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            for(Integer value : p.getPunteggi()) {PuntoDBM.insertPunto(id, value);}
                            return id;
                        }
                    }
                }
            } catch (SQLException e) {e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return null;
    }
    /**Recupera un <b>Punteggio</b> ed i punti ad esso associati dal database utilizzando l'ID.
     * @param identifier l'ID del <b>Punteggio</b> da recuperare
     * @return - un oggetto <b>Punteggio</b> associato al proprio ID o <br> -<code>null</code> se non trovato
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Punteggio getPunteggioById(int identifier) throws ConnectionException {
        if(connection != null){
            String query = "SELECT * FROM PUNTEGGIO WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, identifier);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new Punteggio(rs.getInt("ID"), Grado.getGradoByOrdinal(rs.getInt("GRADO")), UserDBM.getUserByID(rs.getInt("UTENTE"), false), rs.getString("TITOLO"), PuntoDBM.getPuntiByIdPunteggio(identifier));
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return null;
    }
    /**Recupera una lista di <b>Punteggio</b> ed i punti ad essi associati dal database utilizzando il <b>titolo</b>.
     * @param titolo il <b>titolo</b> dei punteggi da recuperare
     * @return - una lista di oggetti <b>Punteggio</b> associati al titolo o <br>- una lista di <b>Punteggio</b> vuota se non trovati
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static ArrayList<Punteggio> getPunteggiByTitolo(String titolo) throws ConnectionException {
        ArrayList<Punteggio> punteggi = new ArrayList<>();
        if (connection != null) {
            String query = "SELECT * FROM PUNTEGGIO WHERE TITOLO = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, titolo);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("ID");
                    Punteggio punteggio = new Punteggio(
                            id,
                            Grado.getGradoByOrdinal(rs.getInt("GRADO")),
                            UserDBM.getUserByID(rs.getInt("UTENTE"), false),
                            rs.getString("TITOLO"),
                            PuntoDBM.getPuntiByIdPunteggio(id)
                    );
                    punteggi.add(punteggio);
                }
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return punteggi;
    }
    /**Recupera una lista di <b>Punteggio</b> ed i punti ad essi associati dal database utilizzando il <b>titolo</b> e lo <b>User</b>.
     * @param titolo il <b>titolo</b> dei punteggi da recuperare
     * @param user l'oggetto <b>User</b> associato ai punteggi da recuperare
     * @return - una lista di oggetti <b>Punteggio</b> associati al titolo e all'utente o <br>- una lista di <b>Punteggio</b> vuota se non trovati
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static ArrayList<Punteggio> getPunteggiByTitoloAndUser(String titolo, User user) throws ConnectionException {
        ArrayList<Punteggio> punteggi = new ArrayList<>();
        if (connection != null) {
            String query = "SELECT * FROM PUNTEGGIO WHERE TITOLO = ? AND UTENTE = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, titolo);
                stmt.setInt(2, UserDBM.getUserID(user.getUsername(), false));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("ID");
                    Punteggio punteggio = new Punteggio(
                            id,
                            Grado.getGradoByOrdinal(rs.getInt("GRADO")),
                            UserDBM.getUserByID(rs.getInt("UTENTE"), false),
                            rs.getString("TITOLO"),
                            PuntoDBM.getPuntiByIdPunteggio(id)
                    );
                    punteggi.add(punteggio);
                }
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return punteggi;
    }
    /**Recupera una lista di <b>Punteggio</b> ed i punti ad essi associati dal database utilizzando il <b>titolo</b>, lo <b>User</b> e il <b>Grado</b>.
     * @param titolo il <b>titolo</b> dei punteggi da recuperare
     * @param user l'oggetto <b>User</b> associato ai punteggi da recuperare
     * @param grado il <b>Grado</b> dei punteggi da recuperare
     * @return - una lista di oggetti <b>Punteggio</b> associati al titolo, all'utente e al grado o <br>- una lista di <b>Punteggio</b> vuota se non trovati
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static ArrayList<Punteggio> getPunteggiByTitoloUserGrado(String titolo, User user, Grado grado) throws ConnectionException {
        ArrayList<Punteggio> punteggi = new ArrayList<>();
        Integer idUser = UserDBM.getUserID(user.getUsername(), false);
        if (connection != null && idUser != null) {
            String query = "SELECT * FROM PUNTEGGIO WHERE TITOLO = ? AND UTENTE = ? AND GRADO = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, titolo);
                stmt.setInt(2, idUser);
                stmt.setInt(3, grado.ordinal());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    punteggi.add(new Punteggio(rs.getInt("ID"), grado, user, titolo, PuntoDBM.getPuntiByIdPunteggio(rs.getInt("ID"))));
                }
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return punteggi;
    }
    /**Recupera una lista di <b>Punteggio</b> ed i punti ad essi associati dal database utilizzando lo <b>User</b> e il <b>Grado</b>.
     * @param user l'oggetto <b>User</b> associato ai punteggi da recuperare
     * @param grado il <b>Grado</b> dei punteggi da recuperare
     * @return - una lista di oggetti <b>Punteggio</b> associati all'utente e al grado o <br>- una lista di <b>Punteggio</b> vuota se non trovati
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static ArrayList<Punteggio> getPunteggiByUserGrado(User user, Grado grado) throws ConnectionException {
        ArrayList<Punteggio> punteggi = new ArrayList<>();
        Integer idUser = UserDBM.getUserID(user.getUsername(), false);
        if (connection != null && idUser != null) {
            String query = "SELECT * FROM PUNTEGGIO WHERE UTENTE = ? AND GRADO = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, idUser);
                stmt.setInt(2, grado.ordinal());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    punteggi.add(new Punteggio(rs.getInt("ID"), grado, user, rs.getString("TITOLO"), PuntoDBM.getPuntiByIdPunteggio(rs.getInt("ID"))));
                }
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return punteggi;
    }
}