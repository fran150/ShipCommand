package ar.com.shipcommand.ui.map;

import ar.com.shipcommand.geo.*;
import ar.com.shipcommand.gfx.*;
import ar.com.shipcommand.input.*;
import ar.com.shipcommand.main.windows.*;
import ar.com.shipcommand.ui.*;
import lombok.*;
import ucar.ma2.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * Shows a planishere view of the world map
 */
public class Map implements Renderable {
    /**
     * Current zoom configuration
     */
    @Getter
    private final MapZoom currentZoomConfig;

    /**
     * Map width
     */
    @Getter
    private final int width;
    /**
     * Map height
     */
    @Getter
    private final int height;

    // Map resizing info
    private final MapResizingBox mapResizingBox;
    // Height map file reader
    private final HeightMap heightMap;
    // Previous shown areas
    private final Stack<Geo2DPosition> history;
    // Generated image
    private BufferedImage map;
    // Track the right button release event
    private ButtonReleaseTracker buttonReleaseTracker;



    /**
     * Creates a new map
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     */
    public Map() throws IOException {
        this.heightMap = new HeightMap();

        // Stack for storing the previous areas shown in map
        this.history = new Stack<>();

        // Get the main window object
        MainWindow win = WindowManager.getMainWindow();

        // Set the map size
        this.width = win.getWidth();
        this.height = win.getHeight();

        this.currentZoomConfig = new MapZoom(this.width, this.height);
        this.mapResizingBox = new MapResizingBox(this);
        this.buttonReleaseTracker = new ButtonReleaseTracker(3);
    }

    /**
     * Sets a new area to show on map
     * @param upperLeft Upper left position of the area to show
     * @param lowerRight Lower right position of the area to show
     */
    public void pushArea(Geo2DPosition upperLeft, Geo2DPosition lowerRight) {
        history.push(currentZoomConfig.getUpperLeft());
        history.push(currentZoomConfig.getLowerRight());

        currentZoomConfig.setZoomArea(upperLeft, lowerRight);
        clearMapCache();
    }

    /**
     * Retrieve a new area from the zoom history to show
     * If its the last area show the entire world
     */
    public void popArea() {
        if (!history.isEmpty()) {
            Geo2DPosition lowerRight = history.pop();
            Geo2DPosition upperLeft = history.pop();

            currentZoomConfig.setZoomArea(upperLeft, lowerRight);
            clearMapCache();
        }
    }

    /**
     * Draw the map on the screen
     * @param graphics Graphics object for drawings
     */
    private void drawMap(Graphics2D graphics) throws IOException, InvalidRangeException {
        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            // Create a new buffered image for the map
            map = ImageTool.createHardwareAccelerated(width, height, false);

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = new Geo2DPosition(currentZoomConfig.getUpperLeft());
            Geo2DPosition currentRight = new Geo2DPosition(currentLeft);
            currentRight.setLon(currentRight.getLon() + currentZoomConfig.getAreaWidth());

            // Iterate each pixel in the image
            for (int y = 0; y < height; y++) {
                // Get the line of heights for this latitude, between the specified longitudes
                // and stepping by the longitudes per pixels.
                Heights heights = heightMap
                        .getHeightsLine(currentLeft.getLat(),
                                        currentLeft.getLon(),
                                        currentRight.getLon(),
                                        currentZoomConfig.getLonPerPixel());

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < width; x++) {
                    double ratio = x / (double) width;
                    long size = heights.getSize() - 1;
                    int heightCoordinate = (int) Math.round(ratio * size);

                    // Get the depth of the current point
                    int depth = heights.getHeight(heightCoordinate);

                    // Calculate the color of the pixel given the depth
                    Color color = MapTools.getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());
                }

                // Calculate the new latitude moving as many degrees needed for the next pixel
                double newLatitude = currentLeft.getLat() - currentZoomConfig.getLatPerPixel();

                // Move down both pointers
                currentLeft.setLat(newLatitude);
                currentRight.setLat(newLatitude);
            }
        }

        // Draw the current map image
        graphics.drawImage(map, null, null);
    }

    /**
     * Clears the current image displayed forcing the draw routine to
     * redraw a new one
     */
    private void clearMapCache() {
        map = null;
    }

    /**
     * Generates an image for the map and shows it on the main window
     * @param graphics Graphics object reference
     * @param dt Time elapsed from previous time step
     */
    @SneakyThrows
    public void render(Graphics2D graphics, double dt) {
        drawMap(graphics);

        if (mapResizingBox.isResizing()) {
            mapResizingBox.drawResizeRectangle(graphics);

            // If the button is released stop resizing and redraw the map with the new
            // coordinates
            if (!MouseHandler.isButtonPressed(1)) {
                mapResizingBox.stopResizing();
            }
        } else {
            // If mouse button is pressed start resizing and set the initial coordinates
            if (MouseHandler.isButtonPressed(1)) {
                mapResizingBox.startResizing();
            }
        }

        // Pops the area from the history when the mouse button is released.
        buttonReleaseTracker.onButtonRelease(this::popArea);
    }
}