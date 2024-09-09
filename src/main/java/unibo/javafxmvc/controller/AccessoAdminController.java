package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;

import java.sql.SQLException;

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
    void AccediOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) accesso();
    }
    @FXML
    void AccediOnMousePressed(MouseEvent event) {
        accesso();
    }
    @FXML
    void BackOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/Accesso.fxml");
    }
    @FXML
    void BackOnMousePressed(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }
    @FXML
    void newUserBtnOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Main.changeScene("View/RegistrazioneAdmin.fxml");
    }
    @FXML
    void newUserBtnOnMouseClicked(MouseEvent event) {
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
}
