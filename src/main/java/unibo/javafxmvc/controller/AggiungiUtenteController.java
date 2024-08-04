package unibo.javafxmvc.controller;

import java.nio.file.Files;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.DatabaseManager;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;
import unibo.javafxmvc.model.FormValidator;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AggiungiUtenteController implements Initializable {
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
    private ImageView ivAvatar;
    @FXML
    private Label lblAvatar;
    @FXML
    private ColorPicker cpUser;
    @FXML
    private Label lblColorPicker;
    @FXML
    private Label lblConnessione;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivAvatar.setImage(new Image(getClass().getResourceAsStream("/unibo/javafxmvc/Images/avatar.png")));
        Tooltip tooltipImg = new Tooltip("Trascina un'immagine");
        tooltipImg.setShowDelay(Duration.ZERO);
        Tooltip.install(ivAvatar, tooltipImg);
    }
    @FXML
    void handleColorChange(ActionEvent event) {
        lblColorPicker.setTextFill(cpUser.getValue());
    }
    @FXML
    void ivAvatarOnDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            try{
                ivAvatar.setImage(new Image(file.toURI().toString()));
                lblAvatar.setText("Errore nel caricamento dell' immagine");
                lblAvatar.setVisible(false);
                success = true;
            } catch(NullPointerException npe){
                lblAvatar.setText("Errore nel caricamento dell' immagine");
                lblAvatar.setVisible(true);
            } catch(IllegalArgumentException iae){
                lblAvatar.setText("Tipo del file non supportato");
                lblAvatar.setVisible(true);
            }
        }
        event.setDropCompleted(success);
        event.consume();    // l'evento viene gestito e non propagato
    }

    @FXML
    void ivAvatarOnDragOver(DragEvent event) {
        if (event.getGestureSource() != ivAvatar && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
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
        String username = tfUsername.getText().trim().toLowerCase();
        if (!FormValidator.validateUsername(username)) {
            setLblVisibility(lblUsername, true);
        } else {
            setLblVisibility(lblUsername, false);
            try {
                if (DatabaseManager.userExists(username)) {
                    setLblVisibility(lblRegistrato, true);
                } else setLblVisibility(lblRegistrato, false);
            }catch(SQLException sqle){
                sqle.printStackTrace(); //  errore di esecuzione della query
            } catch (ConnectionException ce) {
                lblConnessione.setVisible(true);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
        }
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
    protected void btnAddUserOnKeyPressed(KeyEvent event) {
        if(keyEnterPressed(event))
            addUser();
    }
    @FXML
    private Boolean keyEnterPressed(KeyEvent event) {
        return (event.getCode() == KeyCode.ENTER);
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
                byte[] avatar = null;
                try{    // si cerca di recuperare l'immagine dell'avatar se disponibile
                    avatar = Files.readAllBytes((new File(ivAvatar.getImage().getUrl().replace("file:/", ""))).toPath());
                    lblAvatar.setVisible(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IO");
                } catch (NullPointerException e) {
                    lblAvatar.setVisible(true);
                }
                Boolean check = DatabaseManager.insertUser(new User(
                        tfNome.getText(),
                        tfCognome.getText(),
                        tfUsername.getText(),
                        User.getSHA256Hash(tfPassword.getText()),
                        avatar, cpUser.getValue().toString()));
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