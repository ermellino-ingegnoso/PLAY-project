package unibo.javafxmvc.controller;

import com.gluonhq.charm.glisten.control.Avatar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import unibo.javafxmvc.Main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label label;
    @FXML
    private ImageView userAvatar;
    @FXML
    private GridPane mainGridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byte[] avatar = Main.currentUser.getAvatar();
        try{
            if(avatar == null){
                avatar = Files.readAllBytes((new File(getClass().getResource("/unibo/javafxmvc/Images/avatar.png").getPath())).toPath());
            }
            userAvatar.setImage(new Image(new ByteArrayInputStream(avatar)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userAvatar.setClip(new Circle(userAvatar.getFitWidth() / 2, userAvatar.getFitHeight() / 2, Math.min(userAvatar.getFitWidth(), userAvatar.getFitHeight()) / 2));
    }
}