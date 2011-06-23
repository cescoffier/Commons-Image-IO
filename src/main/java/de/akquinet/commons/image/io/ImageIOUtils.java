package de.akquinet.commons.image.io;

import de.akquinet.commons.image.io.ScaleHelper.Interpolation;

/**
 * Class to instantiate helper objects.
 * Helper objects are then cached once created.
 */
public class ImageIOUtils {

    private static IOHelper m_bufferedImageHelper;
    private static ScaleHelper m_defaultScaleHelper;
    private static ConversionHelper m_defaultConverter;
    private static ManipulationHelper m_defaultManipulator;

    /**
     * Gets an {@link IOHelper} instance.
     * @return a new {@link IOHelper} instance if not already
     * created or a cached instance.
     */
    public static IOHelper getIOHelper() {
        synchronized (ImageIOUtils.class) {
            if (m_bufferedImageHelper == null) {
                m_bufferedImageHelper = getFreshIOHelper();
            }
        }
        return m_bufferedImageHelper;
    }

    /**
     * Gets a new {@link IOHelper}.
     * This method returns a fresh instance of {@link IOHelper}
     * every time.
     * @return a new {@link IOHelper} object
     */
    public static IOHelper getFreshIOHelper() {
        return new IOHelper();
    }

    /**
     * Gets an {@link ScaleHelper} instance. The instance
     * uses the bilinear interpolation algorithm for scaling.
     * @return a new {@link ScaleHelper} instance if not already
     * created or a cached instance.
     */
    public static ScaleHelper getScaleHelper() {
        synchronized (ImageIOUtils.class) {
            if (m_defaultScaleHelper == null) {
                m_defaultScaleHelper = getFreshScaleHelper(Interpolation.BILINEAR);
            }
        }
        return m_defaultScaleHelper;
    }

    /**
     * Gets a new {@link ScaleHelper} object configured to use the
     * specified interpolation algorithm.
     * @param interpolation the interpolation algorithm
     * @return a new scale helper using the specified interpolation
     * algorithm
     */
    public static ScaleHelper getFreshScaleHelper(Interpolation interpolation) {
        return new ScaleHelper(interpolation);
    }

    /**
     * Gets an {@link ConversionHelper} instance.
     * @return a new {@link ConversionHelper} instance if not already
     * created or a cached instance.
     */
    public static ConversionHelper getConversionHelper() {
        synchronized (ImageIOUtils.class) {
            if (m_defaultConverter == null) {
                m_defaultConverter = getFreshConversionHelper();
            }
        }
        return m_defaultConverter;
    }

    /**
     * Gets a new {@link ConversionHelper} object
     * @return a new conversion helper instance
     */
    public static ConversionHelper getFreshConversionHelper() {
        return new ConversionHelper();
    }

    /**
     * Gets a new {@link ManipulationHelper} object
     * @return a new manipulation helper instance
     */
    public static ManipulationHelper getFreshManipulationHelper() {
        return new ManipulationHelper();
    }

    /**
     * Gets an {@link ManipulationHelper} instance.
     * @return a new {@link ManipulationHelper} instance if not already
     * created or a cached instance.
     */
    public static ManipulationHelper getManipulationHelper() {
        synchronized (ImageIOUtils.class) {
            if (m_defaultManipulator == null) {
                m_defaultManipulator = getFreshManipulationHelper();
            }
        }
        return m_defaultManipulator;
    }

}
