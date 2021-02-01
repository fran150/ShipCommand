package ar.com.shipcommand.ui;

import ar.com.shipcommand.common.CommonConstants;
import lombok.AllArgsConstructor;

import java.awt.Color;

/**
 * Tools for map drawing
 */
public class MapTools {
    /**
     * RGB Colors
     */
    @AllArgsConstructor
    private static class RGB {
        /**
         * Red color
         */
        private final int red;
        /**
         * Green color
         */
        private final int green;
        /**
         * Blue color
         */
        private final int blue;

        /**
         * Blue map color
         * @return RGB blue color for map
         */
        private static RGB blue() {
            return new RGB(0,0, 255);
        }

        /**
         * Brown map color
         * @return RGB brown color for map
         */
        private static RGB brown() {
            return new RGB(205, 133, 63);
        }

        /**
         * Yellow map color
         * @return RGB yellow color for map
         */
        private static RGB yellow() {
            return new RGB(255, 255,153);
        }

        /**
         * Green map color
         * @return RGB green color for map
         */
        private static RGB green() {
            return new RGB(0, 156,76);
        }
    }

    /**
     * Returns the color in rgb tinted in the specified factor
     * @param baseColor base color for tinting
     * @param factor tint factor
     * @return Tinted color
     */
    private static Color tint(RGB baseColor, double factor) {
        int r = (int)(baseColor.red + (255 - baseColor.red) * factor);
        int g = (int)(baseColor.green + (255 - baseColor.green) * factor);
        int b = (int)(baseColor.blue + (255 - baseColor.blue) * factor);
        return new Color(r, g, b);
    }

    /**
     * Returns the color in rgb shaded in the specified factor
     * @param baseColor base color for shading
     * @param factor shade factor
     * @return Shaded color
     */
    private static Color shade(RGB baseColor, double factor) {
        int r = (int)(baseColor.red * (1 - factor));
        int g = (int)(baseColor.green * (1 - factor));
        int b = (int)(baseColor.blue * (1 - factor));
        return new Color(r, g, b);
    }

    /**
     * Returns the corresponding color for the given depth
     * @param depth Depth in meters
     * @return Color for the map
     */
    public static Color getColor(double depth) {
        // Shade / Tint factor
        double factor = 0;

        // If depth is negative
        if (depth < 0) {
            // Paint blue, more deep more dark
            factor = depth / CommonConstants.EARTH_MIN_HEIGHT;
            return shade(RGB.blue(), factor);
        } else {
            if (depth > 1300) {
                // Paint brown, more height more dark
                factor = ((depth - 1300) / CommonConstants.EARTH_MAX_HEIGHT);
                factor = Math.max(Math.min(factor, 1), 0);
                return shade(RGB.brown(), factor);
            } else if (depth > 500 && depth <= 1300) {
                // Between 500 and 1300 meters paint yellow
                double maxFactor = 0.45;
                // More height more dark
                factor = ((depth - 500) / ((1300 - 500) / maxFactor));
                return shade(RGB.yellow(), factor);
            } else {
                // Below 500 meters paint green
                double maxFactor = 0.5;
                // More height less dark
                factor = (depth / (500 / maxFactor));
                return tint(RGB.green(), factor);
            }
        }
    }
}