package unibo.javafxmvc.controller;

import javafx.application.Platform;
import unibo.javafxmvc.DAO.DatabaseManager;
import unibo.javafxmvc.util.FileUtils;
import unibo.javafxmvc.Main;

import java.nio.file.Path;
import java.sql.SQLException;

public class AppController {
    public static void handleWindowClose() {
        try { // Chiusura della connessione al database: (per evitare corrosioni dei dati)
            DatabaseManager.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }   // Pulizia della cartella temporanea: (per evitare accumulo di file inutili)
        FileUtils.deleteDirectoryContent(Path.of(Main.tempPath));
        Platform.exit();
    }
}
