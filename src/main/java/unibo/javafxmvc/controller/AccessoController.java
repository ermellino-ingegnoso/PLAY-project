package unibo.javafxmvc.controller;

import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.User;
import unibo.javafxmvc.model.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AccessoController {
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private Alert alert = new Alert(AlertType.INFORMATION);

    @FXML
    private void AccediOnMousePressed(MouseEvent event) {
        accesso();
    }

    @FXML
    void AccediOnKeyPressed(KeyEvent event) {
        if (keyEnterPressed(event))
            accesso();
    }

    @FXML
    private void newUserBtnOnKeyPressed(KeyEvent event) {
        if (keyEnterPressed(event))
            openAddUser();
    }

    @FXML
    private void newUserBtnOnMouseClicked(MouseEvent event) {
        openAddUser();
    }

    @FXML
    private void openAddUser() {
        Main.changeScene("View/AggiungiUtente.fxml");
    }

    @FXML
    private void accesso() {
        User user = UserManager.findUserByUsername(tfUsername.getText());
        if (user != null) {
            lblUsername.setVisible(false);
            if (user.checkPassword(UserManager.getSHA256Hash(tfPassword.getText().trim()))) {
                lblPassword.setVisible(false);
                Main.currentUser = user;
                alert.setTitle("Accesso effettuato");
                alert.setHeaderText("Benvenuto " + user.getNome());
                alert.show();
                // Main.changeScene("Views/Home.fxml");
            } else {
                lblPassword.setVisible(true);
            }
        } else {
            lblUsername.setVisible(true);
        }
    }

    @FXML
    private Boolean keyEnterPressed(KeyEvent event) {
        return (event.getCode() == KeyCode.ENTER);
    }
}

