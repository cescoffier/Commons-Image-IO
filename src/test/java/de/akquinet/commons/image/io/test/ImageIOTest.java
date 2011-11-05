package de.akquinet.commons.image.io.test;

import org.junit.Test;

import javax.imageio.ImageIO;

public class ImageIOTest {

    @Test
    public void testAvailableWriterFormats() {
        String[] formats = ImageIO.getWriterFormatNames();
        System.out.println("Supported format in write mode:");
        for (String f : formats) {
            System.out.println(f);
        }
        System.out.println("===");
    }

    @Test
    public void testAvailableWriterExtensions() {
        String[] formats = ImageIO.getWriterFileSuffixes();
        System.out.println("Supported extension in write mode:");
        for (String f : formats) {
            System.out.println(f);
        }
        System.out.println("===");
    }

    @Test
    public void testAvailableWriterMimeTypes() {
        String[] formats = ImageIO.getWriterMIMETypes();
        System.out.println("Supported mime-types in write mode:");
        for (String f : formats) {
            System.out.println(f);
        }
        System.out.println("===");
    }

}
