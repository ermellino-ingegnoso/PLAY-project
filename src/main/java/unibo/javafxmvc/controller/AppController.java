package unibo.javafxmvc.controller;

import javafx.application.Platform;

import java.sql.Connection;
import java.sql.SQLException;

public class AppController {
    public static Connection dbConnection;

    public static void setDbConnection(Connection dbConnection) {
        AppController.dbConnection = dbConnection;
    }

    public static void handleWindowClose() {
        // Chiudi la connessione al database
        if (dbConnection != null) {
            try {
                dbConnection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Termina l'applicazione
        Platform.exit();
    }
}
