package unibo.javafxmvc.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Punto;

public class PuntoDBM extends DatabaseManager {
    /**Inserisce un <b>Punto</b> nel database associato a un punteggio specifico.
     * @param idPunteggio l'ID del punteggio a cui il punto è associato
     * @param value il valore del punto da inserire
     * @return l'ID del punto inserito o <br> <code>null</code> se l'inserimento fallisce
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Integer insertPunto(int idPunteggio, Integer value) throws ConnectionException {
        if (connection != null) {
            String insertQuery = "INSERT INTO PUNTO (VALORE, PUNTEGGIO_FK) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, value);
                insertStmt.setInt(2, idPunteggio);
                if (insertStmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) return generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {return null;}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return null;
    }
    /**Recupera il <b>valore</b> utilizzando l'ID del <b>Punto</b>.
     * @param id l'ID del punto da recuperare
     * @return - il valore del punto o <br>- <code>null</code> se non trovato
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static Integer getPuntoByID(int id) throws ConnectionException {
        if (connection != null) {
            String query = "SELECT VALORE FROM PUNTO WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt("VALORE");
                return null;
            } catch (SQLException e) {return null;}
        } else { throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));}
    }
    /**Recupera tutti i valori dei punti associati a un <b>Punteggio</b> specifico.
     * @param idPunteggio l'ID del <b>Punteggio</b> a cui i punti sono associati
     * @return - una <code>ArrayList</code> di <b>Punto</b> o <br> - <code>null</code> se si verifica un errore
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     */
    public static ArrayList<Integer> getPuntiByIdPunteggio(int idPunteggio) throws ConnectionException {
        if (connection != null) {
            String query = "SELECT VALORE FROM PUNTO WHERE PUNTEGGIO_FK = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, idPunteggio);
                ResultSet rs = stmt.executeQuery();
                ArrayList<Integer> punti = new ArrayList<>();
                while (rs.next()) { punti.add(rs.getInt("VALORE"));}
                return punti;
            } catch (SQLException e) {return null;}
        } else { throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));}
    }
}