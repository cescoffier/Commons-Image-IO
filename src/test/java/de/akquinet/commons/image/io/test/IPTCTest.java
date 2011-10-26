package de.akquinet.commons.image.io.test;


import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.jpeg.iptc.JpegIptcRewriter;
import org.apache.sanselan.formats.jpeg.xmp.JpegRewriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class IPTCTest {

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
            for (int j = 0; j < oldRecords.size(); j++)
            {
                IPTCRecord record = (IPTCRecord) oldRecords.get(j);
                if (record.iptcType.type != IPTCConstants.IPTC_TYPE_CITY.type)
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
           String xmpXml = new JpegImageParser().getXmpXml(new ByteSourceFile(file), params );
            System.out.println(xmpXml);
    }

}
