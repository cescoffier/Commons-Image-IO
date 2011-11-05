package de.akquinet.commons.image.io.test;

import de.akquinet.commons.image.io.Format;
import de.akquinet.commons.image.io.IOHelper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadAndWriteTest {

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
        BufferedImage img = m_helper.read(GIF);
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        img = m_helper.read(PNG);
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        img = m_helper.read(JPG);
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        try {
            m_helper.read(new File("does not exist"));
            Assert.fail("The file does not exist - IOException expected");
        } catch (IOException e) {
            // OK
        }

        try {
            m_helper.read((File) null);
            Assert.fail("The file is null - IOException expected");
        } catch (IOException e) {
            // OK
        }

    }

    @Test
    public void testReadInputStream() throws IOException {
        // GIF
        BufferedImage img = m_helper.read(new FileInputStream(GIF));
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        img = m_helper.read(new FileInputStream(PNG));
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        img = m_helper.read(new FileInputStream(JPG));
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        try {
            m_helper.read(new FileInputStream("does not exist"));
            Assert.fail("The file does not exist - IOException expected");
        } catch (IOException e) {
            // OK
        }

        try {
            m_helper.read((FileInputStream) null);
            Assert.fail("The file is null - IOException expected");
        } catch (IOException e) {
            // OK
        }
    }

    @Test
    public void testReadByteArray() throws IOException {
        // GIF
        byte[] bytes = getByteArrayForFile(GIF);
        BufferedImage img = m_helper.read(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(GIF_HEIGHT, img.getHeight());
        Assert.assertEquals(GIF_WIDTH, img.getWidth());

        // PNG
        bytes = getByteArrayForFile(PNG);
        img = m_helper.read(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(PNG_HEIGHT, img.getHeight());
        Assert.assertEquals(PNG_WIDTH, img.getWidth());

        // JPG
        bytes = getByteArrayForFile(JPG);
        img = m_helper.read(bytes);
        Assert.assertNotNull(img);
        Assert.assertEquals(JPG_HEIGHT, img.getHeight());
        Assert.assertEquals(JPG_WIDTH, img.getWidth());

        // Error cases
        Assert.assertNull(m_helper.read(new byte[0]));


        try {
            m_helper.read((byte[]) null);
            Assert.fail("The file is empty - IOException expected");
        } catch (IOException e) {
            // OK
        }
    }

    @Test
    public void testWriteGIFToBMPFile() throws IOException {
        File tmp = File.createTempFile("image-io", ".bmp");
        BufferedImage img = m_helper.read(GIF);
        m_helper.write(img, tmp);
        BufferedImage copy = m_helper.read(tmp);
        Assert.assertNotNull(copy);
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
        System.out.println("BMP : " + tmp.getAbsolutePath());
    }

    @Test
    public void testWriteGIFToPNGFile() throws IOException {
        // use png by default.
        File tmp = File.createTempFile("image-io", "nothing");
        BufferedImage img = m_helper.read(GIF);
        m_helper.write(img, tmp);
        BufferedImage copy = m_helper.read(tmp);
        Assert.assertNotNull(copy);
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
        System.out.println("PNG : " + tmp.getAbsolutePath());
    }

    @Test(expected=IOException.class)
    public void testWriteGIFToUnknownFormat() throws IOException {
        File tmp = File.createTempFile("image-io", ".xxx");
        BufferedImage img = m_helper.read(GIF);
        m_helper.write(img, tmp);
    }

    @Test
    public void testGetBytes() throws IOException {
        File tmp = File.createTempFile("image-io", ".bmp");
        BufferedImage img = m_helper.read(GIF);
        byte[] bytes = m_helper.getBytes(img, Format.GIF);

        Assert.assertEquals(tmp.length(), bytes.length);

        bytes = m_helper.getBytes(img, Format.PNG);

        BufferedImage copy = m_helper.read(bytes);
        Assert.assertNotNull(copy);
        Assert.assertEquals(GIF_HEIGHT, copy.getHeight());
        Assert.assertEquals(GIF_WIDTH, copy.getWidth());
    }

    private byte[] getByteArrayForFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

}
