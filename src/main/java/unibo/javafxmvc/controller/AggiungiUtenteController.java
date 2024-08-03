package unibo.javafxmvc.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import unibo.javafxmvc.DAO.DatabaseManager;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;
import unibo.javafxmvc.model.FormValidator;

public class AggiungiUtenteController {
    @FXML
    Alert alert = new Alert(AlertType.INFORMATION);
    @FXML
    private Label lblNome;
    @FXML
    private Label lblCognome;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblRipetiPassword;
    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfCognome;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfRipetiPassword;
    @FXML
    private Label lblRegistrato;

    @FXML
    protected void tfNomeOnKeyTyped(KeyEvent event) {
        if (!FormValidator.validateName(tfNome.getText().trim())) {
            setLblVisibility(lblNome, true);
        } else
            setLblVisibility(lblNome, false);
    }

    @FXML
    protected void tfCognomeOnKeyTyped(KeyEvent event) {
        if (!FormValidator.validateName(tfCognome.getText().trim())) {
            setLblVisibility(lblCognome, true);
        } else
            setLblVisibility(lblCognome, false);
    }

    @FXML
    protected void tfUsernameOnKeyTyped(KeyEvent event) {
        if (!FormValidator.validateUsername(tfUsername.getText().trim())) {
            setLblVisibility(lblUsername, true);
        } else
            setLblVisibility(lblUsername, false);
    }

    @FXML
    protected void tfPasswordOnKeyTyped(KeyEvent event) {
        if (!FormValidator.validatePassword(tfPassword.getText().trim())) {
            setLblVisibility(lblPassword, true);
        } else
            setLblVisibility(lblPassword, false);

    }

    @FXML
    protected void tfRipetiPasswordOnKeyTyped(KeyEvent event) {
        setLblVisibility(lblRipetiPassword, false);
    }

    @FXML
    public void trimLbl(Label... lbls) {
        for (Label lbl : lbls)
            lbl.setText(lbl.getText().trim());
    }

    private void setLblVisibility(Label lbl, Boolean visible) {
        lbl.setVisible(visible);
    }

    @FXML
    protected void btnAddUserOnMouseClicked(MouseEvent event) {
        addUser();
    }

    @FXML
    protected void btnAddUserOnKeyPressed(MouseEvent event) {
        addUser();
    }

    private boolean validatePassword(String password, String ripetiPassword) {
        if (!password.equals(ripetiPassword)) {
            setLblVisibility(lblRipetiPassword, true);
            return false;
        }
        setLblVisibility(lblRipetiPassword, false);
        return true;
    }

    private void addUser() {
        trimLbl(lblNome, lblCognome, lblUsername, lblPassword, lblRipetiPassword);
        if (FormValidator.validateName(tfNome.getText()) && FormValidator.validateName(tfCognome.getText())
                && FormValidator.validateUsername(tfUsername.getText())
                && validatePassword(tfPassword.getText(), tfRipetiPassword.getText())) {
            try {
                Boolean check = DatabaseManager.insertUser(new User(
                        tfNome.getText(),
                        tfCognome.getText(),
                        tfUsername.getText(),
                        User.getSHA256Hash(tfPassword.getText())));
                if (check) {
                    setLblVisibility(lblRegistrato, false);
                    Main.changeScene("View/Accesso.fxml");
                } else
                    setLblVisibility(lblRegistrato, true);
            } catch (ConnectionException e) {
                Main.changeScene("View/ErroreDatabase.fxml");
            } catch (RuntimeException e) {
                alert.setTitle("Errore");
                alert.setHeaderText("Errore durante l'aggiunta dell'utente");
                alert.show();
                e.printStackTrace();
            }
        }
    }
    @FXML
    void IndietroOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }
}