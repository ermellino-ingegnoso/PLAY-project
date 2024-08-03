package unibo.javafxmvc;

import java.io.InputStream;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import unibo.javafxmvc.DAO.DatabaseManager;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.*;
import unibo.javafxmvc.controller.AppController;

public class Main extends Application {
    private static Scene currentScene;
    public static Stage thisStage;
    public static User currentUser;
    private static String dbURL;
    private static String fxmlPath;
    private static String windowTitle;
    private static Boolean fullScreen;
    private static Boolean maximized;

    static{
        dbURL = "jdbc:h2:~/playproj/playprojDB";
        windowTitle = "Applicazione PLAY";
        fullScreen = false;
        maximized = false;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        thisStage = primaryStage;
        thisStage.setTitle(windowTitle);
        thisStage.setOnCloseRequest(event -> AppController.handleWindowClose());
        thisStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {maximized = isNowMaximized;});
        thisStage.show();
        updateWindowBounds();
        changeScene(fxmlPath);
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
            thisStage.setMaximized(maximized);
            thisStage.setScene(currentScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public static void updateWindowBounds(){
        ObservableList<Screen> screens = Screen.getScreensForRectangle(new Rectangle2D(thisStage.getX(), thisStage.getY(), thisStage.getWidth(), thisStage.getHeight()));
        Rectangle2D bounds = screens.get(0).getVisualBounds();
        thisStage.setX(bounds.getMinX());
        thisStage.setY(bounds.getMinY());
        thisStage.setWidth((bounds.getWidth()/3)*2);
        thisStage.setHeight((bounds.getHeight()/3)*2);
        thisStage.centerOnScreen();
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