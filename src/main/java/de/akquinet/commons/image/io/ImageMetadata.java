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

public class ImageMetadata {

    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_UNKNOWN = -2;

    public enum ColorType {
        BLACK_WHITE,
        GRAYSCALE,
        RGB,
        CMYK,
        OTHER,
        UNKNOWN;

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

    public enum Orientation {
        PORTRAIT,
        LANDSCAPE,
        UNKNOWN;

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

    public class Localization {
        public final String latitudeRef;
        public final String longitudeRef;

        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final RationalNumber longitudeSeconds;

        public Localization(final String latitudeRef, final String longitudeRef,
                final RationalNumber latitudeDegrees,
                final RationalNumber latitudeMinutes,
                final RationalNumber latitudeSeconds,
                final RationalNumber longitudeDegrees,
                final RationalNumber longitudeMinutes,
                final RationalNumber longitudeSeconds)
        {
            this.latitudeRef = latitudeRef;
            this.longitudeRef = longitudeRef;
            this.latitudeDegrees = latitudeDegrees;
            this.latitudeMinutes = latitudeMinutes;
            this.latitudeSeconds = latitudeSeconds;
            this.longitudeDegrees = longitudeDegrees;
            this.longitudeMinutes = longitudeMinutes;
            this.longitudeSeconds = longitudeSeconds;
        }

        public Localization(GPSInfo info)
        {
            this.latitudeRef = info.latitudeRef;
            this.longitudeRef = info.longitudeRef;
            this.latitudeDegrees = info.latitudeDegrees;
            this.latitudeMinutes = info.latitudeMinutes;
            this.latitudeSeconds = info.latitudeSeconds;
            this.longitudeDegrees = info.longitudeDegrees;
            this.longitudeMinutes = info.longitudeMinutes;
            this.longitudeSeconds = info.longitudeSeconds;
        }

        public String toString()
        {
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

        public double getLongitudeAsDegreesEast() throws ImageReadException
        {
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

        public double getLatitudeAsDegreesNorth() throws ImageReadException
        {
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

    private final Localization m_localization;

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
                    m_localization = new Localization(gps);
                } else {
                    m_localization = null;
                }
            } else {
                m_localization = null;
            }

        } else {
            m_localization = null;
        }

        System.out.println(m_metadata);
    }

    public ImageMetadata(byte[] bytes) throws IOException {
        this(new Image(bytes));
    }

    public ImageMetadata(File file) throws IOException {
        this(new Image(file));
    }

    public int getHeight() {
        return m_height;
    }

    public int getWidth() {
        return m_width;
    }

    public Format getFormat() {
        return m_format;
    }

    public String getFormatName() {
        return m_formatName;
    }

    public String getFormatDetails() {
        return m_formatDetails;
    }

    public Algorithm getAlgorithm() {
        return m_algorithm;
    }

    public int getBitsPerPixel() {
        return m_bitsPerPixel;
    }

    public ColorType getColorType() {
        return m_colorType;
    }

    public int getDpiHeight() {
        return m_dpiHeight;
    }

    public int getDpiWidth() {
        return m_dpiWidth;
    }

    public boolean isTransparent() {
        return m_isTransparent;
    }

    public boolean isProgressive() {
        return m_isProgressive;
    }

    public int getNumberOfImages() {
        return m_numberOfImages;
    }

    public boolean usesPalette() {
        return m_usesPalette;
    }

    public Map<String, String> getMetadata() {
        return new HashMap<String,String>(m_metadata);
    }

    public String getMake() {
        String m = m_metadata.get("Make");
        if (m != null) {
            return removeQuotes(m);
        }
        return null;
    }

    public String getModel() {
        String m = m_metadata.get("Model");
        if (m != null) {
            return removeQuotes(m);
        }
        return null;
    }

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
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String removeQuotes(String d) {
        if (d.startsWith("'")) {
            d = d.substring(1);
        }

        if (d.endsWith("'")) {
            d = d.substring(0, d.length() -1);
        }
        return d;
    }

    public Orientation getOrientation() {
        String or = m_metadata.get("Orientation");
        if (or == null) {
            return Orientation.UNKNOWN;
        } else {
            return Orientation.getOrientationFromExif(Integer.parseInt(or));
        }
    }

    public int getExifOrientation() {
        return Integer.parseInt(m_metadata.get("Orientation"));
    }

    public Localization getLocalization() {
        return m_localization;
    }


}
