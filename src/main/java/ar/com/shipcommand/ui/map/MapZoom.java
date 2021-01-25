package ar.com.shipcommand.ui.map;

import ar.com.shipcommand.geo.*;
import lombok.*;

import java.awt.image.*;

class MapZoom {
    // Map default coordinates
    private static final double DEFAULT_UPPER_LEFT_LAT = 90.0;
    private static final double DEFAULT_UPPER_LEFT_LON = -180.0;
    private static final double DEFAULT_LOWER_RIGHT_LAT = -90.0;
    private static final double DEFAULT_LOWER_RIGHT_LON = 180.0;

    // Upper left position of the area currently shown
    @Getter
    @Setter
    private Geo2DPosition upperLeft;
    // Lower right position of the area currently shown
    @Getter
    @Setter
    private Geo2DPosition lowerRight;

    // Zoom area width and height in degrees of latitude and longitude
    @Getter
    private double areaWidth;
    @Getter
    private double areaHeight;

    // Calculated number of degrees of lat and lon per pixel
    @Getter
    private double lonPerPixel;
    @Getter
    private double latPerPixel;

    // Map's width and height
    private final double mapWidth;
    private final double mapHeight;

    // Generated image
    @Getter
    @Setter
    private BufferedImage map;

    /**
     * Creates a new zoom object
     * @param mapWidth Map's width in pixels
     * @param mapHeight Map's height in pixels
     */
     MapZoom(double mapWidth, double mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        setDefaultZoomArea();
    }

    /**
     * Sets the zoom area to the default values (Full world view)
     */
    void setDefaultZoomArea() {
        Geo2DPosition upperLeft = new Geo2DPosition(DEFAULT_UPPER_LEFT_LAT, DEFAULT_UPPER_LEFT_LON);
        Geo2DPosition lowerRight = new Geo2DPosition(DEFAULT_LOWER_RIGHT_LAT, DEFAULT_LOWER_RIGHT_LON);

        setZoomArea(upperLeft, lowerRight);
    }

    /**
     * Sets a new zoom area defined by the latitude and longitude of the upper left and
     * lower right corners of the image to show
     * @param upperLeft Upper left corner coordinates of the map to show
     * @param lowerRight Lower right corner coordinates of the map to show
     */
    void setZoomArea(Geo2DPosition upperLeft, Geo2DPosition lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;

        // Calculate the area width in degrees given the upper left and lower right corners
        this.areaWidth = GeoTools.getLongitudeDifference(upperLeft, lowerRight);
        this.areaHeight = upperLeft.getLat() - lowerRight.getLat();

        // Calculate the number of degrees of lat and lon per pixel
        this.lonPerPixel = areaWidth / mapWidth;
        this.latPerPixel = areaHeight / mapHeight;

        this.map = null;
    }

    /**
     * Returns if the map is showing the default area
     * @return True if the map is showing the entire world
     */
    boolean isDefaultArea() {
        return upperLeft.getLat() == DEFAULT_UPPER_LEFT_LAT
                && upperLeft.getLon() == DEFAULT_UPPER_LEFT_LON
                && lowerRight.getLat() == DEFAULT_LOWER_RIGHT_LAT
                && lowerRight.getLon() == DEFAULT_LOWER_RIGHT_LON;
    }

    /**
     * Converts the given screen position to geographical position on the map
     * @param x X position on the screen
     * @param y Y position on the screen
     * @return Geographical position of corresponding point in the screen
     */
    Geo2DPosition screenToPos(int x, int y) {
        double lat = upperLeft.getLat() - (y * latPerPixel);
        double lon = (x * lonPerPixel) + upperLeft.getLon();

        return new Geo2DPosition(lat, lon);
    }
}
