package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.BloccoEsperto;
import unibo.javafxmvc.model.EsercizioEsperto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BloccoEspertoDBM extends DatabaseManager {
    /**Inserisce un <b>BloccoEspreto</b> nel database prelevando da esso l'<b>id</b> del blocco generico associato e tutti i paramentri necessari al suo inserimento
     * @return - l'<b>id</b> del <b>BloccoEspreto</b> appena inserito, <br> - <b>null</b> se l'inserimento non è riuscito
     * @param blocco - <b>BloccoEspreto</b> da inserire
     * @param eeID - <b>id</b> dell'<b>EsercizioEsperto</b> a cui il blocco è associato
     * */
    public static Integer insertBloccoEsperto(BloccoEsperto blocco,Integer eeID) throws ConnectionException {
        if (connection != null) {
            String sql = "INSERT INTO BLOCCO_ESPERTO (BLOCCO_GENERICO_ID, ESERCIZIO_ESPERTO_ID, SUPERATO, CODICE_UTENTE) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, blocco.getBloccoGenerico().getId());
                pstmt.setInt(2, eeID);
                pstmt.setBoolean(3, blocco.isSuperato());
                pstmt.setString(4, blocco.getCodiceUtente());
                if (pstmt.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
                return null;
            }  catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore nell'inserimento del blocco esperto");
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    /**Si consiglia di non usare questo metodo in quanto particolarmente dispendioso
     * */
    public static BloccoEsperto getBloccoEspertoByID(Integer id) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT * FROM BLOCCO_ESPERTO WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new BloccoEsperto(rs.getInt("ID"), BloccoGenericoDBM.getBloccoGenericoByID(rs.getInt("BLOCCO_GENERICO_ID")), rs.getBoolean("SUPERATO"), rs.getString("CODICE_UTENTE"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore nel recupero del blocco esperto");
            }
            return null;
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    /**Inserisce tutti i paramentri del Blocco Esperto autonomamente (inluso l'<b>id</b>)
     * @param bgID <b>id</b> del <b>BloccoGenerico</b> associato al <b>BloccoEsperto</b>
     * @param eeID <b>id</b> dell'<b>EsercizioEsperto</b> associato al <b>BloccoEsperto</b>
     * */
    public static BloccoEsperto getBloccoEspertoByBloccoGenericoAndEsercizioEsperto(Integer bgID, Integer eeID) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT * FROM BLOCCO_ESPERTO WHERE BLOCCO_GENERICO_ID = ? AND ESERCIZIO_ESPERTO_ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, bgID);
                pstmt.setInt(2, eeID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new BloccoEsperto(rs.getInt("ID"), BloccoGenericoDBM.getBloccoGenericoByID(rs.getInt("BLOCCO_GENERICO_ID")), rs.getBoolean("SUPERATO"), rs.getString("CODICE_UTENTE"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore nel recupero del blocco esperto");
            }
            return null;
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    public static Boolean updateBloccoEsperto(BloccoEsperto blocco) throws ConnectionException {
        if (connection != null) {
            String sql = "UPDATE BLOCCO_ESPERTO SET SUPERATO = ?, CODICE_UTENTE = ? WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setBoolean(1, blocco.isSuperato());
                pstmt.setString(2, blocco.getCodiceUtente());
                pstmt.setInt(3, blocco.getId());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore SQL nell'aggiornamento del blocco esperto");
                return false;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
}
