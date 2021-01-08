package ar.com.shipcommand.ui;

import ar.com.shipcommand.physics.geo.GeoConstants;

import java.awt.*;

/**
 * Tools for map drawing
 */
public class MapTools {
    /**
     * Returns the color in rgb tinted in the specified factor
     *
     * @param r red color
     * @param g green color
     * @param b blue color
     * @param factor tint factor
     * @return Tinted color
     */
    public static Color tint(double r, double g, double b, double factor) {
        Double red = new Double(r + (255 - r) * factor);
        Double green = new Double(g + (255 - g) * factor);
        Double blue = new Double(b + (255 - b) * factor);;
        return new Color(red.intValue(), green.intValue(), blue.intValue());
    }

    /**
     * Returns the color in rgb shaded in the specified factor
     *
     * @param r red color
     * @param g green color
     * @param b blue color
     * @param factor shade factor
     * @return Shaded color
     */
    public static Color shade(double r, double g, double b, double factor) {
        Double red = new Double(r * (1 - factor));
        Double green = new Double(g * (1 - factor));
        Double blue = new Double(b * (1 - factor));
        return new Color(red.intValue(), green.intValue(), blue.intValue());
    }

    /**
     * Returns the corresponding color for the given depth
     *
     * @param depth Depth in meters
     * @return Color for the map
     */
    public static Color getColor(double depth) {
        // Shade / Tint factor
        double factor = 0;
        // Red, green and blue components
        double r, g, b;
        // Max shading factor
        double maxFactor;

        // If depth is negative
        if (depth < 0) {
            // Paint blue
            r = 0; g = 0; b = 255;
            // More deep more dark
            factor = depth / GeoConstants.EARTH_MIN_HEIGHT;
            return shade(r, g, b, factor);
        } else {
            if (depth > 1300) {
                // If depth is larger than 1300 meters paint brown
                r = 205; g = 133; b = 63;
                // More height more dark
                factor = ((depth - 1300) / GeoConstants.EARTH_MAX_HEIGHT);

                factor = factor > 1 ? 1 : factor;
                factor = factor < 0 ? 0 : factor;
                return shade(r, g, b, factor);
            } else if (depth > 500 && depth <= 1300) {
                // Between 500 and 1300 meters paint yellow
                r = 255; g = 255; b = 153; maxFactor = 0.45;
                // More height more dark
                factor = ((depth - 500) / ((1300 - 500) / maxFactor));
                return shade(r, g, b, factor);
            } else {
                // Below 500 meters paint green
                r = 0; g = 156; b = 76; maxFactor = 0.5;
                // More height less dark
                factor = (depth / (500 / maxFactor));
                return tint(r, g, b, factor);
            }
        }
    }

}
