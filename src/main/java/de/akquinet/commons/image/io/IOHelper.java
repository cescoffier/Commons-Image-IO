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
import javax.imageio.stream.ImageOutputStream;

public class IOHelper {


    public BufferedImage read(File f) throws IOException {
        if (f == null) {
            throw new IOException("The input file is null");
        }

        return ImageIO.read(f);
    }

    public BufferedImage read(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("The input stream is null");
        }
        return ImageIO.read(is);
    }

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

    public ImageWriter getWriterForFormat(Format format) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format.toString());
        if (writers.hasNext()) {
            return writers.next();
        }
        return null;
    }

    public ImageReader getReaderForFormat(Format format) {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format.toString());
        if (readers.hasNext()) {
            return readers.next();
        }
        return null;
    }

    public static boolean closeQuietly(Closeable stream) {
        try {
            stream.close();
            return true;
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    public boolean canReadFormat(String formatName) {
        if (formatName == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName(formatName);
        return iter.hasNext();
    }

    // Returns true if the specified format name can be written
    public boolean canWriteFormat(String formatName) {
        if (formatName == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
        return iter.hasNext();
    }

    // Returns true if the specified file extension can be read
    public boolean canReadExtension(String fileExt) {
        if (fileExt == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(fileExt);
        return iter.hasNext();
    }

    // Returns true if the specified file extension can be written
    public boolean canWriteExtension(String fileExt) {
        if (fileExt == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix(fileExt);
        return iter.hasNext();
    }

    // Returns true if the specified mime type can be read
    public boolean canReadMimeType(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(mimeType);
        return iter.hasNext();
    }

    // Returns true if the specified mime type can be written
    public boolean canWriteMimeType(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType(mimeType);
        return iter.hasNext();
    }

}
