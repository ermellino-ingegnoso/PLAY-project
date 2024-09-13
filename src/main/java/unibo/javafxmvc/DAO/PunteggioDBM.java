package unibo.javafxmvc.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Punteggio;

public class PunteggioDBM extends DatabaseManager{
    /**Inserisce un nuovo <b>Punteggio</b> ed i punti ad esso asccociati nel database.
     * <p>Il vincolo unique legato al <b>titolo</b> è gestito internamente</p>
     * @param p l'oggetto <b>Punteggio</b> da inserire
     * @return - l'ID del <b>Punteggio</b> inserito o <br> -<code>null</code> se l'inserimento fallisce a causa della violazione del vincolo unique sul <b>titolo</b>
     * @throws SQLException se si verifica un errore SQL non legato alla violazione di un vincolo unique
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Integer insertPunteggio(Punteggio p) throws SQLException, ConnectionException {
        if (connection != null) {
            String insertQuery = "INSERT INTO PUNTEGGIO (UTENTE, GRADO, TITOLO) VALUES (?, ?, ?)";

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS )) {
                insertStmt.setInt(1, UserDBM.getUserID(p.getUser().getUsername(), false));
                insertStmt.setInt(2, p.getGrado().ordinal());
                insertStmt.setString(3, p.getTitolo());
                if (insertStmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) return generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) System.err.println("PunteggioDBM#insertPunteggio: Titolo già esistente");
                else throw e;
            }
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
    /**Recupera un <b>Punteggio</b> ed i punti ad esso associati dal database utilizzando il titolo.
     * @param titolo il <b>titolo</b> del <b>Punteggio</b> da recuperare
     * @return - un oggetto <b>Punteggio</b> associato al proprio ID o <br>-<code>null</code> se non trovato
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Punteggio getPunteggioByTitolo(String titolo) throws ConnectionException {
        if(connection != null){
            String query = "SELECT * FROM PUNTEGGIO WHERE TITOLO = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, titolo);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Integer id = rs.getInt("ID");
                    return new Punteggio(id, Grado.getGradoByOrdinal(rs.getInt("GRADO")), UserDBM.getUserByID(rs.getInt("UTENTE"), false), rs.getString("TITOLO"), PuntoDBM.getPuntiByIdPunteggio(id));
                }
            } catch (SQLException e) { e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return null;
    }
}