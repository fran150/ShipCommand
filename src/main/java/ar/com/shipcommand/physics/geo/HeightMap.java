package ar.com.shipcommand.physics.geo;

import ucar.ma2.*;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;

/**
 * Contains information of the world's heights and depths
 */
public class HeightMap {
    // Heightmap file
    private NetcdfFile file;
    // Z variable of the NetCDF file
    private Variable z;

    // Width and height of the heightmap grid
    private static long GRID_WIDTH = 21601;
    private static long GRID_HEIGHT = 10801;

    /**
     * Creates a new height map object that allows to read the height of a given position
     *
     * @throws IOException Produced when the system can't read the heights file
     */
    public HeightMap() throws IOException, InvalidRangeException {
        file = NetcdfFile.openInMemory("./src/main/resources/GRIDONE_1D.nc");
        z = file.findVariable("z");
    }

    /**
     * Converts to the given geographical position to the corresponding heights grid index
     *
     * @param position Position to convert
     * @return index in the heightmap file
     */
    protected long posToGrid(Geo2DPosition position) {
        return Math.round((Math.round((90 - position.getLat()) * 60) * GRID_WIDTH) + ((position.getLon() + 180) * 60));
    }

    /**
     * Returns the height at the given position
     *
     * @param position Position from where to get the height
     * @return Height at the given position
     * @throws IOException Error when reading the elevations file
     * @throws InvalidRangeException Error when an invalid position is entered
     */
    public int getHeight(Geo2DPosition position)
            throws IOException, InvalidRangeException {
        long index = posToGrid(position);
        Array data = z.read(index + "");
        return data.getInt(0);
    }

    /**
     * Returns an array of heights in the same latitude
     *
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
        long stepSize = Math.round(Math.floor(step * 60));
        if (stepSize < 1) stepSize = 1;


        // If the geographical position is inside bounds
        if (posLeft.getLon() < posRight.getLon()) {
            // Transform the geographical position of the pointers to an index on the heightmap grid
            long start = posToGrid(posLeft);
            long end = posToGrid(posRight);

            // Read the row of heights for this line of pixels
            Array heights = z.read(start + ":" + end + ":" + stepSize);

            return new Heights(heights);
        } else {
            // If the heights line is out of bounds
            // Retrieve the heights from the start position to the last longitude
            long start1 = posToGrid(posLeft);
            long end1 = posToGrid(new Geo2DPosition(posLeft.getLat(), 180));

            // Then retrieve the heights from the first longitude to the end position
            long start2 = posToGrid(new Geo2DPosition(posRight.getLat(), -180));
            long end2 = posToGrid(posRight);

            // Read the row of heights for this line of pixels
            Array heights1 = z.read(start1 + ":" + end1 + ":" + step);
            Array heights2 = z.read(start2 + ":" + end2 + ":" + step);

            return new Heights(heights1, heights2);
        }
    }
}
