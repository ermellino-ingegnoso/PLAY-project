package unibo.javafxmvc.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> tfUsername.requestFocus());
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
        try{
            User usr = UserDBM.getUser(userName, false);
            if(usr != null){
                lblUsername.setVisible(false);
                if(usr.checkPassword(User.getSHA256Hash(tfPassword.getText()))){
                    lblPassword.setVisible(false);
                    Main.currentUser = usr;
                    Main.changeScene("View/UserHome.fxml");
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
    @FXML
    void AccessoAdminOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) accessoAdmin();;
    }
    @FXML
    void AccessoAdminOnMouseClicked(MouseEvent event) {
        accessoAdmin();
    }
    private void accessoAdmin(){
        Main.changeScene("View/AccessoAdmin.fxml");
    }
}

