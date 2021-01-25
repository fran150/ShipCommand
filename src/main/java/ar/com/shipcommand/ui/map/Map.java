package ar.com.shipcommand.ui.map;

import ar.com.shipcommand.geo.*;
import ar.com.shipcommand.gfx.*;
import ar.com.shipcommand.input.*;
import ar.com.shipcommand.main.windows.*;
import ar.com.shipcommand.ui.*;
import ucar.ma2.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * Shows a planishere view of the world map
 */
public class Map implements Renderable {
    private final double widthHeightRatio;

    // Current zoom configuration
    private final MapZoom currentZoomConfig;

    // Map resizing info
    private final MapResizingBox mapResizingBox;

    // Height map file reader
    private final HeightMap heightMap;

    // Previous shown areas
    private final Stack<Geo2DPosition> history;

    // Stores the map width and height in pixels, these values are fixed
    private final int mapWidth;
    private final int mapHeight;

    private boolean willZoomBack;

    /**
     * Creates a new map
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     * @throws InvalidRangeException Error when the specified position is invalid
     */
    public Map() throws IOException, InvalidRangeException {
        this.heightMap = new HeightMap();

        // Stack for storing the previous areas shown in map
        this.history = new Stack<>();

        // Get the main window object
        MainWindow win = WindowManager.getMainWindow();

        // Set the map size
        this.mapWidth = win.getWidth();
        this.mapHeight = win.getHeight();

        this.widthHeightRatio = (double) this.mapHeight / (double) this.mapWidth;

        this.currentZoomConfig = new MapZoom(this.mapWidth, this.mapHeight);
        this.mapResizingBox = new MapResizingBox();
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
        }
    }

    /**
     * Draw the map on the screen
     * @param graphics Graphics object for drawings
     */
    private void drawMap(Graphics2D graphics) throws IOException, InvalidRangeException {
        // If there's no image generated for the current zoom, generate a new one
        if (currentZoomConfig.getMap() == null) {
            // Create a new buffered image for the map
            BufferedImage map = ImageTool
                    .createHardwareAccelerated(mapWidth, mapHeight, false);
            currentZoomConfig.setMap(map);

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = new Geo2DPosition(currentZoomConfig.getUpperLeft());
            Geo2DPosition currentRight = new Geo2DPosition(currentLeft);
            currentRight.setLon(currentRight.getLon() + currentZoomConfig.getAreaWidth());

            // Iterate each pixel in the image
            for (int y = 0; y < mapHeight; y++) {
                // Get the line of heights for this latitude, between the specified longitudes
                // and stepping by the longitudes per pixels.
                Heights heights = heightMap
                        .getHeightsLine(currentLeft.getLat(),
                                        currentLeft.getLon(),
                                        currentRight.getLon(),
                                        currentZoomConfig.getLonPerPixel());

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < mapWidth; x++) {
                    double ratio = x / (double) mapWidth;
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
        graphics.drawImage(currentZoomConfig.getMap(), null, null);
    }

    /**
     * Given a width returns the map height by applying the width to height ratio
     * @return Height of the map determined by it's ratio
     */
    private int getHeightFromWidth(int width) {
        return (int) Math.round(width * widthHeightRatio);
    }

    /**
     * Given a height returns the map width by applying the width to height ratio
     * @return Width of the map determined by it's ratio
     */
    private int getWidthFromHeight(int height) {
        return (int) Math.round(height / widthHeightRatio);
    }

    /**
     * Draws the resizing rectangle
     * @param graphics Graphics object
     * @param width resizing rectangle width
     * @param height resizing rectangle height
     */
    private void drawRectangle(Graphics2D graphics, int width, int height) {
        graphics.setColor(Color.yellow);
        graphics.drawRect(mapResizingBox.getResizeStartPosX(), mapResizingBox.getResizeStartPosY(), width, height);
    }

    /**
     * Draw a rectangle when resizing
     * @param graphics Graphics object for drawing
     */
    protected void drawResizeRectangle(Graphics2D graphics) {
        int width = MouseHandler.getCurrentX() - mapResizingBox.getResizeStartPosX();
        int height = getHeightFromWidth(width);

        // Limit resizing if mouse is moved below bottom
        if ((mapResizingBox.getResizeStartPosY() + height) > mapHeight) {
            height = mapHeight - mapResizingBox.getResizeStartPosY();
            width = getWidthFromHeight(height);
        }

        mapResizingBox.setWidth(width);
        mapResizingBox.setHeight(height);

        // If the resizing rectangle has a valid width and height draw it
        if (width > 0 && height > 0) {
            drawRectangle(graphics, width, height);
        }
    }

    private void startResizing() {
        mapResizingBox.setResizing(true);
        mapResizingBox.setResizeStartPosX(MouseHandler.getCurrentX());
        mapResizingBox.setResizeStartPosY(MouseHandler.getCurrentY());
    }

    private void stopResizing() {
        mapResizingBox.setResizing(false);

        if (mapResizingBox.getResizeStartPosX() < MouseHandler.getCurrentX()) {
            int x = mapResizingBox.getResizeStartPosX();
            int y = mapResizingBox.getResizeStartPosY();
            int width = mapResizingBox.getWidth();
            int height = mapResizingBox.getHeight();

            Geo2DPosition upperLeft = currentZoomConfig.screenToPos(x, y);
            Geo2DPosition lowerRight = currentZoomConfig.screenToPos(x + width, y + height);

            pushArea(upperLeft, lowerRight);
        }
    }

    /**
     * Generates an image for the map and shows it on the main window
     * @param graphics Graphics object reference
     * @param dt Time elapsed from previous time step
     */
    public void render(Graphics2D graphics, double dt) {
        try {
            drawMap(graphics);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidRangeException e) {
            e.printStackTrace();
        }

        int mouseX = MouseHandler.getCurrentX();

        if (mapResizingBox.isResizing()) {
            drawResizeRectangle(graphics);

            // If the button is released stop resizing and redraw the map with the new
            // coordinates
            if (!MouseHandler.isButtonPressed(1)) {
                stopResizing();
            }
        } else {
            // If mouse button is pressed start resizing and set the initial coordinates
            if (MouseHandler.isButtonPressed(1)) {
                startResizing();
            }
        }

        if (MouseHandler.isButtonPressed(3)) {
            popArea();
        }
    }
}