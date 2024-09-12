package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.BloccoGenerico;
import unibo.javafxmvc.model.EsercizioGenerico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BloccoGenericoDBM extends DatabaseManager {
    /**Inserisce un blocco generico nel database e lo associa all'esercizio generico specificato recuperandone l'id
     * @return l'id del blocco generico inserito se l'inserimento è andato a buon fine; <br> null se l'inserimento non è andato a buon fine
     * @param blocco blocco generico da inserire
     * @param esercizioGenerico esercizio generico a cui il blocco generico fa riferimento
     * */
    public static Integer insertBloccoGenerico(BloccoGenerico blocco, EsercizioGenerico esercizioGenerico) throws ConnectionException {
        if (connection != null) {
            Integer esercizioID = EsercizioGenericoDBM.getEsercizioGenericoID(esercizioGenerico.getRegola().getTitolo());
            if (esercizioID == null) return null;

            String sql = "INSERT INTO BLOCCO_GENERICO (ESERCIZIO_GENERICO_ESPERTO_ID, CONSEGNA, CODICE, BLOCCO_CORRETTO) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, esercizioID);
                pstmt.setString(2, blocco.getConsegna());
                pstmt.setString(3, blocco.getCodice());
                pstmt.setString(4, blocco.getMetodo());
                if (pstmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    public static BloccoGenerico getBloccoGenericoByID(Integer bloccoID) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT ID, ESERCIZIO_GENERICO_ESPERTO_ID, CONSEGNA, CODICE, BLOCCO_CORRETTO FROM BLOCCO_GENERICO WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, bloccoID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new BloccoGenerico(
                            rs.getInt("ID"),
                            rs.getString("CONSEGNA"),
                            rs.getString("CODICE"),
                            rs.getString("BLOCCO_CORRETTO")
                    );
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    /**Cerca di recuperare i blocchi generici associati all'esercizio generico specificato da <code>esercizioGenericoID</code> con gestione interna delle eccezioni
     * @param esercizioGenericoID id dell'esercizio generico a cui i blocchi generici sono associati
     * @return <ul><li>una lista di <b>BloccoGenerico</b> associati all'esercizio generico specificato da <code>esercizioGenericoID</code>;</li> <li>una lista vuota di <b>BloccoGenerico</b> se non sono stati trovati blocchi associati all'esercizio generico specificato da <code>esercizioGenericoID</code></li></ul>
     * */
    public static ArrayList<BloccoGenerico> getBlocchiGenericiForEsercizioGenerico(Integer esercizioGenericoID) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT * FROM BLOCCO_GENERICO WHERE ESERCIZIO_GENERICO_ESPERTO_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, esercizioGenericoID);
                ResultSet rs = pstmt.executeQuery();
                ArrayList<BloccoGenerico> blocchi = new ArrayList<>();
                while (rs.next()) {
                    blocchi.add(new BloccoGenerico(
                            rs.getInt("ID"),
                            rs.getString("CONSEGNA"),
                            rs.getString("CODICE"),
                            rs.getString("BLOCCO_CORRETTO")
                    ));
                }
                return blocchi;
            } catch (SQLException e) {
                return new ArrayList<>();
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
}
