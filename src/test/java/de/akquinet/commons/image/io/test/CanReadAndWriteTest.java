package de.akquinet.commons.image.io.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.akquinet.commons.image.io.IOHelper;
import de.akquinet.commons.image.io.Format;

public class CanReadAndWriteTest {

    IOHelper m_helper = new IOHelper();


    @Test
    public void testCanReadExtension() {
        Assert.assertTrue(m_helper.canReadExtension("png"));
        Assert.assertTrue(m_helper.canReadExtension("jpg"));
        Assert.assertTrue(m_helper.canReadExtension("jpeg"));
        Assert.assertTrue(m_helper.canReadExtension("gif"));

        Assert.assertFalse(m_helper.canReadExtension("ppp"));
        Assert.assertFalse(m_helper.canReadExtension("this is not an extension"));
        Assert.assertFalse(m_helper.canReadExtension(".."));
        Assert.assertFalse(m_helper.canReadExtension(""));
        Assert.assertFalse(m_helper.canReadExtension(null));
    }

    @Test
    public void testCanWriteExtension() {
        Assert.assertTrue(m_helper.canWriteExtension("png"));
        Assert.assertTrue(m_helper.canWriteExtension("jpg"));
        Assert.assertTrue(m_helper.canWriteExtension("jpeg"));
        Assert.assertTrue(m_helper.canWriteExtension("gif"));

        Assert.assertFalse(m_helper.canWriteExtension("ppp"));
        Assert.assertFalse(m_helper.canWriteExtension("this is not an extension"));
        Assert.assertFalse(m_helper.canWriteExtension(".."));
        Assert.assertFalse(m_helper.canWriteExtension(""));
        Assert.assertFalse(m_helper.canWriteExtension(null));
    }

    @Test
    public void testCanReadFormat() {
        Assert.assertTrue(m_helper.canReadFormat("png"));
        Assert.assertTrue(m_helper.canReadFormat("jpg"));
        Assert.assertTrue(m_helper.canReadFormat("jpeg"));
        Assert.assertTrue(m_helper.canReadFormat("gif"));

        Assert.assertFalse(m_helper.canReadFormat("ppp"));
        Assert.assertFalse(m_helper.canReadFormat("this is not an extension"));
        Assert.assertFalse(m_helper.canReadFormat(".."));
        Assert.assertFalse(m_helper.canReadFormat(""));
        Assert.assertFalse(m_helper.canReadFormat(null));
    }

    @Test
    public void testCanWriteFormat() {
        Assert.assertTrue(m_helper.canWriteFormat("png"));
        Assert.assertTrue(m_helper.canWriteFormat("jpg"));
        Assert.assertTrue(m_helper.canWriteFormat("jpeg"));
        Assert.assertTrue(m_helper.canWriteFormat("gif"));

        Assert.assertFalse(m_helper.canWriteFormat("ppp"));
        Assert.assertFalse(m_helper.canWriteFormat("this is not an extension"));
        Assert.assertFalse(m_helper.canWriteFormat(".."));
        Assert.assertFalse(m_helper.canWriteFormat(""));
        Assert.assertFalse(m_helper.canWriteFormat(null));
    }

    @Test
    public void testCanReadMimeType() {
        Assert.assertTrue(m_helper.canReadMimeType("image/png"));
        Assert.assertTrue(m_helper.canReadMimeType("image/bmp"));
        Assert.assertTrue(m_helper.canReadMimeType("image/jpeg"));
        Assert.assertTrue(m_helper.canReadMimeType("image/gif"));
        Assert.assertTrue(m_helper.canReadMimeType("image/x-png"));

        Assert.assertFalse(m_helper.canReadMimeType("image/ppp"));
        Assert.assertFalse(m_helper.canReadMimeType("this is not an mime type"));
        Assert.assertFalse(m_helper.canReadMimeType(".."));
        Assert.assertFalse(m_helper.canReadMimeType(""));
        Assert.assertFalse(m_helper.canReadMimeType(null));
    }

    @Test
    public void testCanWriteMimeType() {
        Assert.assertTrue(m_helper.canWriteMimeType("image/png"));
        Assert.assertTrue(m_helper.canWriteMimeType("image/bmp"));
        Assert.assertTrue(m_helper.canWriteMimeType("image/jpeg"));
        Assert.assertTrue(m_helper.canWriteMimeType("image/gif"));
        Assert.assertTrue(m_helper.canWriteMimeType("image/x-png"));

        Assert.assertFalse(m_helper.canWriteMimeType("image/ppp"));
        Assert.assertFalse(m_helper.canWriteMimeType("this is not an mime type"));
        Assert.assertFalse(m_helper.canWriteMimeType(".."));
        Assert.assertFalse(m_helper.canWriteMimeType(""));
        Assert.assertFalse(m_helper.canWriteMimeType(null));
    }

    @Test
    public void testGetReaderAndWriterByFormat() {
        Assert.assertNotNull(m_helper.getReaderForFormat(Format.BMP));
        Assert.assertNotNull(m_helper.getReaderForFormat(Format.GIF));
        Assert.assertNotNull(m_helper.getReaderForFormat(Format.PNG));
        Assert.assertNotNull(m_helper.getReaderForFormat(Format.JPEG));

        Assert.assertNotNull(m_helper.getWriterForFormat(Format.BMP));
        Assert.assertNotNull(m_helper.getWriterForFormat(Format.GIF));
        Assert.assertNotNull(m_helper.getWriterForFormat(Format.PNG));
        Assert.assertNotNull(m_helper.getWriterForFormat(Format.JPEG));
    }

    @Test
    public void testFormatExtraction() throws FileNotFoundException, IOException {
        Assert.assertEquals(Format.GIF, m_helper.getFormat(new FileInputStream(ImageReadAndWriteTest.GIF)));
    }

}
