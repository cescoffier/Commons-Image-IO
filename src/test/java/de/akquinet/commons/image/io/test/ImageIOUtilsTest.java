package de.akquinet.commons.image.io.test;

import junit.framework.Assert;

import org.junit.Test;

import de.akquinet.commons.image.io.IOHelper;
import de.akquinet.commons.image.io.ImageIOUtils;
import de.akquinet.commons.image.io.ScaleHelper;
import de.akquinet.commons.image.io.ScaleHelper.Interpolation;

public class ImageIOUtilsTest {

    @Test
    public void testGetIOHelper() {
        IOHelper helper = ImageIOUtils.getIOHelper();
        IOHelper helper2 = ImageIOUtils.getIOHelper();

        Assert.assertSame(helper, helper2);
    }

    @Test
    public void testGetFreshIOHelper() {
        IOHelper helper = ImageIOUtils.getFreshIOHelper();
        IOHelper helper2 = ImageIOUtils.getFreshIOHelper();

        Assert.assertNotSame(helper, helper2);
    }

    @Test
    public void testGetScaleHelper() {
        ScaleHelper helper = ImageIOUtils.getScaleHelper();
        ScaleHelper helper2 = ImageIOUtils.getScaleHelper();

        Assert.assertSame(helper, helper2);
    }

    @Test
    public void testGetFreshScaleHelper() {
        ScaleHelper helper = ImageIOUtils.getFreshScaleHelper(Interpolation.BICUBIC);
        ScaleHelper helper2 = ImageIOUtils.getFreshScaleHelper(Interpolation.BICUBIC);

        Assert.assertNotSame(helper, helper2);
    }

}
