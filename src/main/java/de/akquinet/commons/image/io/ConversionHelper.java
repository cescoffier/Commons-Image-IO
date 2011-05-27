package de.akquinet.commons.image.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * Conversion Helper.
 * This helper allows converting images to JPEG and PNG.
 */
public class ConversionHelper {

    /**
     * Converts the given {@link BufferedImage} to JPEG using
     * the specified quality.
     * @param image the buffered image to convert
     * @param compressionQuality the quality
     * @return the byte array containing the converted image
     * @throws IOException if the image cannot be converted correctly.
     */
    public byte[] convertImageToJPEGBytes(BufferedImage image,
            float compressionQuality) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg")
                .next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwparam.setCompressionQuality(compressionQuality);
        writer.write(null, new IIOImage(image, null, null), iwparam);
        ios.flush();
        byte[] imageBytes = out.toByteArray();
        return imageBytes;
    }

    /**
     * Converts the given image to PNG. If the initial image do not contain
     * alpha pixel the resulting image won't.
     * @param image the input image
     * @return the byte array containing the converted image
     * @throws IOException if the image cannot be converted correctly.
     */
    public byte[] convertImageToPNGBytes(BufferedImage image)
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }

}
