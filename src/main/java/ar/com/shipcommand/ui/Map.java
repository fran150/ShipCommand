package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.gfx.ImageTool;
import ar.com.shipcommand.input.MouseHandler;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.windows.MainWindow;
import ar.com.shipcommand.main.windows.WindowManager;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ar.com.shipcommand.physics.geo.GeoTools;
import ar.com.shipcommand.physics.geo.HeightMap;
import ar.com.shipcommand.physics.geo.Heights;
import ucar.ma2.InvalidRangeException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;

public class Map implements Renderable {
    // Height map file reader
    HeightMap heightMap;
    // Generated image of the world
    BufferedImage map = null;

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

    // Previous shown areas
    private Stack<Geo2DPosition> history;

    // Indicates if the map is being resized
    boolean resizing = false;
    int resizeStartPosX;
    int resizeStartPosY;

    // Right click has been pressed and the map will zoom back on release
    boolean willZoomBack = false;

    // Get the main window's size
    int mapWidth;
    int mapHeight;


    /**
     * Creates a new map
     *
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     * @throws InvalidRangeException Error when the specified position is invalid
     */
    public Map() throws IOException, InvalidRangeException {
        heightMap = new HeightMap();

        // Stack for storing the previous areas shown in map
        history = new Stack<>();

        // Get the main window object
        MainWindow win = WindowManager.getMainWindow();

        // Set the map size
        mapWidth = win.getWidth();
        mapHeight = mapWidth / 2;

        // Set the default area
        setDefaultArea();
    }

    /**
     * Sets the default area to show on the map
     */
    protected void setDefaultArea() {
        // Show the entire world
        upperLeft = new Geo2DPosition(90, -180);
        lowerRight = new Geo2DPosition(-90, 180);
    }

    /**
     * Returns if the map is showing the default area
     *
     * @return True if the map is showing the entire world
     */
    protected boolean isDefaultArea() {
        return upperLeft.getLat() != 90 || upperLeft.getLon() != -180 || lowerRight.getLat() != -90 && lowerRight.getLon() != 180;
    }

    /**
     * Sets a new area to show on map
     *
     * @param upperLeft Upper left position of the area to show
     * @param lowerRight Lower right position of the area to show
     */
    public void pushArea(Geo2DPosition upperLeft, Geo2DPosition lowerRight) {
        history.push(this.upperLeft);
        history.push(this.lowerRight);

        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;

        map = null;
    }

    /**
     * Retrieve a new area from the zoom history to show
     *
     * If its the last area show the entire world
     */
    public void popArea() {
        if (!history.isEmpty()) {
            lowerRight = history.pop();
            upperLeft = history.pop();
            map = null;
        } else {
            if (!isDefaultArea()) {
                setDefaultArea();
                map = null;
            }
        }
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
     * Draw the map on the screen
     *
     * @param graphics Graphics object for drawings
     */
    protected void drawMap(Graphics2D graphics) throws IOException, InvalidRangeException {
        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            // Create a new buffered image for the map
            map = ImageTool.createHardwareAccelerated(mapWidth, mapHeight, false);

            // Calculate the area width in degrees given the upper left and lower right corners
            areaWidth = GeoTools.getLongitudeDifference(upperLeft, lowerRight);
            areaHeight = upperLeft.getLat() - lowerRight.getLat();

            // Calculate the number of degrees of lat and lon per pixel
            lonPerPixel = areaWidth / mapWidth;
            latPerPixel = areaHeight / mapHeight;

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = upperLeft.clone();
            Geo2DPosition currentRight = currentLeft.clone();
            currentRight.setLon(currentRight.getLon() + areaWidth);

            // Iterate each pixel in the image
            for (int y = 0; y < mapHeight; y++) {
                Heights heights = heightMap
                        .getHeightsLine(currentLeft.getLat(), currentLeft.getLon(), currentRight.getLon(), lonPerPixel);

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < mapWidth; x++) {
                    double r = x / (double) mapWidth;
                    long s = heights.getSize() - 1;
                    int gx = (int) Math.round(r * s);

                    // Get the depth of the current point
                    int depth = heights.getHeight(gx);
                    // Calculate the color of the pixel given the depth
                    Color color = MapTools.getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());
                }

                // Calculate the new latitude moving as many degrees needed for the next pixel
                double newLat = currentLeft.getLat() - latPerPixel;
                currentLeft.setLat(newLat);
                currentRight.setLat(newLat);
            }
        }

        // Draw the current map image
        graphics.drawImage(map, null, null);
    }

    /**
     * Draw a rectangle when resizing
     *
     * @param graphics Graphics object for drawing
     */
    protected void drawResizeRectangle(Graphics2D graphics) {
        int mouseX = MouseHandler.getCurrentX();

        if (mouseX < 0) mouseX = 0;
        if (mouseX > mapWidth) mouseX = mapWidth;

        if (resizing) {
            int w = mouseX - resizeStartPosX;
            int h = w / 2;

            if ((resizeStartPosY + h) > mapHeight) {
                h = mapHeight - resizeStartPosY;
                w = h * 2;
            }

            if (w > 0 && h > 0) {
                graphics.setColor(Color.yellow);
                graphics.drawRect(resizeStartPosX, resizeStartPosY, w, h);
            }

            if (!MouseHandler.isButtonPressed(1)) {
                resizing = false;

                if (resizeStartPosX < mouseX) {
                    Geo2DPosition ul = screenToPos(resizeStartPosX, resizeStartPosY);
                    Geo2DPosition lr = screenToPos(mouseX, resizeStartPosY + h);

                    pushArea(ul, lr);
                }
            }
        } else {
            if (MouseHandler.isButtonPressed(1)) {
                resizing = true;
                resizeStartPosX = mouseX;
                resizeStartPosY = MouseHandler.getCurrentY();
            }
        }
    }

    protected void checkZoomBack() {
        if (MouseHandler.isButtonPressed(3)) {
            willZoomBack = true;
        } else {
            if (willZoomBack) {
                popArea();
                willZoomBack = false;
            }
        }
    }

    /**
     * Generates an image for the map and shows it on the main window
     *
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

        drawResizeRectangle(graphics);
        checkZoomBack();
    }
}
