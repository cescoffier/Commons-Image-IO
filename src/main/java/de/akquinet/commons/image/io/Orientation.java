package de.akquinet.commons.image.io;

/**
 * The different orientations.
 * The {@link Orientation#UNKNOWN} value is used when the extraction
 * is not possible, or if the orientation is not directly mappable to
 * portrait or landscape. Orientation is an IPTC metadata or computed from
 * the height x width ratio.
 * We can't use the EXIF metadata because if the image it then cropped or
 * rotated using software the EXIF orientation is not modified
 */
public enum Orientation {
    PORTRAIT,
    LANDSCAPE,
    SQUARE,
    UNKNOWN;

    /**
     * Gets the {@link Orientation} enumerated value from the image dimension
     *
     * @param width the image width
     * @param height the image height
     * @return the orientation
     */
    public static Orientation getOrientationFromDimension(int width, int height) {
        if (width > height) {
            return LANDSCAPE;
        } else if (height > width) {
            return PORTRAIT;
        } else {
            return SQUARE;
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
