package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.EsercizioGenerico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EsercizioGenericoDBM extends DatabaseManager {
    /** Inserisce un esercizio generico nel database ricavando i valori associati dall'esercizio in input: <strong>si presuppone quindi la preventiva esistenza di</strong> <b>User</b> e <b>Regola</b> <strong>nel database</strong>
     * @return <ul><li>l'id dell'esercizio generico inserito se l'inserimento è andato a buon fine;</li> <li><code>null</code> se l'inserimento non è andato a buon fine</li></ul>
     * @param esercizio esercizio generico da inserire
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Integer insertEsercizioGenerico(EsercizioGenerico esercizio) throws ConnectionException {
        if (connection != null) {
            Integer creatoreID, regolaID;
            creatoreID = UserDBM.getUserID(esercizio.getCreatore().getUsername(), true);
            regolaID = RegolaGenericaDBM.getRegolaGenericaId(esercizio.getRegola().getTitolo());
            if (creatoreID == null || regolaID == null) return null;

            String sql = "INSERT INTO ESERCIZIO_GENERICO (CREATORE_ID, REGOLA_ID) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, creatoreID);
                pstmt.setInt(2, regolaID);
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
                System.out.println("Errore nell'inserimento dell'esercizio generico");
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    /**Si presume che l'esercizio generico sia associato unicamente ad una ed una sola <b>Regola</b>
     * @return <ul><li>l'ID dell'esercizio associato;</li> <li><code>null</code> se l'esecizio non è stata trovato o <code>titoloRegola</code> non è associato a nessuna Regola presente nel database</li></ul>
     * @param titoloRegola titolo della regola associata all'esercizio da cercare
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @see RegolaGenericaDBM#getRegolaGenericaId(String)
     * */
    public static Integer getEsercizioGenericoID(String titoloRegola) throws ConnectionException {
        if (connection != null) {
            Integer regolaID = RegolaGenericaDBM.getRegolaGenericaId(titoloRegola);
            String sql = "SELECT ID FROM ESERCIZIO_GENERICO WHERE REGOLA_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, regolaID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            } catch (SQLException | NullPointerException e) {
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
        return null;
    }
    /**Imposta una query per selezionare tutti i campi dell'esercizio generico con l'<code>id</code> specificato
     * @return <ul><li>l'<b>EsercizioGenerico</b> con l'<code>id</code> specificato;</li> <li><code>null</code> se l'esercizio non è stato trovato</li></ul>
     * @param id id dell'esercizio generico da cercare
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static EsercizioGenerico getEsercizioGenericoById(int id) throws ConnectionException {
        if (connection != null) {
            try {
                String query = "SELECT * FROM ESERCIZIO_GENERICO WHERE ID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return  new EsercizioGenerico(
                            rs.getInt("ID"),
                            UserDBM.getUserByID(rs.getInt("CREATORE_ID"), true),
                            RegolaGenericaDBM.getRegolaGenericaByID(rs.getInt("REGOLA_ID")),
                            BloccoGenericoDBM.getBlocchiGenericiForEsercizioGenerico(rs.getInt("ID")));
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ConnectionException("Errore nella connessione al database", e);
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
}
