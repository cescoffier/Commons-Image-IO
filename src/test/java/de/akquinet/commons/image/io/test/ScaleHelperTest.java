package de.akquinet.commons.image.io.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import de.akquinet.commons.image.io.IOHelper;
import de.akquinet.commons.image.io.Format;
import de.akquinet.commons.image.io.ScaleHelper;

public class ScaleHelperTest {

    private static final int PNG_HEIGHT = 466;
    private static final int PNG_WIDTH = 430;
    public static final File PNG = new File("src/test/resources/png/beastie.png");

    private static final int JPG_WIDTH = 500;
    private static final int JPG_HEIGHT = 300;
    public static final File JPG = new File("src/test/resources/jpg/keepout-security-restricted-access.jpg");


    File m_tmp = new File("target/tmp");

    ScaleHelper m_scaler = new ScaleHelper();
    IOHelper m_img = new IOHelper();

    @Before
    public void setUp() throws IOException {
        if (m_tmp.exists()) {
            FileUtils.cleanDirectory(m_tmp);
        }
        m_tmp.mkdirs();
    }

    @Test
    public void testScalePNG() throws IOException {
        BufferedImage img = m_scaler.scale(m_img.read(PNG), 2);
        Assert.assertEquals(PNG_WIDTH * 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT * 2, img.getHeight());
        m_img.write(img, new File(m_tmp, "PNG_x_2.png"), Format.PNG);


        img = m_scaler.scale(m_img.read(PNG), 0.5f);
        Assert.assertEquals(PNG_WIDTH / 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT / 2, img.getHeight());
        m_img.write(img, new File(m_tmp, "PNG_div_2.png"), Format.PNG);
    }

    @Test
    public void testScaleJPG() throws IOException {
        BufferedImage img = m_scaler.scale(m_img.read(JPG), 2);
        Assert.assertEquals(JPG_WIDTH * 2, img.getWidth());
        Assert.assertEquals(JPG_HEIGHT * 2, img.getHeight());
        m_img.write(img, new File(m_tmp, "JPG_x_2.jpg"), Format.JPEG);


        img = m_scaler.scale(m_img.read(JPG), 0.5f);
        Assert.assertEquals(JPG_WIDTH / 2, img.getWidth());
        Assert.assertEquals(JPG_HEIGHT / 2, img.getHeight());
        m_img.write(img, new File(m_tmp, "JPG_div_2.jpg"), Format.JPEG);
    }

    @Test
    public void testScaleBicubic() throws IOException {
        BufferedImage img = m_scaler.scaleImageBicubic(m_img.read(PNG), PNG_WIDTH * 2, PNG_HEIGHT * 2);
        Assert.assertEquals(PNG_WIDTH * 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT * 2, img.getHeight());

        img = m_scaler.scaleImageBicubic(m_img.read(PNG), (int) (PNG_WIDTH * 0.5),
                (int) (PNG_HEIGHT * 0.5));
        Assert.assertEquals(PNG_WIDTH / 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT / 2, img.getHeight());
    }

    @Test
    public void testScaleNearestNeigbor() throws IOException {
        BufferedImage img = m_scaler.scaleImageNearestNeighbor(m_img.read(PNG), PNG_WIDTH * 2, PNG_HEIGHT * 2);
        Assert.assertEquals(PNG_WIDTH * 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT * 2, img.getHeight());

        img = m_scaler.scaleImageNearestNeighbor(m_img.read(PNG), (int) (PNG_WIDTH * 0.5),
                (int) (PNG_HEIGHT * 0.5));
        Assert.assertEquals(PNG_WIDTH / 2, img.getWidth());
        Assert.assertEquals(PNG_HEIGHT / 2, img.getHeight());
    }

    @Test
    public void testScale100To25() throws IOException {
        BufferedImage img = m_scaler.scale100To25(m_img.read(PNG));
        Assert.assertEquals((int) (PNG_WIDTH / 4), img.getWidth());
        Assert.assertEquals((int) (PNG_HEIGHT / 4), img.getHeight());
    }

    @Test
    public void testScale100To75And75To25() throws IOException {
        BufferedImage img = m_scaler.scale100To75(m_img.read(PNG));
        img = m_scaler.scale75To25(img);
        Assert.assertEquals((int) (PNG_WIDTH / 4), img.getWidth());
        Assert.assertEquals((int) (PNG_HEIGHT / 4), img.getHeight());
    }

    @Test
    public void testScaleToHeight() throws IOException {
        BufferedImage img = m_scaler.scaleToHeight(m_img.read(JPG), 100);
        Assert.assertEquals(JPG_WIDTH / 3, img.getWidth());
        Assert.assertEquals(100, img.getHeight());
        m_img.write(img, new File(m_tmp, "JPG_h_100.jpg"), Format.JPEG);


        img = m_scaler.scaleToHeight(m_img.read(JPG), 600);
        Assert.assertEquals(JPG_WIDTH * 2, img.getWidth());
        Assert.assertEquals(600, img.getHeight());
        m_img.write(img, new File(m_tmp, "JPG_h_600.jpg"), Format.JPEG);
    }

    @Test
    public void testScaleToWidth() throws IOException {
        BufferedImage img = m_scaler.scaleToWidth(m_img.read(JPG), 100);
        Assert.assertEquals(JPG_HEIGHT / 5, img.getHeight());
        Assert.assertEquals(100, img.getWidth());
        m_img.write(img, new File(m_tmp, "JPG_w_100.jpg"), Format.JPEG);


        img = m_scaler.scaleToWidth(m_img.read(JPG), 1000);
        Assert.assertEquals(JPG_HEIGHT * 2, img.getHeight());
        Assert.assertEquals(1000, img.getWidth());
        m_img.write(img, new File(m_tmp, "JPG_w_1000.jpg"), Format.JPEG);
    }

}
