package de.akquinet.commons.image.io;

/**
 * The different orientations.
 * The {@link Orientation#UNKNOWN} value is used when the extraction
 * is not possible, or if the orientation is not directly mappable to
 * portrait or landscape. Orientation is an EXIF or IPTC metadata.
 */
public enum Orientation {
    PORTRAIT,
    LANDSCAPE,
    SQUARE,
    UNKNOWN;

    /**
     * Gets the {@link Orientation} enumerated value for the EXIF orientation
     * (integer from 1 to 8).
     *
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

     /**
     * Gets the {@link Orientation} enumerated value for the IPTC orientation
     * (L, P or S).
     *
     * @param orientation the IPTC Orientation
     * @return
     */
    public static Orientation getOrientationFromIPTC(String orientation) {
        if ("L".equals(orientation)) {
            return LANDSCAPE;
        } else if ("S".equals(orientation)) {
            return SQUARE;
        } else if ("P".equals(orientation)) {
            return PORTRAIT;
        } else {
            return UNKNOWN;
        }
    }
}
