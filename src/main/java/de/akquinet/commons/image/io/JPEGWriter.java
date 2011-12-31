package de.akquinet.commons.image.io;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.iptc.IPTCBlock;
import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;
import org.apache.sanselan.formats.jpeg.iptc.IPTCParser;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.sanselan.formats.jpeg.xmp.JpegRewriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class responsible to write JPEG files including EXIF and IPTC metadata.
 * IPTC metadata are rewritten every time.
 * We can't use buffered image because EXIF metadata and IPTC are not included.
 */
public class JPEGWriter extends JpegRewriter {

    /**
     * Pieces.
     */
    private List<JFIFPiece> m_pieces;

    /**
     * The image.
     */
    private Image m_image;

    /**
     * Replaces the IPTC metadata from the image. The whole App13 segment is replaced.
     * @param newIPTC the new IPTC metadata
     * @throws IOException if the metadata cannot be replaced
     * @throws ImageWriteException if the metadata cannot be replaced
     */
    public void replaceIPTCMetadata(PhotoshopApp13Data newIPTC) throws IOException, ImageWriteException {
        List newPieces = removePhotoshopApp13Segments(m_pieces);


        // discard old iptc blocks.
        List newBlocks = newIPTC.getNonIptcBlocks();
        byte[] newBlockBytes = new IPTCParser().writeIPTCBlock(newIPTC
                .getRecords());

        int blockType = IPTCConstants.IMAGE_RESOURCE_BLOCK_IPTC_DATA;
        byte[] blockNameBytes = new byte[0];
        IPTCBlock newBlock = new IPTCBlock(blockType, blockNameBytes,
                newBlockBytes);
        newBlocks.add(newBlock);

        newIPTC = new PhotoshopApp13Data(newIPTC.getRecords(), newBlocks);

        byte segmentBytes[] = new IPTCParser()
                .writePhotoshopApp13Segment(newIPTC);
        JFIFPieceSegment newSegment = new JFIFPieceSegment(
                JPEG_APP13_Marker, segmentBytes);

        m_pieces = insertAfterLastAppSegments(newPieces, Arrays
                .asList(newSegment));

    }

    /**
     * Writes the new JPEG to the given output stream. The stream is not closed.
     * The IPTC metadata are replaced if there were modified.
     * @param fos the output stream
     * @throws IOException if the image can't be written
     */
    public void write(OutputStream fos) throws IOException {
        try {
            PhotoshopApp13Data data = m_image.getMetadata().getExtendedMetadata().getPhotoshopApp13Data();
            if (data != null) {
                replaceIPTCMetadata(data);
            }

            // Write the XMP segment too, if any
            String xmp = m_image.getMetadata().getXmp();
            if (xmp != null) {
                List newPieces = new ArrayList();
                m_pieces = removeXmpSegments(m_pieces);
                int segmentSize = Math.min(xmp.getBytes().length, MAX_SEGMENT_SIZE);
                byte segmentData[] = writeXmpSegment(m_image.getMetadata().getXmp().getBytes("utf-8"), 0, segmentSize);
                newPieces.add(new JFIFPieceSegment(JPEG_APP1_Marker, segmentData));
                m_pieces = insertAfterLastAppSegments(m_pieces, newPieces);
            }

            writeSegments(fos, (List<JFIFPiece>) m_pieces);
        } catch (ImageWriteException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Initializes the writer with the given image.
     * @param origin the image
     * @throws IOException if the image cannot be read
     */
    public void load(Image origin) throws IOException {
        File file = origin.getFile();
        ByteSource source = null;
        if (file != null  && origin.getFormat() == Format.JPEG) {
            source = new ByteSourceFile(file);
        } else {
            source = new ByteSourceArray(origin.getRawBytes(Format.JPEG));
        }
        m_image = origin;
        try {
            m_pieces = analyzeJFIF(source).pieces;
        } catch (ImageReadException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Reads a Jpeg image, replaces the XMP XML and writes the result to a
     * stream.
     *
     * @param byteSource
     *            ByteSource containing Jpeg image data.
     * @param os
     *            OutputStream to write the image to.
     * @param xmpXml
     *            String containing XMP XML.
     */
    public void updateXmpXml(ByteSource byteSource, OutputStream os,
                             String xmpXml) throws ImageReadException, IOException,
            ImageWriteException
    {
        JFIFPieces jfifPieces = analyzeJFIF(byteSource);
        List pieces = jfifPieces.pieces;
        pieces = removeXmpSegments(pieces);

        List newPieces = new ArrayList();
        byte xmpXmlBytes[] = xmpXml.getBytes("utf-8");
        int index = 0;
        while (index < xmpXmlBytes.length)
        {
            int segmentSize = Math.min(xmpXmlBytes.length, MAX_SEGMENT_SIZE);
            byte segmentData[] = writeXmpSegment(xmpXmlBytes, index,
                    segmentSize);
            newPieces.add(new JFIFPieceSegment(JPEG_APP1_Marker, segmentData));
            index += segmentSize;
        }

        pieces = insertAfterLastAppSegments(pieces, newPieces);

        writeSegments(os, pieces);
    }

    private byte[] writeXmpSegment(byte xmpXmlData[], int start, int length)
            throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(XMP_IDENTIFIER);
        os.write(xmpXmlData, start, length);

        return os.toByteArray();
    }
}
