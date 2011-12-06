package de.akquinet.commons.image.io.test;

import de.akquinet.commons.image.io.Format;
import de.akquinet.commons.image.io.IOHelper;
import de.akquinet.commons.image.io.Image;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageReadAndWriteTest {

    private static final int GIF_WIDTH = 350;
    private static final int GIF_HEIGHT = 321;
    public static final File GIF = new File("src/test/resources/gif/PIC-iCarta-Small.gif");

    private static final Object PNG_HEIGHT = 466;
    private static final Object PNG_WIDTH = 430;
    public static final File PNG = new File("src/test/resources/png/beastie.png");

    private static final Object JPG_WIDTH = 500;
    private static final Object JPG_HEIGHT = 300;
    public static final File JPG = new File("src/test/resources/jpg/keepout-security-restricted-access.jpg");

    IOHelper m_helper = new IOHelper();


    @Before
    public void setUp() {
        Assert.assertTrue(GIF.exists());
        Assert.assertTrue(PNG.exists());
        Assert.assertTrue(JPG.exists());
    }

    @Test
    public void testReadFile() throws IOException {
        // GIF
        Image img = new Image(GIF);
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        img = new Image(PNG);
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        img = new Image(JPG);
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        try {
            new Image(new File("does not exist"));
            Assert.fail("The file does not exist - IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }

        try {
            new Image((File) null);
            Assert.fail("The file is null - IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }

    }

    @Test
    public void testReadInputStream() throws IOException {
        // GIF
        Image img = new Image(new FileInputStream(GIF));
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        img = new Image(new FileInputStream(PNG));;
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        img = new Image(new FileInputStream(JPG));
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        try {
            new Image(new FileInputStream("does not exist"));
            Assert.fail("The file does not exist - IllegalArgumentException expected");
        } catch (IOException e) {
            // OK
        }

        try {
            new Image((FileInputStream) null);
            Assert.fail("The file is null - IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testReadByteArray() throws IOException {
        // GIF
        byte[] bytes = getByteArrayForFile(GIF);
        Image img = new Image(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        bytes = getByteArrayForFile(PNG);
        img = new Image(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        bytes = getByteArrayForFile(JPG);
        img = new Image(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        try {
            new Image(new byte[0]);
            Assert.fail("The file is empty - IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }


        try {
            img = new Image((byte[]) null);
            Assert.fail("The file is empty - IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testWriteGIFToBMPFile() throws IOException {
        File tmp = File.createTempFile("image-io", ".bmp");
        Image img = new Image(GIF);
        img.write(tmp, Format.BMP);
        Image copy = new Image(tmp);
        Assert.assertNotNull(copy);
        Assert.assertEquals(Format.BMP, copy.getFormat());
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
        System.out.println("BMP : " + tmp.getAbsolutePath());
    }

    @Test
    public void testWriteGIFToPNGFile() throws IOException {
        // use png by default.
        File tmp = File.createTempFile("image-io", "nothing");
        Image img = new Image(GIF);
        img.write(tmp, Format.PNG);
        Image copy = new Image(tmp);
        Assert.assertNotNull(copy);
        Assert.assertEquals(Format.PNG, copy.getFormat());
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
        System.out.println("PNG : " + tmp.getAbsolutePath());
    }

    @Test
    public void testGetBytes() throws IOException {
        File tmp = File.createTempFile("image-io", ".bmp");
        Image img = new Image(GIF);
        byte[] bytes = img.getBytes(Format.GIF);

        Assert.assertEquals(tmp.length(), bytes.length);

        bytes = img.getBytes(Format.PNG);

        BufferedImage copy = m_helper.read(bytes);
        Assert.assertNotNull(copy);
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
    }

    @Test
    public void testMetadataExtraction() throws IOException {
        //Image img = new Image(new File("src/test/resources/png/wilber-huge-alpha.png"));
        Image img = new Image(JPG);
        img.getMetadata();
    }

    @Test
    public void testFileThrowingCMMExceptionUsingImageIO() throws  IOException {
        File file = new File("src/test/resources/jpg/Break_Image_IO_CMMException.jpg");
        Image img = new Image(file);
        Assert.assertNotNull(img.getMetadata());
        Assert.assertNotNull(img.getMetadata().getExifMetadata());
        Assert.assertEquals(Format.JPEG, img.getFormat());
    }

    private byte[] getByteArrayForFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

}
