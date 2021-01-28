package ar.com.shipcommand.geo;

import ar.com.shipcommand.common.config.StaticConfiguration;
import ucar.ma2.*;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;

/**
 * Contains information of the world's heights and depths
 */
public class HeightMap {
    /**
     * Elevations variable read from the file
     */
    private final Variable elevation;

    /**
     * Width of the grid in the heightmap file
     */
    private final long gridWidth;
    /**
     * Height of the grid in the heightmap file
     */
    private final long gridHeight;

    /**
     * Creates a new height map object that allows to read the height of a given position
     * @throws IOException Produced when the system can't read the heights file
     */
    public HeightMap() throws IOException {
        NetcdfFile file = NetcdfFile.open(StaticConfiguration.HEIGHT_MAP_FILE);
        elevation = file.findVariable(StaticConfiguration.HEIGHT_MAP_VARIABLE_NAME);

        gridWidth = file.findDimension(StaticConfiguration.HEIGHT_MAP_LONGITUDE_DIMENSION_NAME)
                .getLength();
        gridHeight = file.findDimension(StaticConfiguration.HEIGHT_MAP_LATITUDE_DIMENSION_NAME)
                .getLength();
    }

    /**
     * Converts the given geographical position to a grid latitude
     * @param position Position to convert
     * @return latitude dimension on the grid
     */
    private long posToGridLat(Geo2DPosition position) {
        long lat = (gridHeight - Math.round((90 - position.getLat()) * (gridHeight / 180.0)));
        return Math.min(gridHeight - 1, lat);
    }

    /**
     * Converts the given geographical position to a grid longitude
     * @param position Position to convert
     * @return longitude dimension on the grid
     */
    private long posToGridLon(Geo2DPosition position) {
        long lon = Math.round((position.getLon() + 180) * (gridWidth / 360.0));
        return Math.min(gridWidth - 1, lon);
    }

    /**
     * Returns the height at the given position
     * @param position Position from where to get the height
     * @return Height at the given position
     * @throws IOException Error when reading the elevations file
     * @throws InvalidRangeException Error when an invalid position is entered
     */
    public int getHeight(Geo2DPosition position)
            throws IOException, InvalidRangeException {
        long lat = posToGridLat(position);
        long lon = posToGridLon(position);

        String section = lat + "," + lon;

        Array data = elevation.read(section);
        return data.getInt(0);
    }

    /**
     * Returns a section specification used to read from the file a line of height
     * on the given latitude an between the specified longitudes
     * @param lat Latitude to read from the file
     * @param lonStart Longitude from where to start reading
     * @param lonEnd Longitude where end reading
     * @return Section specification to be used to read from the file
     */
    private String getSectionSpecification(long lat, long lonStart, long lonEnd) {
        return lat + "," + lonStart + ":" + lonEnd;
    }

    /**
     * Returns a section specification used to read from the file a line of height
     * on the given latitude an between the specified longitudes and stepping in the
     * specified interval
     * @param lat Latitude to read from the file
     * @param lonStart Longitude from where to start reading
     * @param lonEnd Longitude where end reading
     * @param stepSize Number of data points to skip between each value
     * @return Section specification to be used to read from the file
     */
    private String getSectionSpecification(long lat, long lonStart, long lonEnd, long stepSize) {
        return getSectionSpecification(lat, lonStart, lonEnd) + ":" + stepSize;
    }

    /**
     * Returns an array of heights in the same latitude
     * @param lat Latitude of heights
     * @param left Leftmost longitude to read
     * @param right Rightmost longitude to read
     * @param step Returns the data separated by this longitude in degrees
     * @return Heights of the specified section
     * @throws IOException Error when reading the elevations file
     * @throws InvalidRangeException Error when the specified position is invalid
     */
    public Heights getHeightsLine(double lat, double left, double right, double step)
            throws IOException, InvalidRangeException {
        Geo2DPosition posLeft = new Geo2DPosition(lat, left);
        Geo2DPosition posRight = new Geo2DPosition(lat, right);

        // Calculate the step size to sample as many heights as pixels in the screen row
        long stepSize = Math.round(Math.floor(step * (gridWidth / 360.0)));
        if (stepSize < 1) stepSize = 1;


        // If the geographical position is inside bounds
        if (posLeft.getLon() < posRight.getLon()) {
            // Transform the geographical position of the pointers to an index on the heightmap grid
            long start = posToGridLon(posLeft);
            long end = posToGridLon(posRight);
            long gridLat = posToGridLat(posLeft);

            // Read the row of heights for this line of pixels
            Array heights = elevation.read(getSectionSpecification(gridLat, start, end, stepSize));

            return new Heights(heights);
        } else {
            // If the heights line is out of bounds
            // Retrieve the heights from the start position to the last longitude
            long start1 = posToGridLon(posLeft);
            long end1 = posToGridLon(new Geo2DPosition(posLeft.getLat(), 180));

            // Then retrieve the heights from the first longitude to the end position
            long start2 = posToGridLon(new Geo2DPosition(posRight.getLat(), -180));
            long end2 = posToGridLon(posRight);

            long gridLatitude = posToGridLat(posLeft);

            // Read the row of heights for this line of pixels
            Array heights1 = elevation.read(getSectionSpecification(gridLatitude, start1, end1, stepSize));
            Array heights2 = elevation.read(getSectionSpecification(gridLatitude, start2, end2, stepSize));

            return new Heights(heights1, heights2);
        }
    }
}
