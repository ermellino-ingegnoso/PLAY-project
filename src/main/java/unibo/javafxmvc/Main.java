package unibo.javafxmvc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unibo.javafxmvc.model.*;
import unibo.javafxmvc.controller.AppController;
import java.sql.ResultSet;


public class Main extends Application {
    private static Scene currentScene;
    public static Stage thisStage;
    public static User currentUser;
    private static String dbURL = "jdbc:h2:~/playproj/playprojDB";

    @Override
    public void start(Stage primaryStage) throws Exception {
        thisStage = primaryStage;
        thisStage.setOnCloseRequest(event -> AppController.handleWindowClose());
        changeScene("View/Accesso.fxml");
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
                System.out.println("File FXML non trovato!");
                return;
            }
            currentScene = new Scene((new FXMLLoader()).load(fxmlStream));
            thisStage.setScene(currentScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {


        /* ⬆️ codice procedurale ⬆️ */
        launch(args);
    }
}