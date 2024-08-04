package unibo.javafxmvc.controller;

import javafx.scene.image.Image;
import com.gluonhq.charm.glisten.control.Avatar;
import unibo.javafxmvc.Main;

public class auxiliaryController {
    public static void setImageTo(Avatar avt, Image img){
        avt.setImage(img);
    }
    public static void setAvatarTo(Avatar avt){
        avt.setImage(new Image(Main.class.getResourceAsStream("/unibo/javafxmvc/Images/avatar.png")));
    }
}
