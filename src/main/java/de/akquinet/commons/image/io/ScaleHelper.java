package de.akquinet.commons.image.io;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class helping to scale images.
 */
public class ScaleHelper {

    /**
     * Interpolation algorithm
     */
    public enum Interpolation {
        BILINEAR,
        BICUBIC,
        NEAREST_NEIGHBOR
    }

    /**
     * The interpolation algorithm to use.
     * {@link Interpolation#BILINEAR} by default
     */
    private Object m_interpolation;

    /**
     * Creates a ScaleHelper.
     * @param interpolation the interpolation algorithm to use
     */
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

    /**
     * Creates a ScaleHelper using {@link Interpolation#BILINEAR}
     */
    public ScaleHelper() {
        m_interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#BILINEAR} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public Image scaleImageBilinear(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        return new Image(bi, image.getFormat());
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#BICUBIC} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public Image scaleImageBicubic(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return new Image(bi, image.getFormat());
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#NEAREST_NEIGHBOR} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public Image scaleImageNearestNeighbor(Image image, int width, int height) throws IOException {
        BufferedImage bi = scaleImage(image.getBufferedImage(), width, height,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        return new Image(bi, image.getFormat());
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#BILINEAR} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public BufferedImage scaleImageBilinear(BufferedImage image, int width, int height) {
        return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#BICUBIC} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public BufferedImage scaleImageBicubic(BufferedImage image, int width, int height) {
        return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    /**
     * Scales the image to the given width and height using the
     * {@link Interpolation#NEAREST_NEIGHBOR} algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the height
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public BufferedImage scaleImageNearestNeighbor(BufferedImage image, int width, int height) {
       return scaleImage(image, width, height, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    /**
     * Utility method scaling the given image to the desired size using the given
     * interpolation algorithm.
     * @param image the image
     * @param width the desired width
     * @param height the desired height
     * @param interpolation the interpolation algorithm
     * @return the scaled image
     */
    private BufferedImage scaleImage(BufferedImage image, int width, int height, Object interpolation) {
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                  interpolation);
        graphics.drawImage (image, 0, 0, width, height, null);
        graphics.dispose();
        return newImage;
    }

    /**
     * Reduces the image size an image by 50%.
     * @param image the image
     * @return the reduced image
     */
    public BufferedImage scale75To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width * 2 / 3, height * 2 / 3, m_interpolation);
        newImage = scaleImageBilinear(newImage, width / 3, height / 3);
        return newImage;
    }

    /**
     * Reduces the image by 25%.
     * @param image the image
     * @return the reduce image
     */
    public BufferedImage scale100To75(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width * 3 / 4, height * 3 / 4, m_interpolation);
        return newImage;
    }

    /**
     * Reduces the image size by 75%.
     * @param image the image
     * @return the reduced image
     */
    public BufferedImage scale100To25(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, width / 2, height / 2, m_interpolation);
        newImage = scaleImageBilinear(newImage, width / 4, height / 4);
        return newImage;
    }

    /**
     * Scales the given image using the given ratio
     * @param image the image
     * @param ratio the ratio
     * @return the scaled image
     * @throws IOException if the image cannot be scaled
     */
    public Image scale(Image image, float ratio) throws IOException {
        BufferedImage bi = scale(image.getBufferedImage(), ratio);
        return new Image(bi, image.getFormat());
    }

    /**
     * Scales the given image using the given ratio
     * @param image the image
     * @param ratio the ratio
     * @return the scaled image
     */
    public BufferedImage scale(BufferedImage image, float ratio) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = scaleImage(image, (int) (width * ratio), (int) (height * ratio),
                m_interpolation);
        return newImage;
    }

}
