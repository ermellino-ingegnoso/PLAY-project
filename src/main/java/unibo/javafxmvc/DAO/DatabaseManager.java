package unibo.javafxmvc.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection connection;
    private static Statement statement;

    public static void inizialize(String dbURL) {
        try (Connection cn = DriverManager.getConnection(dbURL);
             Statement stm = connection.createStatement()) {
            connection = cn;
            statement = stm;
            String insertData = "INSERT INTO test(id, name) VALUES(1, 'Alice')";
            statement.execute(insertData);
            // Leggere dati
            String query = "SELECT * FROM test";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
