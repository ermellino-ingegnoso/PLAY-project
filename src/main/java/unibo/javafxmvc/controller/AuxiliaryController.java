package unibo.javafxmvc.controller;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

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
        return (Image) new WritableImage(reader, (int) x, (int) y, (int) squareSize, (int) squareSize);
    }
    public static void addTooltipTo(Duration d, Node node) {
        Tooltip tooltipImg = new Tooltip("Trascina un'immagine");
        tooltipImg.setShowDelay(d);
        Tooltip.install(node, tooltipImg);
    }
}
