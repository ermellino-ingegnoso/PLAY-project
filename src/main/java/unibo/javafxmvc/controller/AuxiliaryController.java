package unibo.javafxmvc.controller;

import javafx.scene.paint.Color;

public class AuxiliaryController {
    public static String toRgbString(Color color) {
        return "rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255) + "," + (int) (color.getBlue() * 255) + ")";
    }
}
