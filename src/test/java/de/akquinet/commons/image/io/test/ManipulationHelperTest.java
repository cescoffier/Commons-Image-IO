package de.akquinet.commons.image.io.test;

import de.akquinet.commons.image.io.Image;
import de.akquinet.commons.image.io.ManipulationHelper;
import junit.framework.Assert;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ManipulationHelperTest {

    public static final File GIF = new File("src/test/resources/gif/PIC-iCarta-Small.gif");

    private static final Object PNG_HEIGHT = 902;
    private static final Object PNG_WIDTH = 1080;
    public static final File PNG = new File("src/test/resources/png/wilber-huge-alpha.png");

    public static final File PNG_BEASTIE = new File("src/test/resources/png/beastie.png");

    private static final Object JPG_WIDTH = 500;
    private static final Object JPG_HEIGHT = 300;
    public static final File JPG = new File("src/test/resources/jpg/keepout-security-restricted-access.jpg");


    private ManipulationHelper m_manipulation = new ManipulationHelper();

    @Test
    public void test90RotationOnJPG() throws IOException {
        Image image = new Image(JPG);
        Image image2 = m_manipulation.rotate(image, 90);

        File tmp = File.createTempFile("image-io", "rotated.jpg");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());

        Image image3 = new Image(tmp);
        Assert.assertEquals(image3.getHeight(), JPG_WIDTH);
        Assert.assertEquals(image3.getWidth(), JPG_HEIGHT);
    }

    @Test
    public void test180RotationOnJPG() throws IOException {
        Image image = new Image(JPG);
        Image image2 = m_manipulation.rotate(image, 180);

        File tmp = File.createTempFile("image-io", "rotated.jpg");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());

        Image image3 = new Image(tmp);
        Assert.assertEquals(image3.getHeight(), JPG_HEIGHT);
        Assert.assertEquals(image3.getWidth(), JPG_WIDTH);
    }

    @Test
    public void test45RotationOnJPG() throws IOException {
        Image image = new Image(JPG);
        Image image2 = m_manipulation.rotate(image, 45);

        File tmp = File.createTempFile("image-io", "rotated.jpg");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());
    }

    @Test
    public void test90RotationOnPNG() throws IOException {
        Image image = new Image(PNG);
        Image image2 = m_manipulation.rotate(image, 90);

        File tmp = File.createTempFile("image-io", "rotated.png");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());

        Image image3 = new Image(tmp);
        Assert.assertEquals(image3.getHeight(), PNG_WIDTH);
        Assert.assertEquals(image3.getWidth(), PNG_HEIGHT);
        Assert.assertTrue(image3.getMetadata().isTransparent());
    }

    @Test
    public void test180RotationOnPNG() throws IOException {
        Image image = new Image(PNG);
        Image image2 = m_manipulation.rotate(image, 180);

        File tmp = File.createTempFile("image-io", "rotated.png");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());

        Image image3 = new Image(tmp);
        Assert.assertEquals(image3.getHeight(), PNG_HEIGHT);
        Assert.assertEquals(image3.getWidth(), PNG_WIDTH);
        Assert.assertTrue(image3.getMetadata().isTransparent());
    }

    @Test
    public void testSetWhiteAsTransparent() throws IOException {
        Image image = new Image(PNG_BEASTIE);
        Assert.assertFalse(image.getMetadata().isTransparent());

        Image image2 = m_manipulation.makeColorTransparent(image, Color.WHITE);
        Assert.assertTrue(image2.getMetadata().isTransparent());

        File tmp = File.createTempFile("image-io", "transparent.png");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());
    }

    @Test
    public void testMakeTranslucid() throws IOException {
        Image image = new Image(PNG_BEASTIE);
        Assert.assertFalse(image.getMetadata().isTransparent());

        Image image2 = m_manipulation.makeTranslucent(image, 0.5f);
        Assert.assertTrue(image2.getMetadata().isTransparent());

        File tmp = File.createTempFile("image-io", "translucid.png");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());
    }

    @Test
    public void testFlips() throws IOException {
        Image image = new Image(GIF);
        Image image2 = m_manipulation.horizontalflip(image);
        image2 = m_manipulation.verticalflip(image2);

        File tmp = File.createTempFile("image-io", "flip.gif");
        image2.write(tmp);
        System.out.println(tmp.getAbsolutePath());
    }

}
