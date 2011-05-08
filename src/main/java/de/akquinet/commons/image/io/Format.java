package de.akquinet.commons.image.io;

public enum Format {

    JPEG,
    PNG,
    BMP,
    GIF;

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
            return null;
        }
    }

}
