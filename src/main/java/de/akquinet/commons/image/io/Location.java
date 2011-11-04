package de.akquinet.commons.image.io;

import org.apache.sanselan.common.RationalNumber;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;

/**
 * Class representing the location.
 * Location metadata are extracted from JPEG file containing
 * EXIF metadata.
 */
public class Location {
    public final String latitudeRef;
    public final String longitudeRef;

    public final RationalNumber latitudeDegrees;
    public final RationalNumber latitudeMinutes;
    public final RationalNumber latitudeSeconds;
    public final RationalNumber longitudeDegrees;
    public final RationalNumber longitudeMinutes;
    public final RationalNumber longitudeSeconds;

    public Location(final String latitudeRef, final String longitudeRef,
                    final RationalNumber latitudeDegrees,
                    final RationalNumber latitudeMinutes,
                    final RationalNumber latitudeSeconds,
                    final RationalNumber longitudeDegrees,
                    final RationalNumber longitudeMinutes,
                    final RationalNumber longitudeSeconds) {
        this.latitudeRef = latitudeRef;
        this.longitudeRef = longitudeRef;
        this.latitudeDegrees = latitudeDegrees;
        this.latitudeMinutes = latitudeMinutes;
        this.latitudeSeconds = latitudeSeconds;
        this.longitudeDegrees = longitudeDegrees;
        this.longitudeMinutes = longitudeMinutes;
        this.longitudeSeconds = longitudeSeconds;
    }

    public Location(TiffImageMetadata.GPSInfo info) {
        this.latitudeRef = info.latitudeRef;
        this.longitudeRef = info.longitudeRef;
        this.latitudeDegrees = info.latitudeDegrees;
        this.latitudeMinutes = info.latitudeMinutes;
        this.latitudeSeconds = info.latitudeSeconds;
        this.longitudeDegrees = info.longitudeDegrees;
        this.longitudeMinutes = info.longitudeMinutes;
        this.longitudeSeconds = info.longitudeSeconds;
    }

    public String toString() {
        // This will format the gps info like so:
        //
        // latitude: 8 degrees, 40 minutes, 42.2 seconds S
        // longitude: 115 degrees, 26 minutes, 21.8 seconds E

        StringBuffer result = new StringBuffer();
        result.append("[GPS. ");
        result.append("Latitude: " + latitudeDegrees.toDisplayString()
                + " degrees, " + latitudeMinutes.toDisplayString()
                + " minutes, " + latitudeSeconds.toDisplayString()
                + " seconds " + latitudeRef);
        result.append(", Longitude: " + longitudeDegrees.toDisplayString()
                + " degrees, " + longitudeMinutes.toDisplayString()
                + " minutes, " + longitudeSeconds.toDisplayString()
                + " seconds " + longitudeRef);
        result.append("]");

        return result.toString();
    }

    public double getLongitudeAsDegreesEast() {
        double result = longitudeDegrees.doubleValue()
                + (longitudeMinutes.doubleValue() / 60.0)
                + (longitudeSeconds.doubleValue() / 3600.0);

        if (longitudeRef.trim().equalsIgnoreCase("e"))
            return result;
        else if (longitudeRef.trim().equalsIgnoreCase("w"))
            return -result;
        else
            throw new IllegalStateException("Unknown longitude ref: \""
                    + longitudeRef + "\"");
    }

    public double getLatitudeAsDegreesNorth() {
        double result = latitudeDegrees.doubleValue()
                + (latitudeMinutes.doubleValue() / 60.0)
                + (latitudeSeconds.doubleValue() / 3600.0);

        if (latitudeRef.trim().equalsIgnoreCase("n"))
            return result;
        else if (latitudeRef.trim().equalsIgnoreCase("s"))
            return -result;
        else
            throw new IllegalStateException("Unknown latitude ref: \""
                    + latitudeRef + "\"");
    }

}