package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EsercizioEspertoDBM extends DatabaseManager{
    /** Inserisce un nuovo esercizio esperto nel database
     * @param esercizio <b>EsercizioEsperto</b> da inserire
     * @return <b>Integer</b> id dell'esercizio esperto inserito, <b>null</b> se l'inserimento non è andato a buon fine
     * */
    public static Integer insertEsercizioEsperto(EsercizioEsperto esercizio) throws ConnectionException {
        if (connection != null) {
            Integer esercizioGenericoID, utenteID;
            esercizioGenericoID = EsercizioGenericoDBM.getEsercizioGenericoID(esercizio.getRegola().getTitolo());
            utenteID = UserDBM.getUserID(esercizio.getUtente().getUsername(), false);
            String sql = "INSERT INTO ESERCIZIO_ESPERTO (ESERCIZIO_GENERICO_ID, UTENTE_ID) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, esercizioGenericoID);
                pstmt.setInt(2, utenteID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Integer idEE = generatedKeys.getInt(1);
                            esercizio.setId(idEE);
                            ArrayList<BloccoEsperto> blocchiEsperto = new ArrayList<BloccoEsperto>();
                            for(BloccoGenerico bg : esercizio.getBlocchi()){
                                BloccoEsperto be = new BloccoEsperto(bg, false);
                                be.setId(BloccoEspertoDBM.insertBloccoEsperto(be, idEE));
                                blocchiEsperto.add(be);
                            }
                            esercizio.setBlocchiEsperto(blocchiEsperto);
                            return idEE;
                        }
                    }
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore nell'inserimento dell'esercizio esperto");
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    /** Restituisce un esercizio esperto incompleto per l'utente se esiste, altrimenti ne crea uno nuovo. Gestione interna degli errori SQL
     * @param user - <b>User</b> per il quale cercare un esercizio esperto incompleto
     * @param grado - <b>Grado</b> della <b>Regola</b> associata all'esercizio da cercare
     * @return - <b>EsercizioEsperto</b> attualmente incompleto,
     * <br> - nuovo <b>EsercizioEsperto</b> se non ne esiste uno incompleto per  l'utente,
     * <br> - <b>null</b> se non è possibile creare un nuovo EsercizioEsperto per l'utente
     * */
    public static EsercizioEsperto getOrCreateEsercizioEspertoForUser(User user, Grado grado) throws ConnectionException {
        if (connection != null) {
            try {
                Integer userID = UserDBM.getUserID(user.getUsername(), false);
                if (userID == null) return null;
                try (PreparedStatement ps = connection.prepareStatement(
                "SELECT ee.ID FROM ESERCIZIO_ESPERTO ee " +
                    "JOIN BLOCCO_ESPERTO be ON ee.ID = be.ESERCIZIO_ESPERTO_ID " +
                    "JOIN ESERCIZIO_GENERICO eg ON ee.ESERCIZIO_GENERICO_ID = eg.ID " +
                    "JOIN REGOLA_GENERICA r ON eg.REGOLA_ID = r.ID " +
                    "WHERE ee.UTENTE_ID = ? AND be.SUPERATO = FALSE AND r.GRADO = ?"
                )) {
                    ps.setInt(1, userID);
                    ps.setString(2, grado.toString());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int esercizioEspertoId = rs.getInt("ID");
                            return getEsercizioEspertoById(esercizioEspertoId);
                        }
                    }
                }
                EsercizioGenerico eg = trovaNuovoEsercizioGenerico(userID, grado);
                if (eg == null) return null;
                EsercizioEsperto newEE = new EsercizioEsperto(null, eg, user);  // I blocchiEsperto vengono inseriti autonomamente
                newEE.setId(insertEsercizioEsperto(newEE));  //inserisce il nuovo esercizio esperto nel database
                if(newEE.getId() == null) return null;
                return newEE;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
        return null;
    }
    private static EsercizioGenerico trovaNuovoEsercizioGenerico(Integer userID, Grado grado) throws ConnectionException {
        if (connection != null) {
            try (PreparedStatement ps = connection.prepareStatement(
            "SELECT eg.ID FROM ESERCIZIO_GENERICO eg " +
                "LEFT JOIN ESERCIZIO_ESPERTO ee ON eg.ID = ee.ESERCIZIO_GENERICO_ID " +
                "JOIN REGOLA_GENERICA rg ON eg.REGOLA_ID = rg.ID " +
                "WHERE (ee.UTENTE_ID IS NULL OR ee.UTENTE_ID != ?) AND rg.GRADO = ?"
            )) {
                ps.setInt(1, userID);
                ps.setString(2, grado.name());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return EsercizioGenericoDBM.getEsercizioGenericoById(rs.getInt("ID"));
                    }
                    System.out.println("EsercizioEspretoDBM> Nessun esercizio generico disponibile per l'utente con id: " + userID);
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
    /**Inserisce i blocchi autonomamente
     * */
    private static EsercizioEsperto getEsercizioEspertoById(int id) throws ConnectionException {
        if (connection != null) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM ESERCIZIO_ESPERTO WHERE ID = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ArrayList<BloccoEsperto> blocchiEsperto = new ArrayList<BloccoEsperto>();
                        EsercizioGenerico eg = EsercizioGenericoDBM.getEsercizioGenericoById(rs.getInt("ESERCIZIO_GENERICO_ID"));
                        //  Inserimento dei blocchi esperto
                        for(BloccoGenerico bg : eg.getBlocchi()){   // Null Pointer Exception
                            blocchiEsperto.add(BloccoEspertoDBM.getBloccoEspertoByBloccoGenericoAndEsercizioEsperto(bg.getId(), id));
                        }
                        return new EsercizioEsperto(id, eg, blocchiEsperto, UserDBM.getUserByID(rs.getInt("UTENTE_ID"), false));
                    }
                }
            } catch (SQLException e) { e.printStackTrace();
            } catch (NullPointerException npe){ npe.printStackTrace();}
        } else {throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));}
        return null;
    }
    /**
     * @param grado Il <b>Grado</b> dell'<b>EsercizioEsperto</b> da recuperare.
     * @param user lo <b>User</b> associato all'<b>EsercizioEsperto</b>.
     * @return - L'oggetto <b>EsercizioEsperto</b> associato allo <b>User</b> specificato con l'ID più alto e il <b>Grado</b> specificato, o <br>
     * -<code>null</code> se non esiste alcun <b>EsercizioEsperto</b> che rispetta le condizioni.
     * @throws ConnectionException Se si verifica un errore di connessione al database.
     */
    public static EsercizioEsperto getEsercizioEspertoWithMaxIdByGradoAndUser(Grado grado, User user) throws ConnectionException {
        if (connection != null) {
            String query = "SELECT ee.* FROM ESERCIZIO_ESPERTO ee " +
                    "JOIN ESERCIZIO_GENERICO eg ON ee.ESERCIZIO_GENERICO_ID = eg.ID " +
                    "JOIN REGOLA_GENERICA rg ON eg.REGOLA_ID = rg.ID " +
                    "WHERE rg.GRADO = ? AND ee.UTENTE_ID = ? ORDER BY ee.ID DESC LIMIT 1";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, grado.toString());
                ps.setInt(2, UserDBM.getUserID(user.getUsername(), false));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("ID");
                        EsercizioGenerico eg = EsercizioGenericoDBM.getEsercizioGenericoById(rs.getInt("ESERCIZIO_GENERICO_ID"));
                        ArrayList<BloccoEsperto> blocchiEsperto = new ArrayList<>();
                        for (BloccoGenerico bg : eg.getBlocchi()) {
                            blocchiEsperto.add(BloccoEspertoDBM.getBloccoEspertoByBloccoGenericoAndEsercizioEsperto(bg.getId(), id));
                        }
                        return new EsercizioEsperto(id, eg, blocchiEsperto, user);
                    }
                }
            } catch (SQLException e) {e.printStackTrace();}
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        return null;
    }
    private static List<BloccoEsperto> getBlocchiEspertoByEsercizioEspertoId(Integer esercizioEspertoId) throws ConnectionException {
        List<BloccoEsperto> blocchiEsperto = new ArrayList<>();
        if (connection != null) {
            try {
                String query = "SELECT * FROM BLOCCO_ESPERTO WHERE ESERCIZIO_ESPERTO_ID = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, esercizioEspertoId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int bloccoGenericoId = rs.getInt("BLOCCO_GENERICO_ID");
                    boolean superato = rs.getBoolean("SUPERATO");
                    BloccoEsperto bloccoEsperto = new BloccoEsperto(BloccoGenericoDBM.getBloccoGenericoByID(bloccoGenericoId), superato);
                    blocchiEsperto.add(bloccoEsperto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ConnectionException("Errore nella connessione al database", e);
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
        return blocchiEsperto;
    }
}
