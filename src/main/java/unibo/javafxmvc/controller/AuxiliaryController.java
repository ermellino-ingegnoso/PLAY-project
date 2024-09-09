package unibo.javafxmvc.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Optional;
// I metodi di questa classe sono costruiti col presupposto di essere utilizzati in maniera sequenziale e soprattutto sullo stesso thread
public class AuxiliaryController {  //TODO: separare le resonsabilità per ambito dei metodi (in altre classi)
    private static int modalMaxWidth = 400;
    private static int modalMaxHeight = 300;

    public static String toRgbString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }
    public static Image cropImageToSquare(Image originalImage) {
        double width = originalImage.getWidth();
        double height = originalImage.getHeight();
        double squareSize = Math.min(width, height);
        double x = (width - squareSize) / 2;
        double y = (height - squareSize) / 2;

        PixelReader reader = originalImage.getPixelReader();
        return (Image) new WritableImage(reader, (int) x, (int) y, (int) squareSize, (int) squareSize);
    }
    public static void addTooltipTo(Node node, Duration d, String text) {    //TODO: aggiungere il parametro: String text
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(d);
        Tooltip.install(node, tooltip);
    }
    public static Boolean keyEnterPressed(KeyEvent event) {
        return (event.getCode() == KeyCode.ENTER);
    }
    @FXML
    public static void alertWindow(String title, String headerText, String text) {
        if(text == null) text = "";
        String finalText = text;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            if (finalText.length() > 100) {  // Se il testo è troppo lungo, usa una TextArea
                TextArea textArea = new TextArea(finalText);
                textArea.setWrapText(true);
                textArea.setEditable(false);
                textArea.setMaxWidth(400);
                textArea.setMaxHeight(300);
                alert.getDialogPane().setContent(textArea);
            } else {
                alert.setContentText(finalText);
            }
            alert.show();
        });
    }
    /** Mostra una finestra informativa
     * @param resultTitle il titolo della finestra
     * @param result il testo del risultato da visualizzare
     * @see #showInformation(String, String, String)
     * */
    public static void showResultModal(String resultTitle, String result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resultTitle);
        alert.setHeaderText(null);

        TextArea textArea = new TextArea(result);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(modalMaxWidth);
        textArea.setMaxHeight(modalMaxHeight);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
    /** Mostra una finestra informativa con <code>header</code>
     * @param title il titolo della finestra
     * @param headerText il testo dell'intestazione
     * @param contentText il testo del contenuto
     * @see #showResultModal(String, String)
     * */
    public static void showInformation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        TextArea textArea = new TextArea(contentText);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(modalMaxWidth);
        textArea.setMaxHeight(modalMaxHeight);

        alert.getDialogPane().setContent(textArea);
        alert.show();
    }

    /** Mostra una finestra di conferma
     * @param title il titolo della finestra
     * @param headerText il testo dell'intestazione
     * @param contentText il testo del contenuto
     * @return true se l'utente ha confermato, false altrimenti
     * */
    public static boolean confirmSave(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType buttonTypeYes = new ButtonType("Sì");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);  //  ButtonData.CANCEL_CLOSE indica che il pulsante implica la chiusura
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();  //  prassi nella gestione del risultato modale
        return result.isPresent() && (result.get() == buttonTypeYes);
    }
}