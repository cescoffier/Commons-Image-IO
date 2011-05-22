package de.akquinet.commons.image.io;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ManipulationHelper {

    public BufferedImage makeTranslucent(BufferedImage img, float transperancy) {
        BufferedImage aimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = aimg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));
        g.drawImage(img, null, 0, 0);
        g.dispose();
        return aimg;
    }

    public Image makeTranslucent(Image img, float transperancy) throws IOException {
        BufferedImage aimg = makeTranslucent(img.getBufferedImage(), transperancy);
        return new Image(aimg, img.getFormat());
    }

    public BufferedImage makeColorTransparent(BufferedImage image, Color color) {
        BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
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

    public Image makeColorTransparent(Image image, Color color) throws IOException {
        BufferedImage dimg = makeColorTransparent(image.getBufferedImage(), color);
        return new Image(dimg, image.getFormat());
    }

    public BufferedImage horizontalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dimg;
    }

    public Image horizontalflip(Image img) throws IOException {
        BufferedImage dimg = horizontalflip(img.getBufferedImage());
        return new Image(dimg, img.getFormat());
    }

    public BufferedImage verticalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }

    public Image verticalflip(Image img) throws IOException {
        BufferedImage dimg = verticalflip(img.getBufferedImage());
        return new Image(dimg, img.getFormat());
    }

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

    public Image rotate(Image image, int angle) throws IOException {
        BufferedImage dimg = rotate(image.getBufferedImage(), angle);
        return new Image(dimg, image.getFormat());
    }

}
