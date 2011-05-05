package de.akquinet.commons.image.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class BufferedImageUtils {


    public static BufferedImage getBufferedImage(File f) throws IOException {
        return ImageIO.read(f);
    }

    public static BufferedImage getBufferedImage(InputStream is) throws IOException {
        return ImageIO.read(is);
    }

    public static BufferedImage getBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(is);
        closeQuietly(is);
        return img;
    }

    public static boolean closeQuietly(Closeable stream) {
        try {
            stream.close();
            return true;
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

}
