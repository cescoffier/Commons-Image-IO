package de.akquinet.commons.image.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class Image {

    private final BufferedImage m_bufferedImage;

    private final Format m_format;

    private final File m_file;

    private ImageMetadata m_metadata;


    public Image(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("Cannot read image : the input is null");
        }
        byte[] bytes = IOUtils.toByteArray(is);
        m_bufferedImage = ImageIOUtils.getIOHelper().read(bytes);
        m_format = ImageIOUtils.getIOHelper().getFormat(bytes);

        m_file = null;
    }

    public Image(File file) throws IOException {
        m_bufferedImage = ImageIOUtils.getIOHelper().read(file);
        m_format = ImageIOUtils.getIOHelper().getFormat(file);

        m_file = file;
    }

    public Image(byte[] bytes) throws IOException {
        m_bufferedImage = ImageIOUtils.getIOHelper().read(bytes);
        m_format = ImageIOUtils.getIOHelper().getFormat(bytes);

        m_file = null;
    }

    public BufferedImage getBufferedImage() {
        return m_bufferedImage;
    }

    public Format getFormat() {
        return m_format;
    }

    public byte[] getBytes() throws IOException {
        return getBytes(m_format);
    }

    public byte[] getBytes(Format format) throws IOException {
        return ImageIOUtils.getIOHelper().getBytes(m_bufferedImage, format);
    }

    public void write(File out) throws IOException {
        ImageIOUtils.getIOHelper().write(m_bufferedImage, out, m_format);
    }

    public void write(OutputStream out) throws IOException {
        write(out, m_format);
    }

    public void write(File out, Format format) throws IOException {
        ImageIOUtils.getIOHelper().write(m_bufferedImage, out, format);
    }

    public void write(OutputStream out, Format format) throws IOException {
        byte[] bytes = ImageIOUtils.getIOHelper().getBytes(m_bufferedImage, format);
        out.write(bytes);
    }

    public int getWidth() {
        return m_bufferedImage.getWidth();
    }

    public int getHeight() {
        return m_bufferedImage.getHeight();
    }

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

    public void scale(float ratio) {
        ImageIOUtils.getScaleHelper().scale(m_bufferedImage, ratio);
    }

    public File getFile() {
        return m_file;
    }
}
