package de.akquinet.commons.image.io.test;

import de.akquinet.commons.image.io.ConversionHelper;
import de.akquinet.commons.image.io.ImageIOUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ConverterHelperTest {

    public static final File PNG = new File("src/test/resources/png/beastie.png");

    public static final File JPG = new File("src/test/resources/jpg/keepout-security-restricted-access.jpg");

    File m_tmp = new File("target/tmp");

    private ConversionHelper m_helper = new ConversionHelper();

    @Before
    public void setUp() throws IOException {
        if (m_tmp.exists()) {
            FileUtils.cleanDirectory(m_tmp);
        }
        m_tmp.mkdirs();
    }

    @Test
    public void testConversionToJPEG() throws IOException {
       BufferedImage img = ImageIOUtils.getIOHelper().read(PNG);
       byte[] bytes = m_helper.convertImageToJPEGBytes(img, 0.2f);
       FileUtils.writeByteArrayToFile(new File(m_tmp, "conversion.jpg"), bytes);
    }

    @Test
    public void testConversionToPNG() throws IOException {
       BufferedImage img = ImageIOUtils.getIOHelper().read(JPG);
       byte[] bytes = m_helper.convertImageToPNGBytes(img);
       FileUtils.writeByteArrayToFile(new File(m_tmp, "conversion.png"), bytes);
    }

}
