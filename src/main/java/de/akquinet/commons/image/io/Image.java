package de.akquinet.commons.image.io;

import java.awt.image.BufferedImage;
import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;

/**
 * The {@link Image} class represents pictures and provides
 * basics action to extract metadata, IO and manipulation.
 */
public class Image {

    /**
     * The format of the image.
     */
    private final Format m_format;

    /**
     * If the image is read from a file, the file,
     * <code>null</code> otherwise.
     */
    private final File m_file;

    /**
     * If the image is loaded from a byte[] or from an input stream, the read bytes,
     * <code>null</code> otherwise.
     */
    private byte[] m_bytes;

    /**
     * The extracted metadata (cached to avoid heavy computation).
     */
    private ImageMetadata m_metadata;

    /**
     * The wrapped buffered image
     */
    private BufferedImage m_bufferedImage;

    /**
     * Creates a Image from the given {@link BufferedImage} and {@link Format}.
     * @param img the buffered image
     * @param format the initial format
     */
    public Image(BufferedImage img, Format format) {
        if (img == null) {
            throw new IllegalArgumentException(
                    "Cannot read image : the buffered image cannot be null");
        }

        if (format == null) {
            throw new IllegalArgumentException(
                    "Cannot read image : the format cannot be null");
        }

        m_bufferedImage = img;
        m_format = format;
        m_file = null;
        m_bytes = null;

    }

    /**
     * Creates a Image from the given input stream.
     * The input stream is closed by this method.
     * @param is the input stream
     * @throws IOException if the stream cannot be read.
     */
    public Image(InputStream is) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException(
                    "Cannot read image : the input is null");
        }
        m_bytes = IOUtils.toByteArray(is);
        IOHelper.closeQuietly(is);
        m_bufferedImage = ImageIOUtils.getIOHelper().read(m_bytes);
        m_format = ImageIOUtils.getIOHelper().getFormat(m_bytes);
        m_file = null;
    }

    /**
     * Creates a Image by reading the given file.
     * @param file the file to read
     * @throws IOException if the file cannot be read correctly
     */
    public Image(File file) throws IOException {
        if (file == null  || ! file.exists()) {
            throw new IllegalArgumentException(
                    "Cannot read image : the file is null" +
                    " or does not exist : " + file);
        }
        m_bufferedImage = ImageIOUtils.getIOHelper().read(file);
        m_format = ImageIOUtils.getIOHelper().getFormat(file);
        m_file = file;
        m_bytes = null;
    }

    /**
     * Creates a Image from the given byte array
     * @param bytes the byte array containing the image
     * @throws IOException if the byte array is not a picture
     */
    public Image(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException(
                    "Cannot read image : the byte array is null" +
                    " or empty");
        }
        m_bufferedImage = ImageIOUtils.getIOHelper().read(bytes);
        m_format = ImageIOUtils.getIOHelper().getFormat(bytes);
        m_bytes = bytes;
        m_file = null;
    }

    /**
     * Gets the wrapped {@link BufferedImage}. This method returns
     * always the same {@link BufferedImage} object. Cloning must be done
     * on the caller side.
     * @return the wrapped {@link BufferedImage} cannot be <code>null</code>
     */
    public synchronized BufferedImage getBufferedImage() {
        return m_bufferedImage;
    }

    /**
     * Gets the format of the image.
     * @return the image format, cannot be <code>null</code>
     */
    public Format getFormat() {
        return m_format;
    }

    /**
     * Converts the image to a byte array in the original format.
     * @return the byte array for the image in the original format.
     * @throws IOException if the image cannot be converted
     * @see Image#getBytes(Format)
     */
    public synchronized byte[] getBytes() throws IOException {
        return getBytes(m_format);
    }

    /**
     * Converts the image to a byte array using the specified format.
     * The conversion uses the default parameter. See the {@link ConversionHelper}
     * for more parameters.
     * @param format the output format
     * @return the byte array of the converted image
     * @throws IOException if the image cannot be converted
     * @see {@link ConversionHelper}
     * @see Image#getBytes()
     */
    public synchronized byte[] getBytes(Format format) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            write(out, format);
            return out.toByteArray();
        } finally {
            out.close();
        }
    }

    /**
     * Gets the byte array of the image from the buffered image.
     * This byte array does <strong>NOT</strong> contain metadata.
     * @param format the output format
     * @return the resulting byte array
     * @throws IOException if the image cannot be read
     */
    public synchronized byte[] getRawBytes(Format format) throws IOException {
        if (format == m_format && m_bytes != null) {
            return  m_bytes;
        }
        return ImageIOUtils.getIOHelper().getBytes(m_bufferedImage, format);
    }

    /**
     * Gets the byte array of the image from the buffered image.
     * This byte array does <strong>NOT</strong> contain metadata.
     * @return the resulting byte array
     * @throws IOException if the image cannot be read
     */
    public synchronized byte[] getRawBytes() throws IOException {
        return getRawBytes(m_format);
    }

    /**
     * Writes the image to the specified file using the original format.
     * @param out the output file
     * @throws IOException if the image cannot be written in the file
     * @see {@link Image#write(File, Format)}
     */
    public synchronized void write(File out) throws IOException {
        write(out, m_format);
    }

    /**
     * Writes the image to the specified {@link OutputStream} using the
     * original format.
     * @param out the {@link OutputStream} where the file is written.
     * The stream is not closed and so should be closed by the caller.
     * @throws IOException if the image cannot be written correctly.
     * @see Image#write(OutputStream, Format)
     */
    public synchronized void write(OutputStream out) throws IOException {
        write(out, m_format);
    }

    /**
     * Writes the image to the specified file using the specified format.
     * @param out the output file
     * @param format the output format
     * @throws IOException if the image cannot be written in the file
     * @see {@link Image#write(File)}
     */
    public synchronized void write(File out, Format format) throws IOException {
        // For JPEG, we use JPEGWriter because of the EXIF and IPTC metadata
        if(format == Format.JPEG) {
            JPEGWriter writer = new JPEGWriter();
            OutputStream fos = new FileOutputStream(out);
            try {
                writer.load(this);
                writer.write(fos);
            } finally {
                fos.close();
            }
        } else {
            ImageIOUtils.getIOHelper().write(m_bufferedImage, out, format);
        }
    }

    /**
     * Writes the image to the specified {@link OutputStream} using the
     * specified format.
     * @param out the {@link OutputStream} where the file is written.
     * The stream is not closed and so should be closed by the caller.
     * @param format the output format
     * @throws IOException if the image cannot be written correctly.
     * @see Image#write(OutputStream)
     */
    public synchronized void write(OutputStream out, Format format) throws IOException {
        if(format == Format.JPEG) {
            JPEGWriter writer = new JPEGWriter();
            writer.load(this);
            writer.write(out);
        } else {
            byte[] bytes = ImageIOUtils.getIOHelper().getBytes(m_bufferedImage, format);
            out.write(bytes);
        }
    }

    /**
     * Gets the width in pixels of the image.
     * @return the width of the image
     */
    public synchronized int getWidth() {
        return m_bufferedImage.getWidth();
    }

    /**
     * Gets the height in pixels of the image.
     * @return the height of the image
     */
    public synchronized int getHeight() {
        return m_bufferedImage.getHeight();
    }

    /**
     * Gets the {@link ImageMetadata} of the image.
     * The image metadata are extracted and then cached to
     * avoid heavy computation every time.
     * @return the {@link ImageMetadata} extracted from the image.
     */
    public synchronized ImageMetadata getMetadata() {
        if (m_metadata == null) {
            try {
                m_metadata = new ImageMetadata(this);
            } catch (IOException e) {
                // TODO Log.
                e.printStackTrace();
            }
        }
        return m_metadata;
    }

    /**
     * Scales the image.
     * This method is default parameter and bilinear scaling.
     * For further configuration, see the {@link ScaleHelper}.
     *
     * This method invalidates the extracted metadata.
     *
     * @param ratio the ratio used to scale the image.
     */
    public synchronized void scale(float ratio) {
        m_bufferedImage = ImageIOUtils.getScaleHelper().scale(m_bufferedImage, ratio);
        m_metadata = null; // Must be recomputed.
    }

    /**
     * Rotates the image.
     *
     * This method invalidates the extracted metadata.
     *
     * @param angle the rotation angle in degree.
     */
    public synchronized void rotate(int angle) {
        m_bufferedImage = ImageIOUtils.getManipulationHelper().rotate(m_bufferedImage, angle);
        m_metadata = null; // Must be recomputed.
    }

    /**
     * Gets the source file of the image if any.
     * @return the source file, <code>null</code> if the {@link Image}
     * object was not created from a {@link File}
     * @see Image#Image(File)
     */
    public File getFile() {
        return m_file;
    }
}
