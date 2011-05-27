package de.akquinet.commons.image.io;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class providing method to manipulate image.
 */
public class ManipulationHelper {

    /**
     * Makes the given {@link BufferedImage} translucent using the given transparency.
     * @param img the image
     * @param transparancy the transparency between 0 and 1.
     * @return the translucent image
     */
    public BufferedImage makeTranslucent(BufferedImage img, float transparancy) {
        BufferedImage aimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = aimg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparancy));
        g.drawImage(img, null, 0, 0);
        g.dispose();
        return aimg;
    }

    /**
     * Makes the given {@link Image} translucent using the given transparency.
     * @param img the image
     * @param transparancy the transparency between 0 and 1.
     * @return the translucent image
     * @throws IOException if the new image cannot be created
     */
    public Image makeTranslucent(Image img, float transparancy) throws IOException {
        BufferedImage aimg = makeTranslucent(img.getBufferedImage(), transparancy);
        return new Image(aimg, img.getFormat());
    }

    /**
     * Changes all pixels from the given image from the given color transparent
     * @param image the image
     * @param color the color to replace by transparent
     * @return the transparent image
     */
    public BufferedImage makeColorTransparent(BufferedImage image, Color color) {
        BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for(int i = 0; i < dimg.getHeight(); i++) {
            for(int j = 0; j < dimg.getWidth(); j++) {
                if(dimg.getRGB(j, i) == color.getRGB()) {
                dimg.setRGB(j, i, 0x8F1C1C);
                }
            }
        }
        return dimg;
    }

    /**
     * Changes all pixels from the given image from the given color transparent
     * @param image the image
     * @param color the color to replace by transparent
     * @return the transparent image
     * @throws IOException if the new image cannot be created
     */
    public Image makeColorTransparent(Image image, Color color) throws IOException {
        BufferedImage dimg = makeColorTransparent(image.getBufferedImage(), color);
        return new Image(dimg, image.getFormat());
    }

    /**
     * Flips the image horizontally
     * @param img the image
     * @return the flipped image
     */
    public BufferedImage horizontalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dimg;
    }

    /**
     * Flips the image horizontally
     * @param img the image
     * @return the flipped image
     * @throws IOException if the new image cannot be created
     */
    public Image horizontalflip(Image img) throws IOException {
        BufferedImage dimg = horizontalflip(img.getBufferedImage());
        return new Image(dimg, img.getFormat());
    }

    /**
     * Flips the image vertically
     * @param img the image
     * @return the flipped image
     */
    public BufferedImage verticalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }

    /**
     * Flips the image vertically
     * @param img the image
     * @return the flipped image
     * @throws IOException if the new image cannot be created
     */
    public Image verticalflip(Image img) throws IOException {
        BufferedImage dimg = verticalflip(img.getBufferedImage());
        return new Image(dimg, img.getFormat());
    }

    /**
     * Rotates the given image with the given angle in degree.
     * This method recomputes the image size.
     * @param image the image
     * @param angle the angle in degree
     * @return the rotated image
     */
    public BufferedImage rotate(BufferedImage image, int angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));

        int w = image.getWidth();
        int h = image.getHeight();

        int neww = (int)Math.floor(w*cos+h*sin);
        int newh = (int)Math.floor(h*cos+w*sin);

        BufferedImage dimg = new BufferedImage(neww, newh, image.getType());
        Graphics2D g = dimg.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(Math.toRadians(angle), w/2, h/2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return dimg;
    }

    /**
     * Rotates the given image with the given angle in degree.
     * This method recomputes the image size.
     * @param image the image
     * @param angle the angle in degree
     * @return the rotated image
     * @throws IOException if the new image cannot be created
     */
    public Image rotate(Image image, int angle) throws IOException {
        BufferedImage dimg = rotate(image.getBufferedImage(), angle);
        return new Image(dimg, image.getFormat());
    }

}
