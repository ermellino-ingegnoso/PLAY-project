package unibo.javafxmvc.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ImpostaNumeroBlocchiController implements Initializable {
    @FXML
    private Spinner<Integer> spinnerBlocchi;
    private Stage dialogStage;
    private boolean okClicked = false;
    private int number;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerBlocchi.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public boolean isOkClicked() {
        return okClicked;
    }
    public int getNumber() {
        return number;
    }
    @FXML
    void ProcediOnKeyPressed(KeyEvent event) {
        if(AuxiliaryController.keyEnterPressed(event)) Procedi();
    }
    @FXML
    void ProcediOnMouseClicked(MouseEvent event) {
        Procedi();
    }
    @FXML
    protected void Procedi() {
        number = spinnerBlocchi.getValue();
        okClicked = true;
        dialogStage.close();
    }
}
