package unibo.javafxmvc.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AccessoController implements Initializable {
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private Pane fallingDigitsPane;
    @FXML
    private GridPane mainGridPane;

    private static final int QUANTI = 4;
    private static final int FALL_SPEED = 5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> tfUsername.requestFocus());
        startFallingDigitsAnimation();
    }
    @FXML
    private void AccediOnMousePressed(MouseEvent event) {
        accesso();
    }
    @FXML
    void AccediOnKeyPressed(KeyEvent event) {
        if (AuxiliaryController.keyEnterPressed(event))
            accesso();
    }
    @FXML
    private void newUserBtnOnKeyPressed(KeyEvent event) {
        if (AuxiliaryController.keyEnterPressed(event))
            openAddUser();
    }
    @FXML
    private void newUserBtnOnMouseClicked(MouseEvent event) {
        openAddUser();
    }
    @FXML
    private void openAddUser() {
        Main.changeScene("View/Registrazione.fxml");
    }
    @FXML
    private void accesso() {
        String userName = tfUsername.getText().trim();
        try {
            User usr = UserDBM.getUser(userName, false);
            if (usr != null) {
                lblUsername.setVisible(false);
                if (usr.checkPassword(User.getSHA256Hash(tfPassword.getText()))) {
                    lblPassword.setVisible(false);
                    Main.currentUser = usr;
                    Main.admin = false;
                    Main.changeScene("View/UserHome.fxml");
                } else {
                    lblPassword.setVisible(true);
                }
            } else {
                lblUsername.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            Main.changeScene("View/ErroreDatabase.fxml");
        }
    }
    @FXML
    void AccessoAdminOnKeyPressed(KeyEvent event) {
        if (AuxiliaryController.keyEnterPressed(event)) accessoAdmin();
    }
    @FXML
    void AccessoAdminOnMouseClicked(MouseEvent event) {
        accessoAdmin();
    }
    private void accessoAdmin() {
        Main.changeScene("View/AccessoAdmin.fxml");
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