package de.akquinet.commons.image.io;

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
     *
     * @param type the color type from Sanselan.
     * @return the associated {@link ColorType}, {@link ColorType#UNKNOWN}
     *         if there is no matching {@link ColorType} for the given integer.
     */
    public static ColorType getColorType(int type) {
        switch (type) {
            case COLOR_TYPE_BW:
                return BLACK_WHITE;
            case COLOR_TYPE_GRAYSCALE:
                return GRAYSCALE;
            case COLOR_TYPE_RGB:
                return RGB;
            case COLOR_TYPE_CMYK:
                return CMYK;
            case COLOR_TYPE_OTHER:
                return OTHER;
            default:
                return UNKNOWN;
        }
    }

    public static final int COLOR_TYPE_BW = 0;
    public static final int COLOR_TYPE_GRAYSCALE = 1;
    public static final int COLOR_TYPE_RGB = 2;
    public static final int COLOR_TYPE_CMYK = 3;
    public static final int COLOR_TYPE_OTHER = -1;
    public static final int COLOR_TYPE_UNKNOWN = -2;

}