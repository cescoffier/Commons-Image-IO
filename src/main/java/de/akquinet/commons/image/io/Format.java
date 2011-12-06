package de.akquinet.commons.image.io;

import org.apache.sanselan.ImageFormat;

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
     */
    public static ImageFormat getSanselanImageFormat(Format format) {
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

}
