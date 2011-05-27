package de.akquinet.commons.image.io;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffImageMetadata.GPSInfo;

/**
 * Classes representing image metadata. This class is also responsible
 * of the extraction of the metadata. The extracted metadata depends from
 * the type of images (Format) and the source of the image (file or others).
 * Pictures can contain different set of metadata.
 *
 * This class extracts both image info and EXIF metadata is available.
 */
public class ImageMetadata {

    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_UNKNOWN = -2;

    /**
     * The different color types. The {@link ColorType#UNKNOWN}
     * value is used when the extraction is not possible.
     */
    public enum ColorType {
        BLACK_WHITE,
        GRAYSCALE,
        RGB,
        CMYK,
        OTHER,
        UNKNOWN;

        /**
         * Gets the {@link ColorType} enumerated value from the
         * given integer from Sanselan.
         * @param type the color type from Sanselan.
         * @return the associated {@link ColorType}, {@link ColorType#UNKNOWN}
         * if there is no matching {@link ColorType} for the given integer.
         */
        public static ColorType getColorType(int type) {
            switch (type) {
            case COLOR_TYPE_BW: return BLACK_WHITE;
            case COLOR_TYPE_GRAYSCALE: return GRAYSCALE;
            case COLOR_TYPE_RGB: return RGB;
            case COLOR_TYPE_CMYK: return CMYK;
            case COLOR_TYPE_OTHER: return OTHER;
            default: return UNKNOWN;
            }
        }
    }

    /**
     * The different orientations.
     * The {@link Orientation#UNKNOWN} value is used when the extraction
     * is not possible, or if the orientation is not directly mappable to
     * portrait or landscape. Orientation is an EXIF metadata.
     */
    public enum Orientation {
        PORTRAIT,
        LANDSCAPE,
        UNKNOWN;

        /**
         * Gets the {@link Orientation} enumerated value for the EXIF orientation
         * (integer from 1 to 8).
         * @param orientation the EXIF Orientation
         * @return
         */
        public static Orientation getOrientationFromExif(int orientation) {
            switch (orientation) {
            case 1:
            case 3:
                return LANDSCAPE;
            case 8:
            case 6:
                return PORTRAIT;
            default:
                return UNKNOWN;
            }
        }
    }

    /**
     * Compression Algorithm.
     * {@link Algorithm#UNKNOWN} is used when the algorithm is unknown
     * or cannot be extracted.
     */
    public enum Algorithm {
        UNKNOWN,
        NONE,
        LZW,
        PACKBITS,
        JPEG,
        RLE,
        PSD,
        PNG,
        CCITT_GROUP_3,
        CCITT_GROUP_4,
        CCITT_1D;

        /**
         * Gets the {@link Algorithm} enumerated values for the
         * algorithm name returned by Sanselan.
         * @param name the algorithm name from Sanselan
         * @return the matching {@link Algorithm}, {@link Algorithm#UNKNOWN}
         * if no matching algorithm found
         */
        public static Algorithm getAlgorithm(String name) {
            if (name == null) {
                return UNKNOWN;
            } else if (COMPRESSION_ALGORITHM_CCITT_1D.equals(name)) {
                return CCITT_1D;
            } else if (COMPRESSION_ALGORITHM_CCITT_GROUP_3.equals(name)) {
                return CCITT_GROUP_3;
            } else if (COMPRESSION_ALGORITHM_CCITT_GROUP_4.equals(name)) {
                return CCITT_GROUP_4;
            } else if (COMPRESSION_ALGORITHM_JPEG.equals(name)) {
                return JPEG;
            } else if (COMPRESSION_ALGORITHM_LZW.equals(name)) {
                return LZW;
            } else if (COMPRESSION_ALGORITHM_NONE.equals(name)) {
                return NONE;
            } else if (COMPRESSION_ALGORITHM_PACKBITS.equals(name)) {
                return PACKBITS;
            } else if (COMPRESSION_ALGORITHM_PNG_FILTER.equals(name)) {
                return PNG;
            } else if (COMPRESSION_ALGORITHM_PSD.equals(name)) {
                return PSD;
            } else if (COMPRESSION_ALGORITHM_RLE.equals(name)) {
                return RLE;
            } else {
                return UNKNOWN;
            }
        }
    }

    public static final String COMPRESSION_ALGORITHM_UNKNOWN = "Unknown";
    public static final String COMPRESSION_ALGORITHM_NONE = "None";
    public static final String COMPRESSION_ALGORITHM_LZW = "LZW";
    public static final String COMPRESSION_ALGORITHM_PACKBITS = "PackBits";
    public static final String COMPRESSION_ALGORITHM_JPEG = "JPEG";
    public static final String COMPRESSION_ALGORITHM_RLE = "RLE: Run-Length Encoding";
    public static final String COMPRESSION_ALGORITHM_PSD = "Photoshop";
    public static final String COMPRESSION_ALGORITHM_PNG_FILTER = "PNG Filter";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_3 = "CCITT Group 3 1-Dimensional Modified Huffman run-length encoding.";
    public static final String COMPRESSION_ALGORITHM_CCITT_GROUP_4 = "CCITT Group 4";
    public static final String COMPRESSION_ALGORITHM_CCITT_1D = "CCITT 1D";

    /**
     * Class representing the location.
     * Location metadata are extracted from JPEG file containing
     * EXIF metadata.
     * TODO Remove the reference to Sanselan class
     */
    public class Location {
        public final String latitudeRef;
        public final String longitudeRef;

        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final RationalNumber longitudeSeconds;

        public Location(final String latitudeRef, final String longitudeRef,
                final RationalNumber latitudeDegrees,
                final RationalNumber latitudeMinutes,
                final RationalNumber latitudeSeconds,
                final RationalNumber longitudeDegrees,
                final RationalNumber longitudeMinutes,
                final RationalNumber longitudeSeconds) {
            this.latitudeRef = latitudeRef;
            this.longitudeRef = longitudeRef;
            this.latitudeDegrees = latitudeDegrees;
            this.latitudeMinutes = latitudeMinutes;
            this.latitudeSeconds = latitudeSeconds;
            this.longitudeDegrees = longitudeDegrees;
            this.longitudeMinutes = longitudeMinutes;
            this.longitudeSeconds = longitudeSeconds;
        }

        public Location(GPSInfo info) {
            this.latitudeRef = info.latitudeRef;
            this.longitudeRef = info.longitudeRef;
            this.latitudeDegrees = info.latitudeDegrees;
            this.latitudeMinutes = info.latitudeMinutes;
            this.latitudeSeconds = info.latitudeSeconds;
            this.longitudeDegrees = info.longitudeDegrees;
            this.longitudeMinutes = info.longitudeMinutes;
            this.longitudeSeconds = info.longitudeSeconds;
        }

        public String toString() {
            // This will format the gps info like so:
            //
            // latitude: 8 degrees, 40 minutes, 42.2 seconds S
            // longitude: 115 degrees, 26 minutes, 21.8 seconds E

            StringBuffer result = new StringBuffer();
            result.append("[GPS. ");
            result.append("Latitude: " + latitudeDegrees.toDisplayString()
                    + " degrees, " + latitudeMinutes.toDisplayString()
                    + " minutes, " + latitudeSeconds.toDisplayString()
                    + " seconds " + latitudeRef);
            result.append(", Longitude: " + longitudeDegrees.toDisplayString()
                    + " degrees, " + longitudeMinutes.toDisplayString()
                    + " minutes, " + longitudeSeconds.toDisplayString()
                    + " seconds " + longitudeRef);
            result.append("]");

            return result.toString();
        }

        public double getLongitudeAsDegreesEast() throws ImageReadException {
            double result = longitudeDegrees.doubleValue()
                    + (longitudeMinutes.doubleValue() / 60.0)
                    + (longitudeSeconds.doubleValue() / 3600.0);

            if (longitudeRef.trim().equalsIgnoreCase("e"))
                return result;
            else if (longitudeRef.trim().equalsIgnoreCase("w"))
                return -result;
            else
                throw new ImageReadException("Unknown longitude ref: \""
                        + longitudeRef + "\"");
        }

        public double getLatitudeAsDegreesNorth() throws ImageReadException {
            double result = latitudeDegrees.doubleValue()
                    + (latitudeMinutes.doubleValue() / 60.0)
                    + (latitudeSeconds.doubleValue() / 3600.0);

            if (latitudeRef.trim().equalsIgnoreCase("n"))
                return result;
            else if (latitudeRef.trim().equalsIgnoreCase("s"))
                return -result;
            else
                throw new ImageReadException("Unknown latitude ref: \""
                        + latitudeRef + "\"");
        }

    }


    private final int m_height;

    private final int m_width;

    private final Format m_format;

    private final String m_formatDetails;

    private final String m_formatName;

    private final Algorithm m_algorithm;

    private final int m_bitsPerPixel;

    private final ColorType m_colorType;

    private final int m_dpiHeight;

    private final int m_dpiWidth;

    private final boolean m_isTransparent;

    private final boolean m_isProgressive;

    private final int m_numberOfImages;

    private final boolean m_usesPalette;

    private final Map<String, String> m_metadata = new HashMap<String, String>();

    private final Location m_location;

    /**
     * Creates a ImageMetadata for the given Image.
     * This constructor extracts image info and if the format is eligible
     * tries to extract the EXIF metadata
     * @param image the image from where metadata are extracted
     * @throws IOException if metadata cannot be extracted
     */
    public ImageMetadata(Image image) throws IOException {
        ImageInfo info = null;
        IImageMetadata metadata = null;

        try {
            if (image.getFile() == null  || ! image.getFile().exists()) {
                byte[] bytes = image.getBytes();
                info = Sanselan.getImageInfo(bytes);
                metadata = Sanselan.getMetadata(bytes);
            } else {
                info = Sanselan.getImageInfo(image.getFile());
                metadata = Sanselan.getMetadata(image.getFile());
            }
        } catch (ImageReadException e) {
            throw new IOException(e);
        }

        m_format = image.getFormat();
        m_width = image.getBufferedImage().getWidth();
        m_height = image.getBufferedImage().getHeight();

        // Extract Image Info
        m_formatName = info.getFormatName();
        m_formatDetails = info.getFormatDetails();

        m_algorithm = Algorithm.getAlgorithm(info.getCompressionAlgorithm());
        m_bitsPerPixel = info.getBitsPerPixel();
        m_colorType = ColorType.getColorType(info.getColorType());

        m_dpiHeight = info.getPhysicalHeightDpi();
        m_dpiWidth = info.getPhysicalWidthDpi();

        m_isTransparent = info.isTransparent();
        m_isProgressive = info.isProgressive();

        m_usesPalette = info.usesPalette();

        m_numberOfImages = info.getNumberOfImages();

        if (metadata != null) {
            @SuppressWarnings("unchecked")
            List<Item> items = metadata.getItems();
            for (Item i : items) {
                m_metadata.put(i.getKeyword(), i.getText());
            }

            if (metadata instanceof JpegImageMetadata) {
                GPSInfo gps = null;
                try {
                    gps = ((JpegImageMetadata) metadata).getExif().getGPS();
                } catch (ImageReadException e) {
                    // ignore.
                }
                if (gps != null) {
                    m_location = new Location(gps);
                } else {
                    m_location = null;
                }
            } else {
                m_location = null;
            }

        } else {
            m_location = null;
        }
    }

    /**
     * Creates a ImageMetadata from a byte array. Exif metadata won't be
     * extracted.
     * @param bytes the byte array
     * @throws IOException if the metadata cannot be extracted
     */
    public ImageMetadata(byte[] bytes) throws IOException {
        this(new Image(bytes));
    }

    /**
     * Creates a ImageMetadata from the given file.
     * @param file the file
     * @throws IOException if the metadata cannot be extracted
     */
    public ImageMetadata(File file) throws IOException {
        this(new Image(file));
    }

    /**
     * Gets the image height in pixels.
     * @return the height
     * @see Image#getHeight()
     */
    public int getHeight() {
        return m_height;
    }

    /**
     * Gets the image width in pixels
     * @return the width
     * @see Image#getWidth()
     */
    public int getWidth() {
        return m_width;
    }

    /**
     * Gets the image format.
     * @return the image format
     * @see Image#getFormat()
     */
    public Format getFormat() {
        return m_format;
    }

    /**
     * Gets the format name.
     * @return the format name
     */
    public String getFormatName() {
        return m_formatName;
    }

    /**
     * Gets format details.
     * @return the format details
     */
    public String getFormatDetails() {
        return m_formatDetails;
    }

    /**
     * Gets the compression algorithm
     * @return the algorithm
     */
    public Algorithm getAlgorithm() {
        return m_algorithm;
    }

    /**
     * Gets the number of bits used to encode one pixel
     * @return the number of bits per pixels
     */
    public int getBitsPerPixel() {
        return m_bitsPerPixel;
    }

    /**
     * Gets the color type of the picture
     * @return the color type
     */
    public ColorType getColorType() {
        return m_colorType;
    }

    /**
     * Gets the number of Dot Per Inch for the image height.
     * @return the DPI for the height
     */
    public int getDpiHeight() {
        return m_dpiHeight;
    }

    /**
     * Gets the number of Dot Per Inch for the image width.
     * @return the DPI for the width
     */
    public int getDpiWidth() {
        return m_dpiWidth;
    }

    /**
     * Does the pictures contains transparent pixels, i.e.
     * alpha colors.
     * @return <code>true</code> if the image contains transparent pixels,
     * <code>false</code> otherwise
     */
    public boolean isTransparent() {
        return m_isTransparent;
    }

    /**
     * Does the picture is interlaced or not.
     * @return <code>true</code> if the image support progressive loading,
     * <code>false</code> otherwise
     */
    public boolean isProgressive() {
        return m_isProgressive;
    }

    /**
     * Gets the number of images contained in the picture.
     * This not not count the EXIF thumbnails.
     * @return the number of images
     */
    public int getNumberOfImages() {
        return m_numberOfImages;
    }

    /**
     * Does the image use a custom palette.
     * @return <code>true</code> if the image uses a custom palette,
     * <code>false</code> otherwise
     */
    public boolean usesPalette() {
        return m_usesPalette;
    }

    /**
     * Gets EXIF metadata if any.
     * @return a copy of the image metadata. An empty {@link Map} is
     * returned if no EXIF metadata were extracted
     */
    public Map<String, String> getExifMetadata() {
        return new HashMap<String,String>(m_metadata);
    }

    /**
     * Gets the camera make.
     * @return the camera make, <code>null</code> if not contained
     */
    public String getMake() {
        String m = m_metadata.get("Make");
        if (m != null) {
            return removeQuotes(m);
        }
        return null;
    }

    /**
     * Gets the camera model.
     * @return the camera model, <code>null</code> if not contained
     */
    public String getModel() {
        String m = m_metadata.get("Model");
        if (m != null) {
            return removeQuotes(m);
        }
        return null;
    }

    /**
     * Gets the creation date.
     * This methods checks the <code>create date</code> EXIF key. If this key is
     * not present, the <code>modify date</code> key is checked.
     * @return the creation date, <code>null</code> if not contained
     * or if the date cannot be parsed.
     */
    public Date getCreationDate() {
        String d = m_metadata.get("Create Date");

        if (d == null) {
            d = m_metadata.get("Modify Date");
        }

        if (d == null) {
            return null;
        } else {
            d = removeQuotes(d);

            SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                return format.parse(d);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * Utility methods removing quotes around EXIF metadata.
     * @param d the EXIF String
     * @return the given input without the first and last quote is present
     */
    public static String removeQuotes(String d) {
        if (d.startsWith("'")) {
            d = d.substring(1);
        }

        if (d.endsWith("'")) {
            d = d.substring(0, d.length() -1);
        }
        return d;
    }

    /**
     * Gets the picture orientation.
     * @return the orientation, {@link Orientation#UNKNOWN} is returned
     * if the orientation was not extracted.
     */
    public Orientation getOrientation() {
        String or = m_metadata.get("Orientation");
        if (or == null) {
            return Orientation.UNKNOWN;
        } else {
            return Orientation.getOrientationFromExif(Integer.parseInt(or));
        }
    }

    /**
     * Gets the the picture orientation. This methods gets the EXIF orientation
     * code.
     * @return the orientation EXIF code, or <code>-1</code> if the orientation
     * was not extracted
     * @see ImageMetadata#getOrientation()
     */
    public int getExifOrientation() {
        if (m_metadata.containsKey("Orientation")) {
            return Integer.parseInt(m_metadata.get("Orientation"));
        } else {
            return -1;
        }
    }

    /**
     * Gets the location metadata.
     * @return the location or <code>null</code> if the location was not contained
     * in the metadata
     */
    public Location getLocation() {
        return m_location;
    }

}
