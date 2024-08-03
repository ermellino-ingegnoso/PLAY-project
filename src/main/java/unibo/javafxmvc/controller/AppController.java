package unibo.javafxmvc.controller;

import javafx.application.Platform;
import unibo.javafxmvc.DAO.DatabaseManager;

import java.sql.SQLException;

public class AppController {
    public static void handleWindowClose() {
        try {
            DatabaseManager.closeConnection();
            System.out.println("Siummico chiuso");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.exit();
    }
}
