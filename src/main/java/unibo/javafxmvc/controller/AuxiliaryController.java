package unibo.javafxmvc.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import unibo.javafxmvc.Main;
import unibo.javafxmvc.model.User;

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
    /**@param event l'evento da controllare
     * @return <b>true</b> se il tasto premuto è il tasto "Invio", <br> <b>false</b> altrimenti
     * */
    public static Boolean keyEnterPressed(KeyEvent event) {
        return (event.getCode() == KeyCode.ENTER);
    }
    /** Mostra una finestra di avviso che si adatta per contenere un testo lungo
     * */
    @FXML
    public static void alertWindow(String title, String headerText, String text) {
        if(text == null) text = "";
        final String finalText = text;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            if (finalText.length() > 50) {  // Se il testo è troppo lungo, usa una TextArea
                TextArea textArea = new TextArea(finalText);
                textArea.setWrapText(true);
                textArea.setEditable(false);
                textArea.setMaxWidth(400);
                textArea.setMaxHeight(300);
                alert.getDialogPane().setContent(textArea);
            } else alert.setContentText(finalText);
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
    /**Costruisce un avatar circolare con un bordo colorato ed una label con il nome dell'utente anch'essa colorata in base al colore selezionato dallo <code>User</code>; <br> posiziona questo avatar in un <code>GridPane</code> alla colonna 0 con indice di riga 1
     * @param utente l'utente di cui si vuole costruire l'avatar
     * @param avatar il controllo <code>ImageView</code> che rappresenta l'avatar
     * @param circleAvatar il controllo <code>Circle</code> che rappresenta il bordo dell'avatar
     * @param label la label che rappresenta lo username
     * @param gridPane il <code>GridPane</code> che contiene tutti i controlli
     * */
    public static void initAvatar(User utente, ImageView avatar, Circle circleAvatar, Label label, GridPane gridPane){
        avatar.setImage(utente.getAvatar());

        Circle circle = new Circle(avatar.getFitWidth() / 2, avatar.getFitHeight() / 2, Math.min(avatar.getFitWidth(), avatar.getFitHeight()) / 2);
        circle.setStroke(Color.web(utente.getColor()));
        circle.setStrokeWidth(0);
        avatar.setClip(circle);
        drawBordersInGrid(Color.web(utente.getColor()), circleAvatar, label, gridPane);
        label.setText(utente.getUsername());

        Rotate rotate = new Rotate();
        rotate.setPivotX(avatar.getFitWidth() / 2);
        rotate.setPivotY(avatar.getFitHeight() / 2);
        avatar.getTransforms().add(rotate);
    }
    /**Costruisce un avatar circolare con un bordo colorato ed una label con il nome dell'utente anch'essa colorata in base al colore selezionato dallo <code>User</code>; <br> posiziona questo avatar in un <code>Pane</code>
     * @param utente l'utente di cui si vuole costruire l'avatar
     * @param pane il <code>Pane</code> che contiene tutti i controlli
     * @param dimensione la dimensione dell'avatar
     * */
    public static void initAvatar(User utente, Pane pane, int dimensione) {
        ImageView avatar = new ImageView();
        avatar.setImage(utente.getAvatar());
        avatar.setFitHeight(dimensione); // Altezza avatar
        avatar.setFitWidth(dimensione);  // Larghezza avatar

        Circle circleAvatar = new Circle(avatar.getFitWidth() / 2, avatar.getFitHeight() / 2, (Math.min(avatar.getFitWidth(), avatar.getFitHeight()) / 2) - (double)(dimensione/10));
        circleAvatar.setStroke(Color.web(utente.getColor()));
        circleAvatar.setStrokeWidth(2);
        avatar.setClip(circleAvatar);

        Label label = new Label(utente.getUsername());
        label.setTextFill(Color.web(utente.getColor()));

        // Creare un nuovo HBox per contenere i nuovi nodi
        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(avatar, label);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(hbox, Priority.ALWAYS);
        pane.getChildren().setAll(hbox);
    }
    private static void drawBorders(){

    }
    private static void drawBordersInGrid(Color borderColor,Circle circleAvatar, Label label, GridPane gridPane){
        circleAvatar.setStroke(borderColor);
        label.setTextFill(borderColor);
        Line line = new Line(0, 0, gridPane.getWidth(), 0);
        line.setStroke(borderColor);
        line.setStrokeWidth(2);
        Pane linePane = new Pane();
        linePane.getChildren().add(line);
        linePane.setPrefSize(gridPane.getWidth(), line.getStrokeWidth());
        gridPane.add(linePane, 0, 1);
    }
}