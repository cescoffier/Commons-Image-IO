package de.akquinet.commons.image.io;

import org.apache.commons.imaging.ImageFormat;

import java.util.Arrays;
import java.util.List;

/**
 * List of supported formats.
 */
public enum Format {

    /**
     * JPEG
     */
    JPEG,
    /**
     * PNG
     */
    PNG,
    /**
     * BitMap
     */
    BMP,
    /**
     *  GIF
     */
    GIF,
    /**
     * Unknown format
     */
    UNKNOWN;
    
    private static final List<Format> FORMATS_SUPPORTING_EXTENDED_METADATA = Arrays.asList(JPEG, PNG);

    private static final List<Format> FORMATS_SUPPORTING_XMP = Arrays.asList(JPEG, PNG);

    private static final List<Format> FORMATS_SUPPORTING_EXIF = Arrays.asList(JPEG);

    private static final List<Format> FORMATS_SUPPORTING_IPTC = Arrays.asList(JPEG);

    /**
     * Gets the {@link Format} enum value for the given extension.
     * The extension must not be prefixed by '.'.
     * @param ext the extension
     * @return the {@link Format} enum value or <code>UNKNOWN</code>
     * if the extension is not associated with any supported format.
     */
    public static Format getFormatByExtension(String ext) {
        if (ext == null) {
            return null;
        }
        if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
            return JPEG;
        } else if (ext.toLowerCase().equals("png")) {
            return PNG;
        } else if (ext.toLowerCase().equals("bmp")) {
            return BMP;
        } else if (ext.toLowerCase().equals("gif")) {
            return GIF;
        } else {
            return UNKNOWN;
        }
    }

    /**
     * Gets the Sanselan ImageFormat object for the given Format object
     * @param format the format
     * @return the ImageFormat object
     * @deprecated Sanselan became Imaging.
     */
    public static ImageFormat getSanselanImageFormat(Format format) {
        return getImagingImageFormat(format);
    }

    /**
     * Gets the Imaging ImageFormat object for the given Format object
     * @param format the format
     * @return the ImageFormat object
     */
    public static ImageFormat getImagingImageFormat(Format format) {
        switch (format) {
            case BMP:
                return ImageFormat.IMAGE_FORMAT_BMP;
            case GIF:
                return  ImageFormat.IMAGE_FORMAT_GIF;
            case JPEG:
                return ImageFormat.IMAGE_FORMAT_JPEG;
            case PNG:
                return ImageFormat.IMAGE_FORMAT_PNG;
            case UNKNOWN:
            default:
                return ImageFormat.IMAGE_FORMAT_UNKNOWN;
        }
    }

    /**
     * Gets the mime type for the given Format object
     * @param format the format
     * @return the mime type, <code>application/octet-stream</code> for UNKNOWN and not-supported formats
     */
    public static String getMimeType(Format format) {
        switch (format) {
            case BMP:
                return "image/bmp";
            case GIF:
                return  "image/gif";
            case JPEG:
                return "image/jpeg";
            case PNG:
                return "image/png";
            case UNKNOWN:
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Checks whether the format support extended metadata (i.e. IPTC or XMP).
     * @param format the format to check
     * @return <code>true</code> if the format supports extended metadata, <code>false</code> otherwise.
     */
    public static boolean supportExtendedMetadata(Format format) {
         return FORMATS_SUPPORTING_EXTENDED_METADATA.contains(format);
    }

    /**
     * Checks whether the format support XMP metadata
     * @param format the format to check
     * @return <code>true</code> if the format supports XMP metadata, <code>false</code> otherwise.
     */
    public static boolean supportXMP(Format format) {
        return FORMATS_SUPPORTING_XMP.contains(format);
    }

    /**
     * Checks whether the format support IPTC metadata
     * @param format the format to check
     * @return <code>true</code> if the format supports IPTC metadata, <code>false</code> otherwise.
     */
    public static boolean supportIPTC(Format format) {
        return FORMATS_SUPPORTING_IPTC.contains(format);
    }

    /**
     * Checks whether the format support EXIF metadata
     * @param format the format to check
     * @return <code>true</code> if the format supports EXIF metadata, <code>false</code> otherwise.
     */
    public static boolean supportEXIF(Format format) {
        return FORMATS_SUPPORTING_EXIF.contains(format);
    }



}
