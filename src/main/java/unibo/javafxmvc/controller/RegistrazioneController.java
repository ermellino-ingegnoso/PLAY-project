package unibo.javafxmvc.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.User;
import unibo.javafxmvc.model.FormValidator;

import java.net.URL;
import java.sql.SQLException;
import java.io.File;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable {
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
    @FXML
    private Button btnAddUser;
    private FileChooser fileChooser;
    private Boolean check;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> tfNome.requestFocus()); //  imposta il focus del cursore su tfNome
        ivAvatar.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/unibo/javafxmvc/Images/avatar.png"))));
        try{
            tfUsername.setText(System.getProperty("user.name"));
        } catch (Exception e){
            e.printStackTrace();
        }
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        AuxiliaryController.addTooltipTo(ivAvatar, Duration.ZERO, "Trascina un'immagine o clicca per selezionarla");
    }
    @FXML
    private void handleColorChange(ActionEvent event) {
        lblColorPicker.setTextFill(cpUser.getValue());
        event.consume();
    }
    @FXML
    private void ivAvatarOnMouseClicked(MouseEvent event) { // fileChooser inizializzato in initialize()
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );  //  la Dialog blocca il thread su cui viene aperta
        File selectedFile = fileChooser.showOpenDialog(ivAvatar.getScene().getWindow());
        if (selectedFile != null) {
            addImageAsFileToIVAvatar(selectedFile);
            fileChooser.setInitialDirectory(selectedFile.getParentFile());
        }
        event.consume();
    }
    private Boolean addImageAsFileToIVAvatar(File file){
        if(file != null){
            try{
                ivAvatar.setImage(AuxiliaryController.cropImageToSquare(new Image(file.toURI().toString())));
                lblAvatar.setVisible(false);
                lblAvatar.setText("Errore nel caricamento dell' immagine");
                return true;
            } catch(NullPointerException npe){
                lblAvatar.setText("Errore nel caricamento dell' immagine");
                lblAvatar.setVisible(true);
            } catch(IllegalArgumentException iae){
                lblAvatar.setText("Tipo del file non supportato");
                lblAvatar.setVisible(true);
            }
        }
        return false;
    }
    @FXML
    void ivAvatarOnDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            File file = db.getFiles().get(0);
            success = addImageAsFileToIVAvatar(file);
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
        lblNome.setVisible(!tfNome.getText().trim().isEmpty() && !FormValidator.validateName(tfNome.getText()));
    }
    /*`isEmpty()` : `validateName()`  : `!isEmpty()` : `!validateName()` : `(!isEmpty() && !validateName())` :
    :-------------:-------------------:--------------:-------------------:-----------------------------------:
    :  tru        : true              : false        : false             : false                             :
    :  true       : false             : false        : true              : false                             :
    :  false      : true              : true         : false             : false                             :
    :  false      : false             : true         : true              : true                              :
    */
    @FXML
    protected void tfCognomeOnKeyTyped(KeyEvent event) {
        lblCognome.setVisible(!tfCognome.getText().trim().isEmpty() && !FormValidator.validateName(tfCognome.getText().trim()));
        event.consume();
    }
    @FXML
    protected void tfUsernameOnKeyTyped(KeyEvent event) {
        String username = tfUsername.getText().trim().toLowerCase();
        check = FormValidator.validateUsername(username);
        lblUsername.setVisible(!check);
        if(check){
            try {
                Boolean checkIfuserExists = UserDBM.userExists(username, false);
                lblRegistrato.setVisible(checkIfuserExists);
                btnAddUser.setDisable(checkIfuserExists);
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
        lblPassword.setVisible(!FormValidator.validatePassword(tfPassword.getText().trim()) && !tfPassword.getText().isEmpty());
    }
    @FXML
    protected void tfRipetiPasswordOnKeyTyped(KeyEvent event) {
        lblRipetiPassword.setVisible(false);
    }
    @FXML
    public void trimLbl(Label... lbls) {
        for (Label lbl : lbls)
            lbl.setText(lbl.getText().trim());
    }
    @FXML
    protected void btnAddUserOnMouseClicked(MouseEvent event) {
        addUser(event);
    }
    @FXML
    protected void btnAddUserOnKeyPressed(KeyEvent event) {
        if(keyEnterPressed(event))
            addUser(event);
    }
    @FXML
    private Boolean keyEnterPressed(KeyEvent event) {
        return (event.getCode() == KeyCode.ENTER);
    }
    private boolean validatePassword(String password, String ripetiPassword) {
        lblRipetiPassword.setVisible(!password.equals(ripetiPassword));
        return !lblRipetiPassword.isVisible();
    }
    private void addUser(Event event) {
        btnAddUser.setDisable(true);
        trimLbl(lblNome, lblCognome, lblUsername, lblPassword, lblRipetiPassword);
        if (FormValidator.validateName(tfNome.getText()) && FormValidator.validateName(tfCognome.getText())
                && FormValidator.validateUsername(tfUsername.getText())
                && validatePassword(tfPassword.getText(), tfRipetiPassword.getText())) {
            try {
                Boolean check = UserDBM.insertUser(new User(
                        tfNome.getText(),
                        tfCognome.getText(),
                        tfUsername.getText(),
                        User.getSHA256Hash(tfPassword.getText()),
                        ivAvatar.getImage(),
                        cpUser.getValue().toString()), false);
                lblRegistrato.setVisible(!check);
                if(check) Main.changeScene("View/Accesso.fxml");
            } catch (ConnectionException e) {
                Main.changeScene("View/ErroreDatabase.fxml");
            } catch (RuntimeException e) {
                alert.setTitle("Errore durante l'aggiunta dell'utente");
                alert.setHeaderText("Si prega di riavviare l'applicazione");
                alert.show();
                e.printStackTrace();
            }
        }
        btnAddUser.setDisable(false);
        event.consume();
    }
    @FXML
    void IndietroOnMouseClicked(MouseEvent event) {
        Main.changeScene("View/Accesso.fxml");
    }
}