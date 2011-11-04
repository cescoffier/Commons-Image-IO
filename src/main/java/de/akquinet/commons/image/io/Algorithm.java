package de.akquinet.commons.image.io;

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
     *
     * @param name the algorithm name from Sanselan
     * @return the matching {@link Algorithm}, {@link Algorithm#UNKNOWN}
     *         if no matching algorithm found
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

}

