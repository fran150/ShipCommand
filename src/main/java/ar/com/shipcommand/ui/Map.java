package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.input.MouseHandler;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.MainWindow;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Stack;

public class Map implements IRenderable {
    // Heightmap file
    NetcdfFile file;
    // Z variable of the NetCDF file
    Variable z;
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

    // Width and height of the heightmap grid
    private static long GRID_WIDTH = 21601;
    private static long GRID_HEIGHT = 10801;

    // Min and max altitudes found on the heightmap file
    private static long EARTH_MIN_HEIGHT = -11000;
    private static long EARTH_MAX_HEIGHT = 7000;

    // Indicates if the map is being resized
    boolean resizing = false;
    int resizeStartPosX;
    int resizeStartPosY;


    /**
     * Creates a new map
     *
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     */
    public Map() throws IOException {
        // Read the NetCDF file and get the Z variable
        file = NetcdfFile.open("./src/main/resources/GRIDONE_1D.nc");
        z = file.findVariable("z");

        // Stack for storing the previous areas shown in map
        history = new Stack<>();

        // Set the default area
        setDefaultArea();
    }

    /**
     * Sets the default area to show on the map
     */
    protected void setDefaultArea() {
        // Show the entire world
        upperLeft = new Geo2DPosition(-90, -180);
        lowerRight = new Geo2DPosition(90, 180);
    }

    /**
     * Returns if the map is showing the default area
     *
     * @return True if the map is showing the entire world
     */
    protected boolean isDefaultArea() {
        return upperLeft.getLat() != -90 || upperLeft.getLon() != -180 || lowerRight.getLat() != 90 && lowerRight.getLon() != 180;
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
     * Converts to the given geographical position to the corresponding grid index
     *
     * @param position Position to convert
     * @return index in the heightmap file
     */
    protected long posToGrid(Geo2DPosition position) {
        return Math.round((Math.round((position.getLat() + 90) * 60) * GRID_WIDTH) + ((position.getLon() + 180) * 60));
    }

    /**
     * Converts the given screen position to geographical position on the map
     *
     * @param x X position on the screen
     * @param y Y position on the screen
     * @return Geographical position of corresponding point in the screen
     */
    protected Geo2DPosition screenToPos(int x, int y) {
        double lat = (y * latPerPixel) - 90;
        double lon = (x * lonPerPixel) - 180;

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
        // Get the main window object
        MainWindow win = Game.getMainWindow();

        // Get the main window's size
        int winWidth = win.getWidth();
        int winHeight = win.getHeight();

        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            // Create a new buffered image for the map
            map = new BufferedImage(winWidth, winHeight, BufferedImage.TYPE_INT_ARGB);

            // Calculate the area width in degrees given the upper left and lower right corners
            areaWidth = lowerRight.getLon() - upperLeft.getLon();
            areaHeight = lowerRight.getLat() - upperLeft.getLat();

            // Calculate the number of degrees of lat and lon per pixel
            lonPerPixel = areaWidth / winWidth;
            latPerPixel = areaHeight / winHeight;

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the end
            Geo2DPosition currentLeft = upperLeft.clone();
            Geo2DPosition currentRight = currentLeft.clone();
            currentRight.setLon(currentRight.getLon() + areaWidth);

            // Iterate each pixel in the image
            for (int y = 0; y < winHeight; y++) {
                // Transform the geographical position of the pointers to an index on the heightmap grid
                long start = posToGrid(currentLeft);
                long end = posToGrid(currentRight);

                // Calculate the step size to sample as many heights as pixels in the screen row
                long step = Math.round(Math.floor(lonPerPixel * 60));

                // Array of heights obtained from the heightmap
                Array read = null;

                try {
                    // Read the row of heights for this line of pixels
                    read = z.read(start + ":" + end + ":" + step);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidRangeException e) {
                    e.printStackTrace();
                }

                // Iterate over all heights drawing them on the image
                for (int x = 0; x < read.getSize() && x < map.getWidth(); x++) {
                    // Get the depth of the current point
                    int depth = read.getInt(x);
                    // Calculate the color of the pixel given the depth
                    Color color = getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());
                }

                // Calculate the new latitude moving as many degrees needed for the next pixel
                double newLat = currentLeft.getLat() + latPerPixel;
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
        if (resizing) {
            int w = MouseHandler.getX() - resizeStartPosX;
            int h = MouseHandler.getY() - resizeStartPosY;

            if (w > 0 && h > 0) {
                graphics.setColor(Color.yellow);
                graphics.drawRect(resizeStartPosX, resizeStartPosY, w, h);
            }

            if (!MouseHandler.isPressed(1)) {
                resizing = false;

                Geo2DPosition ul = screenToPos(resizeStartPosX, resizeStartPosY);
                Geo2DPosition lr = screenToPos(MouseHandler.getX(), MouseHandler.getY());

                pushArea(ul, lr);
            }
        } else {
            if (MouseHandler.isPressed(1)) {
                resizing = true;
                resizeStartPosX = MouseHandler.getX();
                resizeStartPosY = MouseHandler.getY();
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
        drawMap(graphics);
        drawResizeRectangle(graphics);
    }
}