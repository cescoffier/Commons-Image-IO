package de.akquinet.commons.image.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * Helper class to read and write images.
 */
public class IOHelper {


    /**
     * Reads a {@link BufferedImage} from the input file
     * @param f the file to read
     * @return the {@link BufferedImage}
     * @throws IOException if the file is <code>null</code>, not existing,
     * or if the file is not an image
     */
    public BufferedImage read(File f) throws IOException {
        if (f == null) {
            throw new IOException("The input file is null");
        }

        return ImageIO.read(f);
    }

    /**
     * Reads a {@link BufferedImage} from the given input stream.
     * This method does not close the input stream, so must be closed by the
     * caller.
     * @param is the input stream
     * @return the {@link BufferedImage}
     * @throws IOException if the input stream is <code>null</code>, cannot be read,
     * or does not depict an image
     */
    public BufferedImage read(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("The input stream is null");
        }
        return ImageIO.read(is);
    }

    /**
     * Reads a {@link BufferedImage} from the given byte array.
     * @param bytes the bytes
     * @return the {@link BufferedImage}
     * @throws IOException if the byte array is <code>null</code>,
     * or does not depict an image
     */
    public BufferedImage read(byte[] bytes) throws IOException {
        if (bytes == null) {
            throw new IOException("Cannot convert the byte array into an image " +
                    "- the array is null");
        }
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(is);
        closeQuietly(is);
        return img;
    }

    /**
     * Gets the format of the given byte array.
     * @param bytes the bytes
     * @return the image format
     * @throws IOException if the format cannot be extracted, or is
     * not supported
     */
    public Format getFormat(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        String v = getFormatName(is);
        closeQuietly(is);
        if (v != null) {
            return Format.getFormatByExtension(v);
        } else {
            throw new IOException("Cannot extract format");
        }
    }


    /**
     * Gets the format of the given file.
     * @param file the file
     * @return the image format
     * @throws IOException if the format cannot be extracted, or is
     * not supported
     */
    public Format getFormat(File file) throws IOException {
        String v = getFormatName(file);
        if (v != null) {
            return Format.getFormatByExtension(v);
        } else {
            throw new IOException("Cannot extract format from " + file.getAbsolutePath());
        }
    }

    /**
     * Gets the format of the given input stream.
     * @param is the input stream
     * @return the image format
     * @throws IOException if the format cannot be extracted, or is
     * not supported
     * TODO Determined if the stream is closed
     */
    public Format getFormat(InputStream is) throws IOException {
        String v = getFormatName(is);
        if (v != null) {
            return Format.getFormatByExtension(v);
        } else {
            throw new IOException("Cannot extract format from input stream");
        }
    }

    /**
     * Utility method to extract the format name from the given object.
     * @param o a {@link File} or an {@link InputStream} from which the format will
     * be read
     * @return the format name, <code>null</code> if the format name cannot be read
     */
    private static String getFormatName(Object o) {
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);

            // Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }

            // Use the first reader
            ImageReader reader = (ImageReader)iter.next();

            // Close stream
            iis.close();

            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
        }
        // The image could not be read
        return null;
    }

    /**
     * Writes the given {@link BufferedImage} to the given {@link File}
     * @param image the image to write
     * @param file the file where the image will be written
     * @throws IOException if the image cannot be written
     */
    public void write(BufferedImage image, File file) throws IOException {
        int indexOf = file.getName().lastIndexOf('.');
        Format format = null;
        if (indexOf == -1 ) {
            // Use default format.
            format = Format.PNG;
        } else {
            String ext = file.getName().substring(indexOf + 1);
            format = Format.getFormatByExtension(ext);
            if (format == null) {
                throw new IOException("Not supported format : " + ext);
            }
        }

        write(image, file, format);
    }

    /**
     * Writes the given {@link BufferedImage} to the given {@link File} using the
     * specified {@link Format}
     * @param image the image to write
     * @param file the file where the image will be written
     * @param format the format used to write the file
     * @throws IOException if the image cannot be written
     */
    public void write(BufferedImage image, File file, Format format) throws IOException {
        ImageWriter writer = getWriterForFormat(format);
        if (writer != null) {
            ImageOutputStream stream = ImageIO.createImageOutputStream(file);
            writer.setOutput(stream);
            writer.write(image);
        } else {
            throw new IOException("Cannot write image - unsupported format " + format);
        }
    }

    /**
     * Gets the byte array for the given {@link BufferedImage} in the specified
     * {@link Format}
     * @param image the image
     * @param format the output format
     * @return the resulting byte array
     * @throws IOException if the image cannot be converted
     */
    public byte[] getBytes(BufferedImage image, Format format) throws IOException {
        ImageWriter writer = getWriterForFormat(format);
        if (writer != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageOutputStream stream = ImageIO.createImageOutputStream(output);
            writer.setOutput(stream);
            writer.write(image);
            closeQuietly(output);
            return output.toByteArray();
        } else {
            throw new IOException("Cannot write image - unsupported format " + format);
        }
    }

    /**
     * Gets an {@link ImageWriter} for the given {@link Format}
     * @param format the format
     * @return the matching {@link ImageWriter} or <code>null</code>
     * if no writer can be found for the given format
     */
    public ImageWriter getWriterForFormat(Format format) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format.toString());
        if (writers.hasNext()) {
            return writers.next();
        }
        return null;
    }

    /**
     * Gets an {@link ImageReader} for the given {@link Format}
     * @param format the format
     * @return the matching {@link ImageReader} or <code>null</code>
     * if no reader can be found for the given format
     */
    public ImageReader getReaderForFormat(Format format) {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format.toString());
        if (readers.hasNext()) {
            return readers.next();
        }
        return null;
    }

    /**
     * Utility methods closing the given stream without throwing exception
     * in case of error.
     * @param stream the stream
     * @return <code>true</code> if the stream was closed correctly,
     * <code>false</code> otherwise
     */
    public static boolean closeQuietly(Closeable stream) {
        try {
            stream.close();
            return true;
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    /**
     * Checks whether the given format can be read.
     * @param formatName the format
     * @return <code>true</code> if the format can be read,
     * <code>false</code> otherwise
     */
    public boolean canReadFormat(String formatName) {
        if (formatName == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName(formatName);
        return iter.hasNext();
    }

    /**
     * Checks whether the given format can be written.
     * @param formatName the format
     * @return <code>true</code> if the format can be written,
     * <code>false</code> otherwise
     */
    public boolean canWriteFormat(String formatName) {
        if (formatName == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
        return iter.hasNext();
    }

    /**
     * Checks whether the given extension can be read.
     * @param fileExt the file extension, without the <code>.</code>
     * @return <code>true</code> if the extension can be read,
     * <code>false</code> otherwise
     */
    public boolean canReadExtension(String fileExt) {
        if (fileExt == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(fileExt);
        return iter.hasNext();
    }

    /**
     * Checks whether the given extension can be written.
     * @param fileExt the file extension, without the <code>.</code>
     * @return <code>true</code> if the extension can be written,
     * <code>false</code> otherwise
     */
    public boolean canWriteExtension(String fileExt) {
        if (fileExt == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix(fileExt);
        return iter.hasNext();
    }

    /**
     * Checks whether the given mime-type can be read.
     * @param mimeType the mime type
     * @return <code>true</code> if the mime type can be read,
     * <code>false</code> otherwise
     */
    public boolean canReadMimeType(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(mimeType);
        return iter.hasNext();
    }

    /**
     * Checks whether the given mime-type can be written.
     * @param mimeType the mime type
     * @return <code>true</code> if the mime type can be written,
     * <code>false</code> otherwise
     */
    public boolean canWriteMimeType(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType(mimeType);
        return iter.hasNext();
    }

}
