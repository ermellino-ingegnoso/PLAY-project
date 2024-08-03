package unibo.javafxmvc;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unibo.javafxmvc.DAO.DatabaseManager;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.*;
import unibo.javafxmvc.controller.AppController;

public class Main extends Application {
    private static Scene currentScene;
    public static Stage thisStage;
    public static User currentUser;
    private static String dbURL = "jdbc:h2:~/playproj/playprojDB";
    private static String fxmlPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        thisStage = primaryStage;
        thisStage.setOnCloseRequest(event -> AppController.handleWindowClose());
        changeScene(fxmlPath);
        thisStage.show();
    }

    public void setFullScreen() {
        thisStage.setFullScreen(true);
    }

    public void delFullScreen() {
        thisStage.setFullScreen(false);
    }

    public static void changeScene(String fxmlPath) {
        try {
            InputStream fxmlStream = Main.class.getResourceAsStream(fxmlPath);
            if (fxmlStream == null) {
                System.out.println("File FXML non trovato: " + fxmlPath);
                return;
            }
            currentScene = new Scene((new FXMLLoader()).load(fxmlStream));
            thisStage.setScene(currentScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try{
            DatabaseManager.inizialize(dbURL);
            fxmlPath = "View/Accesso.fxml";
        } catch (ConnectionException e) {
            fxmlPath = "View/ErroreDatabase.fxml";
        }
        launch(args);
    }
}