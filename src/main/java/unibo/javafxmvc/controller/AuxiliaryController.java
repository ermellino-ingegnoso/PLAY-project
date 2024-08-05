package unibo.javafxmvc.controller;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class AuxiliaryController {
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
        WritableImage croppedImage = new WritableImage(reader, (int) x, (int) y, (int) squareSize, (int) squareSize);
        return croppedImage;
    }
}
