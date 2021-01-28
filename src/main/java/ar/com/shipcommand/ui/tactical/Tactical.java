package ar.com.shipcommand.ui.tactical;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.geo.Geo2DPosition;
import ar.com.shipcommand.geo.Geo2DReadonlyPosition;
import ar.com.shipcommand.geo.HeightMap;
import ar.com.shipcommand.gfx.ImageTool;
import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.main.windows.MainWindow;
import ar.com.shipcommand.main.windows.WindowManager;
import ar.com.shipcommand.physics.magnitudes.Bearing;
import ar.com.shipcommand.physics.magnitudes.Distance;
import ar.com.shipcommand.physics.magnitudes.DistanceUnits;
import ar.com.shipcommand.physics.magnitudes.ReadOnlyDistance;
import ar.com.shipcommand.ui.MapTools;
import lombok.Getter;
import lombok.SneakyThrows;
import ucar.ma2.InvalidRangeException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * Shows an spherical projection unfolded to fit in the window.
 * This is more accurate on high latitudes and high zoom level
 */
public class Tactical implements Renderable {
    // Height map file reader
    private final HeightMap heightMap;

    // Generated image of the world
    private BufferedImage map = null;

    // Center of the area currently shown
    private Geo2DPosition center;
    // Distance of the side of the area currently shown
    private Distance areaSize;

    // Get the main window's size
    @Getter
    private final int mapWidth;
    @Getter
    private final int mapHeight;

    /**
     * Creates a new map
     *
     * @throws IOException Thrown if the heightmap NetCDF file is not found
     */
    public Tactical() throws IOException {
        // Get the main window object
        MainWindow win = WindowManager.getMainWindow();

        // Get the main window's size
        mapWidth = win.getWidth() / 3;
        mapHeight = win.getHeight() / 3;

        heightMap = new HeightMap();

        setArea(new Geo2DPosition(0, 0), new Distance(1000, DistanceUnits.NauticalMiles));
    }

    public void setArea(Geo2DPosition center, Distance areaSize) {
        this.center = center;
        this.areaSize = areaSize;
    }

    public Geo2DReadonlyPosition getCenterPosition() {
        return center.asReadOnly();
    }

    public ReadOnlyDistance getAreaSize() {
        return areaSize.asReadOnly();
    }

    /**
     * Draw the map on the screen
     *
     * @param graphics Graphics object for drawings
     */
    protected void drawMap(Graphics2D graphics) throws IOException, InvalidRangeException {
        // If there's no image generated for the current zoom, generate a new one
        if (map == null) {
            // Calculate screen height / width ratio
            double screenRatio = (double) mapHeight / (double) mapWidth;

            // Calculate the area width and height in meters
            double areaWidthInMeters = areaSize.inMeters();
            double areaHeightInMeters = areaSize.inMeters() * screenRatio;

            // Get the horizontal and vertical distance per pixel
            Distance hDistancePerPixel = new Distance(areaWidthInMeters / mapWidth);
            Distance vDistancePerPixel = new Distance(areaHeightInMeters / mapHeight);

            // Calculate geographical position of the map corners for the given center,
            // area size and screen ratio
            TacticalMapCorners corners = new TacticalMapCorners(center, areaSize, screenRatio);

            // Create a new buffered image for the map
            map = ImageTool.createHardwareAccelerated(mapWidth, mapHeight, false);

            // Place 2 pointers at the first line of latitude to draw.
            // One at the longitude of the left corner and the other at the right
            Geo2DPosition currentLeft = new Geo2DPosition(corners.getUpperLeft());
            Geo2DPosition currentRight = new Geo2DPosition(corners.getUpperRight());

            // Pointer to the current position being drawn
            Geo2DPosition current = new Geo2DPosition();

            // Iterate each pixel in the image
            for (int y = 0; y < mapHeight; y++) {
                // Put the current pointer in the left part of this line
                current.copy(currentLeft);

                // Draw all the points of this line of pixels
                for (int x = 0; x < mapWidth; x++) {
                    // Get the depth of the current point
                    int depth = heightMap.getHeight(current);

                    // Calculate the color of the pixel given the depth
                    Color color = MapTools.getColor(depth);

                    // Draw the pixel on the image
                    map.setRGB(x, y, color.getRGB());

                    // Move the current pointer right towards the right pointer
                    // and in the distance to get the next pixel height
                    current.moveTowards(currentRight, hDistancePerPixel);
                }

                // Move both left and right limits towards the lower limits the distance
                // to draw the next line of pixels
                currentLeft.moveTowards(corners.getLowerLeft(), vDistancePerPixel);
                currentRight.moveTowards(corners.getLowerRight(), vDistancePerPixel);
            }
        }

        // Get the main window object
        MainWindow win = WindowManager.getMainWindow();

        // Resize the map image to the size of the main window
        map = ImageTool.resize(map, win.getWidth(), win.getHeight());

        // Draw the current map image
        graphics.drawImage(map, null, null);
    }

    private void processKeyPress() {
        Distance d = new Distance(getAreaSize().inNauticalMiles() / 8, DistanceUnits.NauticalMiles);

        if (KeyHandler.isDown(KeyEvent.VK_D)) {
            center.move(new Bearing(CommonConstants.EAST_HEADING), d);
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_W)) {
            center.move(new Bearing(CommonConstants.NORTH_HEADING), d);
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_A)) {
            center.move(new Bearing(CommonConstants.WEST_HEADING), d);
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_S)) {
            center.move(new Bearing(CommonConstants.SOUTH_HEADING), d);
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_EQUALS)) {
            areaSize.setNauticalMiles(getAreaSize().inNauticalMiles() - 50);
            map = null;
        }

        if (KeyHandler.isDown(KeyEvent.VK_MINUS)) {
            areaSize.setNauticalMiles(getAreaSize().inNauticalMiles() + 50);
            map = null;
        }

        if (areaSize.inNauticalMiles() < 50) {
            areaSize.setNauticalMiles(50);
        }
    }

    /**
     * Renders the tactical map on the main window
     * @param graphics Graphics object reference
     * @param dt Time elapsed from previous time step
     */
    @SneakyThrows
    @Override
    public void render(Graphics2D graphics, double dt) {
        processKeyPress();
        drawMap(graphics);
    }
}
