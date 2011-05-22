package de.akquinet.commons.image.io;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScaleHelper {

    public enum Interpolation {
        BILINEAR,
        BICUBIC,
        NEAREST_NEIGHBOR
    }

    private Object m_interpolation;

    public ScaleHelper(Interpolation interpolation) {
        switch (interpolation) {
        case BILINEAR:
            m_interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            break;
        case BICUBIC:
            m_interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            break;
        case NEAREST_NEIGHBOR:
            m_interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            break;
        }
    }

    public ScaleHelper() {
        m_interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    }

    public Image scaleImageBilinear(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        return new Image(bi, image.getFormat());
    }

    public Image scaleImageBicubic(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return new Image(bi, image.getFormat());
    }

    public Image scaleImageNearestNeighbor(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        return new Image(bi, image.getFormat());
    }

    public BufferedImage scaleImageBilinear(BufferedImage image, int width, int height) {
        return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    public BufferedImage scaleImageBicubic(BufferedImage image, int width, int height) {
        return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    public BufferedImage scaleImageNearestNeighbor(BufferedImage image, int width, int height) {
       return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    private BufferedImage scaleImage(BufferedImage image, int width, int height, Object interpolation) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                  interpolation);
        graphics.drawImage (image, 0, 0, width, height, null);
        graphics.dispose();
        return newImage;
    }

    public BufferedImage scale75To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width * 2 / 3, height * 2 / 3, m_interpolation);
        newImage = scaleImageBilinear(newImage, width / 3, height / 3);
        return newImage;
    }

    public BufferedImage scale100To75(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width * 3 / 4, height * 3 / 4, m_interpolation);
        return newImage;
    }

    public BufferedImage scale100To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width / 2, height / 2, m_interpolation);
        newImage = scaleImageBilinear(newImage, width / 4, height / 4);
        return newImage;
    }

    public Image scale(Image image, float ratio) throws IOException {
        BufferedImage bi = scale(image.getBufferedImage(), ratio);
        return new Image(bi, image.getFormat());
    }

    public BufferedImage scale(BufferedImage image, float ratio) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, (int) (width * ratio), (int) (height * ratio),
                m_interpolation);
        return newImage;
    }

}
