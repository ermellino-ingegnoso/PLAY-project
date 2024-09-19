package unibo.javafxmvc.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class AccessoAdminController {
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private Pane fallingDigitsPane;

    private static final int QUANTI = 4;
    private static final int FALL_SPEED = 5;

    public void initialize() {
        Platform.runLater(() -> tfUsername.requestFocus());
        startFallingDigitsAnimation();
    }
    @FXML
    private void AccediOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) accesso();
    }
    @FXML
    private void AccediOnMousePressed(MouseEvent event) {
        accesso();
    }
    @FXML
    private void BackOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/Accesso.fxml");
    }
    @FXML
    private void BackOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }
    @FXML
    private void newUserBtnOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/RegistrazioneAdmin.fxml");
    }
    @FXML
    private void newUserBtnOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/RegistrazioneAdmin.fxml");
    }
    @FXML
    private void accesso() {
        String userName = tfUsername.getText().trim();
        try{
            User usr = UserDBM.getUser(userName, true);
            if(usr != null){
                lblUsername.setVisible(false);
                if(usr.checkPassword(User.getSHA256Hash(tfPassword.getText()))){
                    lblPassword.setVisible(false);
                    Main.currentUser = usr;
                    Main.admin = false;
                    Main.changeScene("View/AdminHome.fxml");
                } else{
                    lblPassword.setVisible(true);
                }
            } else{
                lblUsername.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            Main.changeScene("View/ErroreDatabase.fxml");
        }
    }
    private void startFallingDigitsAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> addFallingDigit()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void addFallingDigit() {
        try{
            Random random = new Random();
            for(int i=0; i<QUANTI; i++){
                Text digit = new Text(random.nextBoolean() ? "0" : "1");
                digit.getStyleClass().add("falling-digit");
                digit.setX(random.nextInt((int)mainGridPane.getWidth()));
                digit.setY(0);

                fallingDigitsPane.getChildren().add(digit);

                Timeline fallTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
                    digit.setY(digit.getY() + FALL_SPEED);
                    double opacity = 1.0 - (digit.getY() / fallingDigitsPane.getHeight()*0.9);
                    digit.setOpacity(opacity);
                    if (digit.getY() > mainGridPane.getHeight() || opacity <= 0) {
                        fallingDigitsPane.getChildren().remove(digit);
                    }
                }));
                fallTimeline.setCycleCount(Timeline.INDEFINITE);
                fallTimeline.play();
            }
        } catch (Exception e) {}
    }
}
