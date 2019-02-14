package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.gfx.ImageTool;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.MainWindow;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ar.com.shipcommand.physics.geo.GeoTools;
import ar.com.shipcommand.physics.geo.HeightMap;
import ar.com.shipcommand.physics.geo.Heights;
import ar.com.shipcommand.physics.magnitudes.Distance;
import ar.com.shipcommand.physics.magnitudes.DistanceUnits;
import ucar.ma2.InvalidRangeException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tactical implements IRenderable {
    // Height map file reader
    HeightMap heightMap;

    // Generated image of the world
    BufferedImage map = null;

    // Center of the area currently shown
    private Geo2DPosition center;
    // Distance of the side of the area currently shown
    private Distance areaSize;

    private Geo2DPosition upperLeft;
    private Geo2DPosition upperRight;
    private Geo2DPosition lowerLeft;
    private Geo2DPosition lowerRight;

    // Calculated area width in degrees given the upper left and lower right corners
    double areaWidth;
    double areaHeight;

    // Calculated number of degrees of lat and lon per pixel
    double lonPerPixel;
    double latPerPixel;

    // Get the main window's size
    int mapWidth;
    int mapHeight;

    /**
     * Creates a new map
     *
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     */
    public Tactical() throws IOException, InvalidRangeException {
        // Get the main window object
        MainWindow win = Game.getMainWindow();

        // Get the main window's size
        mapWidth = win.getWidth();
        mapHeight = mapWidth / 2;

        heightMap = new HeightMap();

        setArea(new Geo2DPosition(-89.99, 0), new Distance(3000, DistanceUnits.NauticalMiles));
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
        upperRight = center.clone();
        lowerLeft = center.clone();
        lowerRight = center.clone();

        upperLeft.move(315, diagonalSize);
        upperRight.move(45, diagonalSize);
        lowerLeft.move(225, diagonalSize);
        lowerRight.move(135, diagonalSize);
    }

    /**
     * Draw the map on the screen
     *
     * @param graphics Graphics object for drawings
     */
    protected void drawMap(Graphics2D graphics) throws IOException, InvalidRangeException {
        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            Distance vDistancePerPixel = new Distance(areaSize.inMeters() / mapHeight);
            Distance hDistancePerPixel = new Distance(areaSize.inMeters() / mapWidth);

            // Create a new buffered image for the map
            map = ImageTool.createHardwareAccelerated(mapWidth, mapHeight, false);

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = upperLeft.clone();
            Geo2DPosition currentRight = upperRight.clone();

            // Iterate each pixel in the image
            for (int y = 0; y < mapHeight; y++) {
                Geo2DPosition current = currentLeft.clone();

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < mapWidth; x++) {
                    // Get the depth of the current point
                    int depth = 100;//heightMap.getHeight(current);
                    // Calculate the color of the pixel given the depth
                    Color color = MapTools.getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());

                    double bearing = GeoTools.getBearing(current, currentRight);
                    current.move(bearing, hDistancePerPixel);
                }

                double leftBearing = GeoTools.getBearing(currentLeft, lowerLeft);
                double rightBearing = GeoTools.getBearing(currentRight, lowerRight);

                // Calculate the new latitude moving as many degrees needed for the next pixel
                currentLeft.move(leftBearing, vDistancePerPixel);
                currentRight.move(rightBearing, vDistancePerPixel);
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
            center.move(90, d);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_W)) {
            center.move(0, d);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_A)) {
            center.move(270, d);
            calculateCorners();
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_S)) {
            center.move(180, d);
            calculateCorners();
            map = null;
        }

        try {
            drawMap(graphics);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }
    }
}
