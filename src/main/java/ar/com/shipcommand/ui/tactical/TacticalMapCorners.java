package ar.com.shipcommand.ui.tactical;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.geo.Geo2DPosition;
import ar.com.shipcommand.geo.Geo2DReadonlyPosition;
import ar.com.shipcommand.physics.magnitudes.Bearing;
import ar.com.shipcommand.physics.magnitudes.Distance;
import lombok.Getter;

/**
 * Calculate the geographical position of the corners of the tactical map given
 * the center coordinates and the current zoom level
 */
class TacticalMapCorners {
    private final Geo2DPosition upperLeft;
    private final Geo2DPosition upperRight;
    private final Geo2DPosition lowerLeft;
    private final Geo2DPosition lowerRight;

    /**
     * Creates an object and calculates the geographical position of the tactical map corners
     * given the specified center coordinate, area size and height / width ratio of the
     * window
     * @param center Coordinates at the center of the map
     * @param areaSize Size of the area to show
     * @param heightWidthRatio Height / width ratio of the window
     */
    public TacticalMapCorners(Geo2DPosition center, Distance areaSize, double heightWidthRatio) {
        // Gets the width and height from the center to the border of the area to show
        double width = areaSize.inMeters() / 2.0;
        double height = (areaSize.inMeters() * heightWidthRatio) / 2.0;

        // Calculates the size of the diagonal by using pythagoras formula
        double sidesSquared = Math.pow(width, 2) + Math.pow(height, 2);
        Distance diagonalSize = new Distance(Math.sqrt(sidesSquared));

        // Gets the angle between the center and the upper right corner
        double angle = Math.toDegrees(Math.atan(height / width));

        // Set all pointers at the center position
        upperLeft = new Geo2DPosition(center);
        upperRight = new Geo2DPosition(center);
        lowerLeft = new Geo2DPosition(center);
        lowerRight = new Geo2DPosition(center);

        // Move all pointers at the different corners using the calculated angle + the
        // diagonal distance
        upperLeft.move(new Bearing(CommonConstants.WEST_HEADING + angle), diagonalSize);
        upperRight.move(new Bearing(CommonConstants.EAST_HEADING - angle), diagonalSize);
        lowerLeft.move(new Bearing(CommonConstants.WEST_HEADING - angle), diagonalSize);
        lowerRight.move(new Bearing(CommonConstants.EAST_HEADING + angle), diagonalSize);
    }

    /**
     * Get the upper left corner position
     * @return Geographical position of the tactical map's upper left corner
     */
    public Geo2DReadonlyPosition getUpperLeft() {
        return upperLeft.asReadOnly();
    }

    /**
     * Get the upper right corner position
     * @return Geographical position of the tactical map's upper right corner
     */
    public Geo2DReadonlyPosition getUpperRight() {
        return upperRight.asReadOnly();
    }

    /**
     * Get the lower left corner position
     * @return Geographical position of the tactical map's lower left corner
     */
    public Geo2DReadonlyPosition getLowerLeft() {
        return lowerLeft.asReadOnly();
    }

    /**
     * Get the lower right corner position
     * @return Geographical position of the tactical map's lower right corner
     */
    public Geo2DReadonlyPosition getLowerRight() {
        return lowerRight.asReadOnly();
    }
}
