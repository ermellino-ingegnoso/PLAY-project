package unibo.javafxmvc.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import unibo.javafxmvc.DAO.UserDBM;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.exception.ConnectionException;
import unibo.javafxmvc.model.FormValidator;
import unibo.javafxmvc.model.User;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class ModificaProfiloController {
    @FXML
    private Button btnAddUser;
    @FXML
    private Circle circleAvatar;
    @FXML
    private ColorPicker cpUser;
    @FXML
    private ImageView ivAvatar;
    @FXML
    private Label lblAvatar;
    @FXML
    private Label lblCognome;
    @FXML
    private Label lblColorPicker;
    @FXML
    private Label lblConnessione;
    @FXML
    private Label lblNome;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblRegistrato;
    @FXML
    private Label lblRipetiPassword;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblUsername1;
    @FXML
    private TextField tfCognome;
    @FXML
    private TextField tfNome;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private PasswordField tfRipetiPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Label lblPropCognome;
    @FXML
    private Label lblPropNome;
    @FXML
    private Label lblPropPassword;
    @FXML
    private Label lblPropUsername;
    @FXML
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private FileChooser fileChooser;
    private Boolean check;

    @FXML
    public void initialize() {
        Platform.runLater(() -> tfNome.requestFocus()); //  imposta il focus del cursore su tfNome
        AuxiliaryController.initAvatar(Main.currentUser, userAvatar, circleAvatar, lblUsername1, mainGridPane);
        reimpostaCampi();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        AuxiliaryController.addTooltipTo(ivAvatar, Duration.ZERO, "Trascina un'immagine o clicca per selezionarla");
        AuxiliaryController.addTooltipTo(lblPropNome, Duration.ZERO, "Clicca per resettare il nome");
        AuxiliaryController.addTooltipTo(lblPropCognome, Duration.ZERO, "Clicca per resettare il cognome");
        AuxiliaryController.addTooltipTo(lblPropUsername, Duration.ZERO, "Clicca per resettare lo username");
        AuxiliaryController.addTooltipTo(lblPropPassword, Duration.ZERO, "Riscrivi la tua password o impostane una nuova");
    }
    private void reimpostaCampi(){
        try{
            ivAvatar.setImage(Main.currentUser.getAvatar());
            tfNome.setText(Main.currentUser.getNome());
            tfCognome.setText(Main.currentUser.getCognome());
            tfUsername.setText(Main.currentUser.getUsername());
            cpUser.setValue(Color.web(Main.currentUser.getColor()));
        } catch (Exception e){ e.printStackTrace();}
    }
    @FXML
    void IndietroOnMouseClicked(MouseEvent event) { Main.changeScene("View/UserHome.fxml");}
    @FXML
    private void handleColorChange(ActionEvent event) {
        lblColorPicker.setTextFill(cpUser.getValue());
        event.consume();
    }
    @FXML
    void lblCognomePropOnMouseClicked(MouseEvent event) {   tfCognome.setText(Main.currentUser.getCognome());}
    @FXML
    void lblNomePropOnMouseClicked(MouseEvent event) {      tfNome.setText(Main.currentUser.getNome());}
    @FXML
    void lblUsernamePropOnMouseClicked(MouseEvent event) {
        tfUsername.setText(Main.currentUser.getUsername());
        lblRegistrato.setVisible(false);
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
    @FXML
    protected void tfCognomeOnKeyTyped(KeyEvent event) {
        lblCognome.setVisible(!tfCognome.getText().trim().isEmpty() && !FormValidator.validateName(tfCognome.getText().trim()));
        event.consume();
    }
    @FXML
    protected void tfUsernameOnKeyTyped(KeyEvent event) {
        lblUsername.setVisible(!FormValidator.validateUsername(tfUsername.getText().trim().toLowerCase()));
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
                && FormValidator.validateUsername(tfUsername.getText()) && !tfPassword.getText().isEmpty()
                && validatePassword(tfPassword.getText(), tfRipetiPassword.getText())) {
            try {
                int id = UserDBM.getUserID(Main.currentUser.getUsername(), Main.admin);
                Main.currentUser.modificaCampi(
                        tfNome.getText(),
                        tfCognome.getText(),
                        tfUsername.getText(),
                        User.getSHA256Hash(tfPassword.getText()),
                        ivAvatar.getImage(),
                        cpUser.getValue().toString());
                if(UserDBM.updateUserById(Main.currentUser, id, Main.admin)) Main.changeScene("View/UserHome.fxml");
                else AuxiliaryController.alertWindow("Errore", "Non è stato possibile aggiornare l'utente", "Riprova più tardi");
            } catch (ConnectionException e) {
                Main.changeScene("View/ErroreDatabase.fxml");
            } catch (RuntimeException e) {
                alert.setTitle("Errore durante la modifica dell'utente");
                alert.setHeaderText("Si prega di riavviare l'applicazione");
                alert.show();
            }
        }
        btnAddUser.setDisable(false);
        event.consume();
    }
}
