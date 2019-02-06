package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.gfx.ImageTool;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.MainWindow;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ar.com.shipcommand.physics.geo.HeightMap;
import ar.com.shipcommand.physics.geo.Heights;
import ar.com.shipcommand.physics.magnitudes.Distance;
import ar.com.shipcommand.physics.magnitudes.DistanceUnits;
import ucar.ma2.Array;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;

public class Tactical implements IRenderable {
    // Generated image of the world
    BufferedImage map = null;

    // Center of the area currently shown
    private Geo2DPosition center;
    // Distance of the side of the area currently shown
    private Distance areaSize;

    // Upper left position of the area currently shown
    private Geo2DPosition upperLeft;
    // Lower right position of the area currently shown
    private Geo2DPosition lowerRight;

    // Calculated area width in degrees given the upper left and lower right corners
    double areaWidth;
    double areaHeight;

    // Calculated number of degrees of lat and lon per pixel
    double lonPerPixel;
    double latPerPixel;

    // Min and max altitudes found on the heightmap file
    private static long EARTH_MIN_HEIGHT = -11000;
    private static long EARTH_MAX_HEIGHT = 8500;

    // Get the main window's size
    int mapWidth;
    int mapHeight;

    HeightMap heightMap;

    /**
     * Creates a new map
     *
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     */
    public Tactical() throws IOException {

        // Get the main window object
        MainWindow win = Game.getMainWindow();

        // Get the main window's size
        mapWidth = win.getWidth();
        mapHeight = mapWidth / 2;

        heightMap =  new HeightMap();

        setArea(new Geo2DPosition(0, 0), new Distance(4000, DistanceUnits.NauticalMiles));
    }

    public void setArea(Geo2DPosition center, Distance areaSize) {
        this.center = center;
        this.areaSize = areaSize;
        calculateCorners();
    }

    protected void calculateCorners() {
        double halfSquared = Math.pow(areaSize.inMeters() / 2, 2);

        Distance diagonalSize = new Distance(Math.sqrt(2 * halfSquared));

        upperLeft = center.clone();
        lowerRight = center.clone();

        upperLeft.move(diagonalSize, 315);
        lowerRight.move(diagonalSize, 135);
    }

    /**
     * Converts the given screen position to geographical position on the map
     *
     * @param x X position on the screen
     * @param y Y position on the screen
     * @return Geographical position of corresponding point in the screen
     */
    protected Geo2DPosition screenToPos(int x, int y) {
        double lat = upperLeft.getLat() - (y * latPerPixel);
        double lon = (x * lonPerPixel) + upperLeft.getLon();

        return new Geo2DPosition(lat, lon);
    }

    /**
     * Returns the color in rgb tinted in the specified factor
     *
     * @param r red color
     * @param g green color
     * @param b blue color
     * @param factor tint factor
     * @return Tinted color
     */
    protected Color tint(double r, double g, double b, double factor) {
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
    protected Color shade(double r, double g, double b, double factor) {
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
    protected Color getColor(double depth) {
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
            factor = depth / EARTH_MIN_HEIGHT;
            return shade(r, g, b, factor);
        } else {
            if (depth > 1300) {
                // If depth is larger than 1300 meters paint brown
                r = 205; g = 133; b = 63;
                // More height more dark
                factor = ((depth - 1300) / EARTH_MAX_HEIGHT);
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

    /**
     * Draw the map on the screen
     *
     * @param graphics Graphics object for drawings
     */
    protected void drawMap(Graphics2D graphics) {
        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            // Create a new buffered image for the map
            map = ImageTool.createHardwareAccelerated(mapWidth, mapHeight, false);

            // Calculate the area width in degrees given the upper left and lower right corners
            areaWidth = heightMap.getAreaWidth(upperLeft, lowerRight);
            areaHeight = Math.abs(upperLeft.getLat() - lowerRight.getLat());

            // Calculate the number of degrees of lat and lon per pixel
            lonPerPixel = areaWidth / mapWidth;
            latPerPixel = areaHeight / mapHeight;

            // Calculate the step size to sample as many heights as pixels in the screen row
            long step = Math.round(Math.floor(lonPerPixel * 60));
            if (step < 1) step = 1;

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = upperLeft.clone();
            Geo2DPosition currentRight = currentLeft.clone();
            currentRight.moveLon(areaWidth);

            // Iterate each pixel in the image
            for (int y = 0; y < mapHeight; y++) {
                Heights read = heightMap.getHeights(currentLeft.getLat(), currentLeft.getLon(), currentRight.getLon(), step);

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < map.getWidth(); x++) {
                    double w = map.getWidth();
                    double r = (x / w);
                    int s = (int) read.getSize() - 1;
                    int gx = (int) Math.round(r * s);

                    // Get the depth of the current point
                    int depth = read.getHeight(gx);
                    // Calculate the color of the pixel given the depth
                    Color color = getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());
                }

                // Calculate the new latitude moving as many degrees needed for the next pixel
                currentLeft.moveLat(-1 * latPerPixel);
                currentRight.moveLat(-1 * latPerPixel);
            }
        }

        // Draw the current map image
        graphics.drawImage(map, null, null);
    }

    /**
     * Generates an image for the map and shows it on the main window
     *
     * @param graphics Graphics object reference
     * @param dt Time elapsed from previous time step
     */
    public void render(Graphics2D graphics, double dt) {
        Distance d = new Distance(500, DistanceUnits.NauticalMiles);

        if (KeyHandler.isDown(KeyEvent.VK_D)) {
            center.move(d, 90);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_W)) {
            center.move(d, 0);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_A)) {
            center.move(d, 270);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_S)) {
            center.move(d, 180);
            calculateCorners();
            map = null;
        }


        drawMap(graphics);
    }
}
