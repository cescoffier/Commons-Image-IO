Commons Image IO
================

Commons-Image-IO is a simple library to manage image in Java.

Objectives
----------
The objective of this library is to reduce the boilerplate code required to manipulate images in Java. Despite image manipulation is supported by java using the _image.io_ extension, the proposed API is quite cumbersome. On the other side, Apache Commons Sanselan already provides a library to manipulate images, but as for image io, the API is far from being easy and a lot of features such as rotation, scaling are missing. Moreover, Sanselan is a dead project.

So the goal os this library is to reduce the complexity of manipulating images in Java by providing a high level API on the top of _image io_ and _sanselan_.

Features
--------

* Loading images from file, inputstream, byte array
* Writing and converting images
* Support of PNG, GIF and JPG images
* Allows scaling, rotations and flips
* Support metadata extractions (also support EXIF, IPTC and XMP)
* Can write IPTC and XMP metadata
* Provide a complex metadata layer
* Support the alpha layer (transparent and translucide).


Maven Dependency
----------------

    <dependency>
      <groupId>de.akquinet.commons.imageio</groupId>
      <artifactId>commons-image-io</artifactId>
      <version>${version}</version>
    </dependency>


Building the project
--------------------


    git clone git://edge.spree.de/ats-bilderverwaltung/commons-image-io.git
    cd commons-image-io
    mvn clean install

Documentation
-------------

### Loading Images

    // From File
    Image img = new Image(new File("my file.png"));

    // From input stream
    Image img2 = new Image(new FileInputStream("my file.png"));

    // From byte[]
    Image img3 = new Image(myBytes);


The library determined automatically the format of the image. It can also wraps a _BufferedImage_. However, the format must be specified:


    Image img4 = new Image(myBufferedImage, Format.PNG)


Supported formats are:

    public enum Format {
        JPEG,
        PNG,
        BMP,
        GIF;
    }

### Getting a BufferedImage


    Image img = new Image(new File("my file.png"));
    img.getBufferedImage();


The buffered image is cached. It also returns the same object.


### Writing an image

To write a image using the same format as the original image:

    Image img = new Image(new File("my file.png"));

    // To file
    img.write(new File("my new file.png"));

    // To output stream
    img.write(new FileOutputStream("my new file.png"));

You can also specified the output format:

    Image img = new Image(new File("my file.png"));

    // To file
    img.write(new File("my new file.png"), Format.JPG);

    // To output stream
    img.write(new FileOutputStream("my new file.png"), Format.BMP);

It is also possible to get the byte array of the image:

    Image img = new Image(new File("my file.png"));

    // Use the original format
    byte[] bytes = img.getBytes();

    // Use a new format
    byte[] bytes2 = img.getBytes(Format.GIF);

### Get Metadata

*Height and Width*
To get height and width of an Image, just call:

    Image img = new Image(new File("my file.png"));
    int width = img.getWidth();
    int height = img.getHeight();

*Advanced Metadata*
The library also support more advanced metadata:

    Image img = new Image(new File("my file.png"));
    ImageMetadata metadata = img.getMetadata(); // EXIT + Basic IPTC
    Extended metadata = img.getMetadata().getExtendedMetadata(); // IPTC and XMP


The Image Metadata object contains the following methods:

* getHeight() => Height in pixels
* getWidth() => Width in pixels
* getFormat() => Format of the image among PNG, JPG, BMP and GIF
* getFormatName() => Gormat name
* getFormatDetails() => Format details
* getAlgorithm() => Compression algorithm among UNKNOWN, NONE, LZW, PACKBITS, JPEG, RLE, PSD, PNG, CCITT_GROUP_3, CCITT_GROUP_4, CCITT_1D
* getBitsPerPixel() => Number of bits per pixel
* getColorType() => Color Type among BLACK_WHITE, GRAYSCALE, RGB, CMYK, OTHER, UNKNOWN
* getDpiHeight() => DPI for the height
* getDpiWidth() => DPI for the width
* isTransparent() => whether the image is transparent or translucide
* isProgressive() => whether the image support progressive loading
* getNumberOfImages() => the number of embedded images
* usesPalette() => whether the image use a custom (embedded) palette

*Exif*
If the Image is created from a File object, the library support EXIF metadata (only of JPEG images):

    Image img = new Image(new File("my file.png"));
    ImageMetadata metadata = img.getMetadata();
    // Get camera maker:
    String maker = metadata.getMake();
    // Get camera model
    String model = metadata.getModel();
    // Get the date when the picture was taken
    Date creation = getCreationDate();
    // Get the orientation of the picture among: PORTRAIT, LANDSCAPE, UNKNOWN;
    Orientation orientation = getOrientation();
    // Get the EXIF Orientation (integer from 1 to 8)
    int exifOrientation = getExifOrientation();

    // Get all EXIF data
    Map<String, String> exif = metadata.getExifMetadata();

*Geolocalization*
If the picture contains EXIF metadata, the library can extract the geolocalization data if available (pictures taken with iPHONEs contains those metadata).

    Image img = new Image(new File("my file.png"));
    ImageMetadata metadata = img.getMetadata();
    Localization localization = metadata.getLocalization();

Localization objects contains:

    public final String latitudeRef;
    public final String longitudeRef;

    public final RationalNumber latitudeDegrees;
    public final RationalNumber latitudeMinutes;
    public final RationalNumber latitudeSeconds;
    public final RationalNumber longitudeDegrees;
    public final RationalNumber longitudeMinutes;
    public final RationalNumber longitudeSeconds;

Manipulating images
-------------------

### Scaling

There is two way to scale an image:

* Using the Image object directly
* Using the ScaleHelper

Using the Image allows to scale an image pretty easily. The scaling uses Bilinear algorithm:

    Image img = new Image(...);
    img.scale(ratio);


However, if you want more control over the scaling, use the Scale Helper:

    ScaleHelper scaleHelper = new ScaleHelper();
    Image img = new Image();
    Image scaled = scaleHelper.scale(img, 0.5f);

    Image scaledBicubic = scaleHelper.scaleImageBicubic(img, newWidth, newHeight);

    BufferedImage img = scaleHelper.scale100To25(img.getBufferedImage());


### Rotation
There is two way to rotate an image:

# Using the Image object directly
# Using the ManipulationHelper

Using the Image allows to rotate an image pretty easily:

    Image img = new Image(...);
    img.rotate(angle); // Angle in degree

However, if you want more control over the rotation, use the Manipulation Helper providing:

* rotate(BufferedImage, angle) => a rotated buffered image
* rotate(Image, angle) => a rotated Image


    ManipulationHelper manipulationHelper = new ManipulationHelper();
    Image img = new Image();
    Image rotated = manipulationHelper.rotate(90);


### Transparency

The manipulation helper also allows to make an Image translucide or make a specific color transparent

    Image image = new Image(PNG_BEASTIE);
    Assert.assertFalse(image.getMetadata().isTransparent());

    Image image2 = ImageIOUtils.getManipulationHelper().makeColorTransparent(image, Color.WHITE);
    Assert.assertTrue(image2.getMetadata().isTransparent());

    Image image3 = ImageIOUtils.getManipulationHelper().makeTranslucent(image, 0.5f);
    Assert.assertTrue(image3.getMetadata().isTransparent());


### Flipping
The manipulation helper class also provides methods to flip images:

* horizontalflip(BufferedImage)
* horizontalflip(Image)
* verticalflip(BufferedImage)
* verticalflip(Image)

Using Helper objects
--------------------

If you don't want to use the Image class, you can use helper objects directly. The _de.akquinet.commons.image.io.ImageIOUtils_ class allows getting helper objects:

* getIOHelper()
* getScaleHelper()
* getConverterHelper()
* getManipulationHelper()
