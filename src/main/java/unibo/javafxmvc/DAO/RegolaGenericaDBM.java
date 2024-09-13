package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.Grado;
import unibo.javafxmvc.model.Regola;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegolaGenericaDBM extends DatabaseManager{
    /**@return true se la regola è stata inserita correttamente; <br>false se il titolo della regola è già presente nel database
     * */
    public static Boolean insertRegola(Regola regola) throws ConnectionException {
        if(connection != null){
            if(getRegolaGenericaId(regola.getTitolo()) > 0) return false;   //  verifica che non ci siano regole con lo stesso Titolo
            try(PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO REGOLA_GENERICA (TITOLO, DOMANDA, DESCRIZIONE, GRADO) VALUES (?, ?, ?, ?)")){
                pstmt.setString(1, regola.getTitolo());
                pstmt.setString(2, regola.getDomanda());
                pstmt.setString(3, regola.getDescrizione());
                pstmt.setString(4, regola.getGradoName());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println("Errore nell'inserimento della regola");
                return false;
            } catch(NullPointerException e){
                e.printStackTrace();
                return false;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return l'ID della regola con il titolo specificato; <br>null se la regola non è stata trovata
     * @param regola titolo della regola da cercare
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Integer getRegolaGenericaId(String regola) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT ID FROM REGOLA_GENERICA WHERE TITOLO = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, regola);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
        return 0;
    }
    /** esegue un <code>SELECT</code> sul database per la ricerca
     * @return la regola con l'<b>ID</b> specificato o<br><code>null</code> se la regola non è stata trovata o viene sollevata un'eccezione SQL
     * @param regolaID <b>ID</b> della regola da cercare
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Regola getRegolaGenericaByID(Integer regolaID) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT TITOLO, DOMANDA, DESCRIZIONE, GRADO FROM REGOLA_GENERICA WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, regolaID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Regola(
                            rs.getString("TITOLO"),
                            rs.getString("DOMANDA"),
                            rs.getString("DESCRIZIONE"),
                            Grado.valueOf(rs.getString("GRADO"))
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
    /** Verifica se una regola esiste nel database
     * @param title titolo della regola da verificare
     * @return <b>true</b> se la regola esiste, <br><b>false</b> altrimenti
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @see RegolaGenericaDBM#getRegolaGenericaId(String)
     */
    public static boolean regolaExists(String title) throws ConnectionException {
        return getRegolaGenericaId(title) > 0;
    }
}
