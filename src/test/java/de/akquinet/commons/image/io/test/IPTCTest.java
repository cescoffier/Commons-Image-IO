package de.akquinet.commons.image.io.test;


import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPProperty;
import de.akquinet.commons.image.io.Format;
import de.akquinet.commons.image.io.IPTCMetadata;
import de.akquinet.commons.image.io.Image;
import de.akquinet.commons.image.io.ImageIOUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.png.PngImageParser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IPTCTest {

    @Test
    public void testIPTCMetadata() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image image = new Image(file);
        IPTCMetadata iptc = image.getMetadata().getIPTCMetadata();
        assertNotNull(iptc);

        assertEquals("Clement Escoffier", image.getMetadata().getByLine());
        assertEquals("C'est moi", image.getMetadata().getByLineTitle());

        assertEquals("Bren", image.getMetadata().getCity());
        assertEquals("France", image.getMetadata().getCountry());
        assertEquals("Drome", image.getMetadata().getState());

        assertEquals("C'est du bois", image.getMetadata().getCaption());
        assertEquals("Clement", image.getMetadata().getEditor());

        assertEquals("Free of super use", image.getMetadata().getCopyright());

        assertEquals("BOIS BOIS BOIS", image.getMetadata().getHeadLine());
        assertEquals("Bois", image.getMetadata().getTitle());

        assertEquals("20111025", image.getMetadata().getIPTCCreationDate());
        assertTrue(image.getMetadata().getKeywords().contains("bois"));
        assertTrue(image.getMetadata().getKeywords().contains("noir"));
        assertTrue(image.getMetadata().getKeywords().contains("bren"));
    }

    @Test
    public void testIPTCMetadataCopy() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image origin = new Image(file);
        File out = File.createTempFile("commons-image-io", "-rewritten.jpg");
        origin.write(out);

        Image copy = new Image(out);
        IPTCMetadata iptc = copy.getMetadata().getIPTCMetadata();
        assertNotNull(iptc);

        assertEquals("Clement Escoffier", origin.getMetadata().getByLine());
        assertEquals("C'est moi", origin.getMetadata().getByLineTitle());

        assertEquals("Bren", origin.getMetadata().getCity());
        assertEquals("France", origin.getMetadata().getCountry());
        assertEquals("Drome", origin.getMetadata().getState());

        assertEquals("C'est du bois", origin.getMetadata().getCaption());
        assertEquals("Clement", origin.getMetadata().getEditor());

        assertEquals("Free of super use", origin.getMetadata().getCopyright());

        assertEquals("BOIS BOIS BOIS", origin.getMetadata().getHeadLine());
        assertEquals("Bois", origin.getMetadata().getTitle());

        assertEquals("20111025", origin.getMetadata().getIPTCCreationDate());
        assertTrue(origin.getMetadata().getKeywords().contains("bois"));
        assertTrue(origin.getMetadata().getKeywords().contains("noir"));
        assertTrue(origin.getMetadata().getKeywords().contains("bren"));
    }

    @Test
    public void testIPTCMetadataUpdate() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image origin = new Image(file);

        // update metadata
        List<String> keywords = origin.getMetadata().getKeywords();
        keywords.add("test");
        keywords.remove("noir");
        origin.getMetadata().setKeywords(keywords);

        origin.getMetadata().setByLine("Commons image io");
        origin.getMetadata().setByLineTitle("Commons image io Title");

        origin.getMetadata().setCaption("Du bois");
        origin.getMetadata().setEditor("Edited by Commons Image IO");

        origin.getMetadata().setCity("Berlin");
        origin.getMetadata().setCountry("Germany");
        origin.getMetadata().setState("BB");

        origin.getMetadata().setCopyright("Creative Commons");
        origin.getMetadata().setHeadline("Foo");
        origin.getMetadata().setTitle("FooTitle");

        origin.getMetadata().setIPTCCreationDate("02112011");

        File out = File.createTempFile("commons-image-io", "-rewritten.jpg");
        origin.write(out);

        Image copy = new Image(out);
        IPTCMetadata iptc = copy.getMetadata().getIPTCMetadata();
        assertNotNull(iptc);

        assertEquals("Commons image io", origin.getMetadata().getByLine());
        assertEquals("Commons image io Title", origin.getMetadata().getByLineTitle());

        assertEquals("Berlin", origin.getMetadata().getCity());
        assertEquals("Germany", origin.getMetadata().getCountry());
        assertEquals("BB", origin.getMetadata().getState());

        assertEquals("Du bois", origin.getMetadata().getCaption());
        assertEquals("Edited by Commons Image IO", origin.getMetadata().getEditor());

        assertEquals("Creative Commons", origin.getMetadata().getCopyright());

        assertEquals("Foo", origin.getMetadata().getHeadLine());
        assertEquals("FooTitle", origin.getMetadata().getTitle());

        assertEquals("02112011", origin.getMetadata().getIPTCCreationDate());
        assertTrue(origin.getMetadata().getKeywords().contains("bois"));
        assertTrue(origin.getMetadata().getKeywords().contains("test"));
        assertFalse(origin.getMetadata().getKeywords().contains("noir"));
        assertTrue(origin.getMetadata().getKeywords().contains("bren"));
    }

    @Test
    public void testIPTCExtraction() throws IOException, ImageReadException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        JpegImageMetadata metadata = (JpegImageMetadata) Sanselan
                .getMetadata(file, null);
        assertNotNull(metadata);
        assertNotNull(metadata.getPhotoshop());

        metadata.getPhotoshop().dump();
        // if(metadata.getPhotoshop().getItems().size()>0)
        // Debug.debug("iptc size",
        // metadata.getPhotoshop().getItems().size());

        JpegPhotoshopMetadata psMetadata = metadata.getPhotoshop();
        List oldRecords = psMetadata.photoshopApp13Data.getRecords();

        System.out.println();
        for (int j = 0; j < oldRecords.size(); j++) {
            IPTCRecord record = (IPTCRecord) oldRecords.get(j);
            //if (record.iptcType.type != IPTCConstants.IPTC_TYPE_CITY.type)
            System.out.println("Key: " + record.iptcType.name + " (0x"
                    + Integer.toHexString(record.iptcType.type)
                    + "), value: " + record.value);
        }
    }

    @Test
    public void testXMPExtraction() throws IOException, ImageReadException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        JpegImageMetadata metadata = (JpegImageMetadata) Sanselan
                .getMetadata(file, null);
        assertNotNull(metadata);
        assertNotNull(metadata.getPhotoshop());

        Map params = new HashMap();
        String xmpXml = new JpegImageParser().getXmpXml(new ByteSourceFile(file), params);
        System.out.println(xmpXml);
    }

    public static final File PNG = new File("src/test/resources/png/wilber-huge-alpha.png");

    @Test
    public void testMetadataEditionOnPNG() throws IOException {
        Image image = new Image(new FileInputStream(PNG));
        assertNull(image.getMetadata().getIPTCMetadata());
        image.getMetadata().setByLine("A test");

        File out = File.createTempFile("image-io", "-rewritten.jpg");
        image.write(out, Format.JPEG);

        image = new Image(out);
        assertNotNull(image.getMetadata());
        assertEquals("A test", image.getMetadata().getByLine());
        System.out.println(out.getAbsolutePath());
    }

    @Test
    public void testMetadataEditionOnBufferedImage() throws IOException {
        Image image = new Image(ImageIOUtils.getIOHelper().read(PNG), Format.PNG);
        assertNull(image.getMetadata().getIPTCMetadata());
        image.getMetadata().setByLine("A test");

        File out = File.createTempFile("image-io", "-rewritten.jpg");
        image.write(out, Format.JPEG);

        image = new Image(out);
        assertNotNull(image.getMetadata());
        assertEquals("A test", image.getMetadata().getByLine());
        System.out.println(out.getAbsolutePath());
    }

    @Test
    public void testPreviewMetadata() throws IOException, ImageReadException {
        File file = new File("src/test/resources/jpg/IMG_1626.JPG");
        Image origin = new Image(file);

        assertTrue(origin.getMetadata().getKeywords().contains("Flore"));
        assertTrue(origin.getMetadata().getKeywords().contains("fauteuil"));

        List<String> tags = origin.getMetadata().getKeywords();
        tags.add("a_test");
        origin.getMetadata().setKeywords(tags);
        File out = File.createTempFile("commons-image-io", "iptc_rewritten.jpg");
        origin.write(out);

        origin = new Image(out);

        assertTrue(origin.getMetadata().getKeywords().contains("Flore"));
        assertTrue(origin.getMetadata().getKeywords().contains("fauteuil"));
        assertTrue(origin.getMetadata().getKeywords().contains("a_test"));
    }

    @Test
    public void testXMPExtractionOnPng() throws IOException, ImageReadException, XMPException {
        File file = new File("/Users/clement/Pictures/pipe.png");

        Map params = new HashMap();
        String xmpXml = new PngImageParser().getXmpXml(new ByteSourceFile(file), params);
        System.out.println(new PngImageParser().getMetadata(new ByteSourceFile(file), params));
        System.out.println(xmpXml);

        XMPMeta meta = XMPMetaFactory.parseFromString(xmpXml);
        System.out.println("Subject: " + meta.getProperty("http://purl.org/dc/elements/1.1/", "subject").getValue());
        //XMPProperty p = meta.getArrayItem(XMPConst.NS_DC, "subject", 0);
        System.out.println(meta.countArrayItems(XMPConst.NS_DC, "subject"));
        System.out.println(meta.getArrayItem(XMPConst.NS_DC, "subject", 1).getValue());

        System.out.println(XMPMetaFactory.serializeToString(meta, null));

    }

}
