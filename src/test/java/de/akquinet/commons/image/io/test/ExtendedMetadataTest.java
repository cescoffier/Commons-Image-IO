package de.akquinet.commons.image.io.test;


import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPProperty;
import de.akquinet.commons.image.io.*;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.png.PngConstants;
import org.apache.sanselan.formats.png.PngImageParser;
import org.apache.sanselan.formats.png.PngWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ExtendedMetadataTest {

    File dir;

    @Before
    public void setUp() {
        dir = new File("target/pictures/metadata");
        dir.mkdirs();
    }

    /**
     * Checks whether we successfully read IPTC and XMP data from a JPEG file.
     * Then the file is written to check whether we write the metadata correctly.
     * @throws IOException
     */
    @Test
    public void testExtendedMetadataOnJPEG() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image image = new Image(file);
        
        // Check extended metadata
        assertEquals("Clement Escoffier", image.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("C'est moi", image.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Bren", image.getMetadata().getExtendedMetadata().getCity());
        assertEquals("France", image.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("Drome", image.getMetadata().getExtendedMetadata().getState());

        assertEquals("C'est du bois", image.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Clement", image.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Free of super use", image.getMetadata().getExtendedMetadata().getCopyright());

        assertEquals("BOIS BOIS BOIS", image.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("Bois", image.getMetadata().getExtendedMetadata().getTitle());

        //assertEquals("20111025", image.getMetadata().getExtendedMetadata().getCreationDate());
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("bois"));
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("noir"));
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("bren"));        
        
        // Check IPTC
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
        
        // Check the IPTC and XMP generation
        Assert.assertNotNull(image.getMetadata().getExtendedMetadata().getPhotoshopApp13Data());
        Assert.assertNotNull(image.getMetadata().getExtendedMetadata().getXMPMetadata());
        
        // Write the file
        File out = new File(dir, "testExtendedMetadata.jpg");
        image.write(out);
        
        image = new Image(out);
        // Check extended metadata
        assertEquals("Clement Escoffier", image.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("C'est moi", image.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Bren", image.getMetadata().getExtendedMetadata().getCity());
        assertEquals("France", image.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("Drome", image.getMetadata().getExtendedMetadata().getState());

        assertEquals("C'est du bois", image.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Clement", image.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Free of super use", image.getMetadata().getExtendedMetadata().getCopyright());

        assertEquals("BOIS BOIS BOIS", image.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("Bois", image.getMetadata().getExtendedMetadata().getTitle());

        assertEquals("20111025", image.getMetadata().getExtendedMetadata().getCreationDate());
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("bois"));
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("noir"));
        assertTrue(image.getMetadata().getExtendedMetadata().getKeywords().contains("bren"));

        // Check IPTC
        iptc = image.getMetadata().getIPTCMetadata();
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

        // Check the IPTC and XMP generation
        Assert.assertNotNull(image.getMetadata().getExtendedMetadata().getPhotoshopApp13Data());
        Assert.assertNotNull(image.getMetadata().getExtendedMetadata().getXMPMetadata());
    }

    /**
     * Checks a copy of an (JPEG) image containing metadata. The metadata must be also included
     * into the destination file.
     * @throws IOException
     */
    @Test
    public void testExtendedMetadataCopyOnJPEG() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image origin = new Image(file);
        File out = new File(dir, "testExtendedMetadataCopyOnJPEG.jpg");
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

        //assertEquals("20111025", origin.getMetadata().getIPTCCreationDate());
        assertTrue(origin.getMetadata().getKeywords().contains("bois"));
        assertTrue(origin.getMetadata().getKeywords().contains("noir"));
        assertTrue(origin.getMetadata().getKeywords().contains("bren"));
    }

    /**
     * Tests the changes on extended metadata (on JPEG). The final file contains both XMP and IPTC metadata
     * (updated).
     * @throws IOException
     */
    @Test
    public void testExtendedMetadataUpdateOnJPEG() throws IOException {
        File file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image origin = new Image(file);

        // update metadata
        List<String> keywords = origin.getMetadata().getKeywords();
        keywords.add("test");
        keywords.remove("noir");
        origin.getMetadata().getExtendedMetadata().setKeywords(keywords);

        origin.getMetadata().getExtendedMetadata().setAuthor("Commons image io");
        origin.getMetadata().getExtendedMetadata().setExtendedAuthor("Commons image io Title");

        origin.getMetadata().getExtendedMetadata().setDescription("Du bois");
        origin.getMetadata().getExtendedMetadata().setEditor("Edited by Commons Image IO");

        origin.getMetadata().getExtendedMetadata().setCity("Berlin");
        origin.getMetadata().getExtendedMetadata().setCountry("Germany");
        origin.getMetadata().getExtendedMetadata().setState("BB");

        origin.getMetadata().getExtendedMetadata().setCopyright("Creative Commons");
        origin.getMetadata().getExtendedMetadata().setUsage("CC-SA-NC");
        origin.getMetadata().getExtendedMetadata().setSynopsis("Foo");
        origin.getMetadata().getExtendedMetadata().setTitle("FooTitle");

        origin.getMetadata().getExtendedMetadata().setCreationDate("02112011");

        File out = new File(dir, "testExtendedMetadataUpdate.jpg");
        origin.write(out);

        Image copy = new Image(out);
        IPTCMetadata iptc = copy.getMetadata().getIPTCMetadata();

        assertNotNull(iptc);

        assertEquals("Commons image io", copy.getMetadata().getByLine());
        assertEquals("Commons image io Title", copy.getMetadata().getByLineTitle());
        assertEquals("Commons image io", copy.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("Commons image io Title", copy.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Berlin", copy.getMetadata().getExtendedMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getExtendedMetadata().getState());
        assertEquals("Berlin", copy.getMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getState());

        assertEquals("Du bois", copy.getMetadata().getCaption());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getEditor());
        assertEquals("Du bois", copy.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Creative Commons", copy.getMetadata().getCopyright());
        assertEquals("Creative Commons", copy.getMetadata().getExtendedMetadata().getCopyright());
        assertEquals("CC-SA-NC", copy.getMetadata().getExtendedMetadata().getUsage());

        assertEquals("Foo", copy.getMetadata().getHeadLine());
        assertEquals("FooTitle", copy.getMetadata().getTitle());
        assertEquals("Foo", copy.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("FooTitle", copy.getMetadata().getExtendedMetadata().getTitle());

        assertEquals("02112011", copy.getMetadata().getIPTCCreationDate());
        assertEquals("02112011", copy.getMetadata().getExtendedMetadata().getCreationDate());

        assertTrue(copy.getMetadata().getKeywords().contains("bois"));
        assertTrue(copy.getMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getKeywords().contains("noir"));
        assertTrue(copy.getMetadata().getKeywords().contains("bren"));

        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("bois"));
        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getExtendedMetadata().getKeywords().contains("noir"));
        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("bren"));
    }

    /**
     * Simply tests the Sanselan API to extract the IPTC Records from a JPEG file
     * @throws IOException
     * @throws ImageReadException
     */
    @Test
    public void testIPTCExtractionOnJPEGFile() throws IOException, ImageReadException {
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

    /**
     * Tests the insertion of new metadata inside a file without metadata.
     * Note: The inserted metadata have nothing to do with the actual edited image.
     * @throws IOException
     */
    @Test
    public void testSettingExtendedMetadataOnAJPEGFileWithoutMetadata() throws  IOException {
        File file = new File("src/test/resources/jpg/flore.JPG");
        Image origin = new Image(file);

        // update metadata
        List<String> keywords = origin.getMetadata().getKeywords();
        // No metadata
        Assert.assertNull(keywords);
        keywords = new ArrayList<String>();
        keywords.add("test");
        origin.getMetadata().getExtendedMetadata().setKeywords(keywords);

        origin.getMetadata().getExtendedMetadata().setAuthor("Commons image io");
        origin.getMetadata().getExtendedMetadata().setExtendedAuthor("Commons image io Title");

        origin.getMetadata().getExtendedMetadata().setDescription("Du bois");
        origin.getMetadata().getExtendedMetadata().setEditor("Edited by Commons Image IO");

        origin.getMetadata().getExtendedMetadata().setCity("Berlin");
        origin.getMetadata().getExtendedMetadata().setCountry("Germany");
        origin.getMetadata().getExtendedMetadata().setState("BB");

        origin.getMetadata().getExtendedMetadata().setCopyright("Creative Commons");
        origin.getMetadata().getExtendedMetadata().setUsage("CC-SA-NC");
        origin.getMetadata().getExtendedMetadata().setSynopsis("Foo");
        origin.getMetadata().getExtendedMetadata().setTitle("FooTitle");

        origin.getMetadata().getExtendedMetadata().setCreationDate("02112011");

        File out = new File(dir, "testSettingExtendedMetadataOnAJPEGFileWithoutMetadata.jpg");
        origin.write(out);

        Image copy = new Image(out);
        IPTCMetadata iptc = copy.getMetadata().getIPTCMetadata();
        assertNotNull(iptc);

        assertEquals("Commons image io", copy.getMetadata().getByLine());
        assertEquals("Commons image io Title", copy.getMetadata().getByLineTitle());
        assertEquals("Commons image io", copy.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("Commons image io Title", copy.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Berlin", copy.getMetadata().getExtendedMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getExtendedMetadata().getState());
        assertEquals("Berlin", copy.getMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getState());

        assertEquals("Du bois", copy.getMetadata().getCaption());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getEditor());
        assertEquals("Du bois", copy.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Creative Commons", copy.getMetadata().getCopyright());
        assertEquals("Creative Commons", copy.getMetadata().getExtendedMetadata().getCopyright());
        assertEquals("CC-SA-NC", copy.getMetadata().getExtendedMetadata().getUsage());

        assertEquals("Foo", copy.getMetadata().getHeadLine());
        assertEquals("FooTitle", copy.getMetadata().getTitle());
        assertEquals("Foo", copy.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("FooTitle", copy.getMetadata().getExtendedMetadata().getTitle());

        assertEquals("02112011", copy.getMetadata().getIPTCCreationDate());
        assertEquals("02112011", copy.getMetadata().getExtendedMetadata().getCreationDate());

        assertTrue(copy.getMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getKeywords().contains("noir"));

        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getExtendedMetadata().getKeywords().contains("noir"));
        
        // Check that we still have the EXIF metadata
        assertNotNull(copy.getMetadata().getLocation());
    }

    /**
     * Checks that we don't insert metadata when rewriting a JPEG without metadata.
     * Checks that we don't forget to insert metadata when rewriting a JPEG without loading the metadata.
     * @throws IOException
     */
    @Test
    public void testRewritingJPEGFileWithoutMetadata() throws IOException {
        File file = new File("src/test/resources/jpg/keepout-security-restricted-access.jpg");
        Image originA = new Image(file);
        String xmp = originA.getMetadata().getXmp();
        assertEquals(0, originA.getMetadata().getIPTCMetadata().getOriginalIPTCMetadata().getRecords().size());
        File out = new File(dir, "testRewritingJPEGFileWithoutMetadata-A.jpg");
        originA.write(out);
        Image a = new Image(out);
        
        assertNotNull(a.getMetadata().getExifMetadata());
        assertEquals(xmp, a.getMetadata().getXmp());
        assertEquals(0, a.getMetadata().getIPTCMetadata().getOriginalIPTCMetadata().getRecords().size());

        file = new File("src/test/resources/jpg/IMG_0467.jpg");
        Image originB = new Image(file);
        out = new File(dir, "testRewritingJPEGFileWithoutMetadata-B.jpg");
        originB.write(out);
        Image b = new Image(out);

        assertFalse(b.getMetadata().getIPTCMetadata().getOriginalIPTCMetadata().getRecords().isEmpty());
        assertNotNull(b.getMetadata().getXmp());
        assertNotNull(b.getMetadata().getExifMetadata());
    }

    /**
     * Checks the XMP extraciton on JPEG file using Sanselan
     * @throws IOException
     * @throws ImageReadException
     */
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

    /**
     * Checks the insertion of XMP metadata in a picture that does not contain XMP.
     * The inserted metadata have nothing to do with the actual image.
     * @throws IOException
     */
    @Test
    public void testExtendedMetadataUpdateOnPNG() throws IOException {
        Image origin = new Image(new FileInputStream(PNG));
        assertNull(origin.getMetadata().getIPTCMetadata().getOriginalIPTCMetadata());
        origin.getMetadata().getExtendedMetadata().setAuthor("Commons image io");
        origin.getMetadata().getExtendedMetadata().setExtendedAuthor("Commons image io Title");

        origin.getMetadata().getExtendedMetadata().setDescription("Du bois");
        origin.getMetadata().getExtendedMetadata().setEditor("Edited by Commons Image IO");

        origin.getMetadata().getExtendedMetadata().setCity("Berlin");
        origin.getMetadata().getExtendedMetadata().setCountry("Germany");
        origin.getMetadata().getExtendedMetadata().setState("BB");

        origin.getMetadata().getExtendedMetadata().setCopyright("Creative Commons");
        origin.getMetadata().getExtendedMetadata().setUsage("CC-SA-NC");
        origin.getMetadata().getExtendedMetadata().setSynopsis("Foo");
        origin.getMetadata().getExtendedMetadata().setTitle("FooTitle");

        origin.getMetadata().getExtendedMetadata().setCreationDate("02112011");
        origin.getMetadata().getExtendedMetadata().setKeywords(Arrays.asList("bois", "noir"));

        File out = new File(dir, "testExtendedMetadataUpdateOnPNG.png");
        origin.write(out);
        Image copy = new Image(out);


        assertEquals("Commons image io", copy.getMetadata().getByLine());
        assertEquals("Commons image io", copy.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("Commons image io Title", copy.getMetadata().getByLineTitle());
        assertEquals("Commons image io Title", copy.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Berlin", copy.getMetadata().getExtendedMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getExtendedMetadata().getState());
        assertEquals("BB", copy.getMetadata().getState());
        assertEquals("Berlin", copy.getMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getCountry());


        assertEquals("Du bois", copy.getMetadata().getCaption());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getEditor());
        assertEquals("Du bois", copy.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Creative Commons", copy.getMetadata().getCopyright());
        assertEquals("Creative Commons", copy.getMetadata().getExtendedMetadata().getCopyright());
        assertEquals("CC-SA-NC", copy.getMetadata().getExtendedMetadata().getUsage());

        assertEquals("Foo", copy.getMetadata().getHeadLine());
        assertEquals("Foo", copy.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("FooTitle", copy.getMetadata().getTitle());
        assertEquals("FooTitle", copy.getMetadata().getExtendedMetadata().getTitle());

        assertTrue(copy.getMetadata().getKeywords().contains("bois"));
        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("bois"));

    }

    /**
     * Checks whether the metadata are correctly written when converting an image.
     * @throws IOException
     */
    @Test
    public void testExtendedMetadataWithFormatChange() throws IOException {
        Image origin = new Image(ImageIOUtils.getIOHelper().read(PNG), Format.PNG);
        assertNull(origin.getMetadata().getIPTCMetadata().getOriginalIPTCMetadata());

        // update metadata
        List<String> keywords = origin.getMetadata().getKeywords();
        // No metadata
        Assert.assertNull(keywords);
        keywords = new ArrayList<String>();
        keywords.add("test");
        origin.getMetadata().getExtendedMetadata().setKeywords(keywords);

        origin.getMetadata().getExtendedMetadata().setAuthor("Commons image io");
        origin.getMetadata().getExtendedMetadata().setExtendedAuthor("Commons image io Title");

        origin.getMetadata().getExtendedMetadata().setDescription("Du bois");
        origin.getMetadata().getExtendedMetadata().setEditor("Edited by Commons Image IO");

        origin.getMetadata().getExtendedMetadata().setCity("Berlin");
        origin.getMetadata().getExtendedMetadata().setCountry("Germany");
        origin.getMetadata().getExtendedMetadata().setState("BB");

        origin.getMetadata().getExtendedMetadata().setCopyright("Creative Commons");
        origin.getMetadata().getExtendedMetadata().setUsage("CC-SA-NC");
        origin.getMetadata().getExtendedMetadata().setSynopsis("Foo");
        origin.getMetadata().getExtendedMetadata().setTitle("FooTitle");

        origin.getMetadata().getExtendedMetadata().setCreationDate("02112011");

        File out = new File(dir, "testExtendedMetadataWithFormatChange.jpg");
        origin.write(out, Format.JPEG);

        Image copy = new Image(out);

        assertEquals("Commons image io", copy.getMetadata().getByLine());
        assertEquals("Commons image io Title", copy.getMetadata().getByLineTitle());
        assertEquals("Commons image io", copy.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("Commons image io Title", copy.getMetadata().getExtendedMetadata().getExtendedAuthor());

        assertEquals("Berlin", copy.getMetadata().getExtendedMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getExtendedMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getExtendedMetadata().getState());
        assertEquals("Berlin", copy.getMetadata().getCity());
        assertEquals("Germany", copy.getMetadata().getCountry());
        assertEquals("BB", copy.getMetadata().getState());

        assertEquals("Du bois", copy.getMetadata().getCaption());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getEditor());
        assertEquals("Du bois", copy.getMetadata().getExtendedMetadata().getDescription());
        assertEquals("Edited by Commons Image IO", copy.getMetadata().getExtendedMetadata().getEditor());

        assertEquals("Creative Commons", copy.getMetadata().getCopyright());
        assertEquals("Creative Commons", copy.getMetadata().getExtendedMetadata().getCopyright());
        assertEquals("CC-SA-NC", copy.getMetadata().getExtendedMetadata().getUsage());

        assertEquals("Foo", copy.getMetadata().getHeadLine());
        assertEquals("FooTitle", copy.getMetadata().getTitle());
        assertEquals("Foo", copy.getMetadata().getExtendedMetadata().getSynopsis());
        assertEquals("FooTitle", copy.getMetadata().getExtendedMetadata().getTitle());

        assertEquals("02112011", copy.getMetadata().getIPTCCreationDate());
        assertEquals("02112011", copy.getMetadata().getExtendedMetadata().getCreationDate());

        assertTrue(copy.getMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getKeywords().contains("noir"));

        assertTrue(copy.getMetadata().getExtendedMetadata().getKeywords().contains("test"));
        assertFalse(copy.getMetadata().getExtendedMetadata().getKeywords().contains("noir"));
    }

    /**
     * Checks that we're copying XMP when copying a PNG file.
     * @throws IOException
     */
    @Test
    public void testCopyingPNGContainingXMP() throws IOException {
        File file = new File("src/test/resources/png/XMP-Logo-with-XMP_Metadata.png");
        Image origin = new Image(file);
        File out = new File(dir, "testCopyingPNGContainingXMP.png");
        origin.write(out);

        Image copy = new Image(out);
        assertNotNull(copy.getMetadata().getXmp());
        // Bad manipulation when inserting the metadata, only one keyword...
        assertTrue(copy.getMetadata().getKeywords().contains("XMP,logo"));
        assertEquals("XMP", copy.getMetadata().getExtendedMetadata().getAuthor());
    }

    /**
     * Checks that XMP are correctly written after an update.
     * @throws IOException
     */
    @Test
    public void testEditingPNGContainingXMP() throws IOException {
        File file = new File("src/test/resources/png/XMP-Logo-with-XMP_Metadata.png");
        Image origin = new Image(file);
        origin.getMetadata().getExtendedMetadata().setDescription("XMP Logo");
        File out = new File(dir, "testCopyingPNGContainingXMP.png");
        origin.write(out);

        Image copy = new Image(out);
        assertNotNull(copy.getMetadata().getXmp());
        // Bad manipulation when inserting the metadata, only one keyword...
        assertTrue(copy.getMetadata().getKeywords().contains("XMP,logo"));
        assertEquals("XMP", copy.getMetadata().getExtendedMetadata().getAuthor());
        assertEquals("XMP Logo", copy.getMetadata().getExtendedMetadata().getDescription());
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
        System.out.println(out.getAbsolutePath());
        origin.write(out);

        origin = new Image(out);

        assertTrue(origin.getMetadata().getKeywords().contains("Flore"));
        assertTrue(origin.getMetadata().getKeywords().contains("fauteuil"));
        assertTrue(origin.getMetadata().getKeywords().contains("a_test"));
    }


    /**
     * Example of usage of the Sanselan API to write XMP metadata.
     * @throws Exception
     */
    @Test
    public void testSanselanXMPWriting() throws Exception {
        File file = new File("src/test/resources/png/pipe.png");
        File out = File.createTempFile("pipe_out", ".png");

        String xmpXml = new PngImageParser().getXmpXml(new ByteSourceFile(file), null);
        PngWriter writer = new PngWriter(true);

        Map<String, String> params = new HashMap<String, String>();
        params.put(PngConstants.PARAM_KEY_XMP_XML, xmpXml);
        writer.writeImage(new PngImageParser().getBufferedImage(new ByteSourceFile(file), new HashMap()), new FileOutputStream(out), params);

        String xmp = new PngImageParser().getXmpXml(new ByteSourceFile(out), params);
        Assert.assertEquals(xmp, xmpXml);
    }

}
