package de.akquinet.commons.image.io;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ResizeUtils {

    public static BufferedImage scaleImageBilinear(BufferedImage image, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                  RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage (image, 0, 0, width, height, null);
        graphics.dispose();
        return newImage;
    }

    public static BufferedImage scale75To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImageBilinear(image, width * 2 / 3, height * 2 / 3);
        newImage = scaleImageBilinear(newImage, width / 3, height / 3);
        return newImage;
    }

    public static BufferedImage scale100To75(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImageBilinear(image, width * 3 / 4, height * 3 / 4);
        return newImage;
    }

    public static BufferedImage scale100To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImageBilinear(image, width / 2, height / 2);
        newImage = scaleImageBilinear(newImage, width / 4, height / 4);
        return newImage;
    }

}
