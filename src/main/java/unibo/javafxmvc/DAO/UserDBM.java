package unibo.javafxmvc.DAO;

import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDBM extends DatabaseManager {
    /**@return true se la connessione è stata stabilita correttamente e l'inserimento è andato a termine; <br> false se l'inserimento non è andato a termine correttamente
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Boolean insertUser(User usr, Boolean admin) throws ConnectionException{
        return insertUser(usr, (admin ? "ADMIN" : "\"User\""));
    }
    public static Boolean updateUserById(User user, Integer userId, boolean admin) throws ConnectionException {
        if (connection != null) {
            String sql = "UPDATE " + (admin ? "Admin" : "\"User\"") + " SET NOME = ?, COGNOME = ?, USERNAME = ?, PASSWORD = ?, AVATAR = ?, COLOR = ? WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, user.getNome());
                pstmt.setString(2, user.getCognome());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setBytes(5, convertImageToByteArray(user.getAvatar()));
                pstmt.setString(6, user.getColor());
                pstmt.setInt(7, userId);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
    }
    public static Boolean insertUser(User usr, String table) throws ConnectionException{
        if(connection != null){
            int affectedRows = 0;
            try(PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO "+ table +" (NOME, COGNOME, USERNAME, PASSWORD, AVATAR, COLOR) VALUES (?, ?, ?, ?, ?, ?)")){
                pstmt.setString(1, usr.getNome());
                pstmt.setString(2, usr.getCognome());
                pstmt.setString(3, usr.getUsername());
                pstmt.setString(4, usr.getPassword());
                pstmt.setBytes(5, convertImageToByteArray(usr.getAvatar()));
                pstmt.setString(6, usr.getColor());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                return false;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@param username username dell'utente da verificare
     * @return true se l'utente con lo username specificato è presente nel database; <br> false se l'utente non è presente
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query di recupero
     * */
    public static Boolean userExists(String username, Boolean admin) throws ConnectionException, SQLException {
        return userExists(username, (admin ? "Admin" : "\"User\""));
    }
    public static Boolean userExists(String username, String table) throws ConnectionException, SQLException {
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT USERNAME FROM "+ table +" WHERE LOWER(USERNAME) = LOWER(?)")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                throw e;
            }
        } else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return true se la password dell'utente corrisponde a quella memorizzata nel database; <br> false non corrisponde
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * @throws SQLException se si verifica un errore durante l'esecuzione della query di recupero o l'Utente non è stato trovato o il dato è corrotto
     * */
    public static Boolean checkPasswordForUser(String username, String password, Boolean admin) throws ConnectionException, SQLException {
        return checkPasswordForUser(username, password, (admin ? "Admin" : "\"User\""));
    }
    public static Boolean checkPasswordForUser(String username, String password, String table) throws ConnectionException, SQLException {
        if(connection != null){
            try(PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT PASSWORD FROM "+ table +" WHERE LOWER(USERNAME) = LOWER(?)")) {
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
    public static User getUser(String username, Boolean admin) throws ConnectionException, SQLException {
        return getUser(username, (admin ? "Admin" : "\"User\""));
    }
    public static User getUser(String username, String table) throws ConnectionException, SQLException {
        if(connection != null) {
            try (PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT NOME, COGNOME, USERNAME, PASSWORD, AVATAR, COLOR FROM "+ table +" WHERE LOWER(USERNAME) = LOWER(?)")) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("username"),
                            rs.getString("password"),
                            convertByteArrayToImage(rs.getBytes("avatar")),
                            rs.getString("color")
                    );
                } else return null;
            } catch (SQLException e) {
                throw e;
            }
        }
        else throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
    }
    /**@return l'ID dell'utente con lo username specificato; <br>null se l'utente non è stato trovato
     * @param username username dell'utente da cercare
     * @throws ConnectionException se la connessione non è stata stabilita correttamente
     * */
    public static Integer getUserID(String username, Boolean admin) throws ConnectionException{
        return getUserID(username, (admin ? "ADMIN" : "\"User\""));
    }
    public static Integer getUserID(String username, String table) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT ID FROM "+ table +" WHERE USERNAME = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            } catch (SQLException e) {
                System.out.println("Tabella: " + table);
                return null;
            }
        } else {
            throw new ConnectionException(connectionExceptionMessage, new NullPointerException(npeMessage));
        }
        return null;
    }
    public static User getUserByID(Integer userID, Boolean admin) throws ConnectionException {
        return getUserByID(userID, (admin ? "ADMIN" : "\"User\""));
    }
    public static User getUserByID(Integer userID, String table) throws ConnectionException {
        if (connection != null) {
            String sql = "SELECT NOME, COGNOME, USERNAME, PASSWORD, AVATAR, COLOR FROM "+ table +" WHERE ID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, userID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getString("NOME"),
                            rs.getString("COGNOME"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD"),
                            convertByteArrayToImage(rs.getBytes("AVATAR")),
                            rs.getString("COLOR")
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
    public static List<User> getAllUsersOrderedByUsername() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM \"User\" ORDER BY username";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("NOME"),
                        resultSet.getString("COGNOME"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        convertByteArrayToImage(resultSet.getBytes("AVATAR")),
                        resultSet.getString("COLOR")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
