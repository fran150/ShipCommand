package ar.com.shipcommand.physics.geo;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;

public class HeightMap {
    // Heightmap file
    NetcdfFile file;
    // Z variable of the NetCDF file
    Variable z;

    // Width and height of the heightmap grid
    private static long GRID_WIDTH = 21601;
    private static long GRID_HEIGHT = 10801;

    public HeightMap() {

        // Read the NetCDF file and get the Z variable
        try {
            file = NetcdfFile.open("./src/main/resources/GRIDONE_1D.nc");
        } catch (IOException e) {
            e.printStackTrace();
        }

        z = file.findVariable("z");
    }

    /**
     * Converts to the given geographical position to the corresponding grid index
     *
     * @param position Position to convert
     * @return index in the heightmap file
     */
    protected long posToGrid(Geo2DPosition position) {
        return Math.round((Math.round((90 - position.getLat()) * 60) * GRID_WIDTH) + ((position.getLon() + 180) * 60));
    }

    public Heights getHeights(double lat, double left, double right, long step) {
        Geo2DPosition posLeft = new Geo2DPosition(lat, left);
        Geo2DPosition posRight = new Geo2DPosition(lat, right);

        // Transform the geographical position of the pointers to an index on the heightmap grid
        long start = posToGrid(posLeft);
        long end = posToGrid(posRight);

        if (start < end) {
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

            return new Heights(read);
        } else {
            // Array of heights obtained from the heightmap
            Array read1 = null;
            Array read2 = null;

            long start1 = end;
            long end1 = posToGrid(new Geo2DPosition(posRight.getLat(), 180));
            long start2 = posToGrid(new Geo2DPosition(posLeft.getLat(), -180));
            long end2 = start;


            try {
                // Read the row of heights for this line of pixels
                read1 = z.read(start1 + ":" + end1 + ":" + step);
                read1 = z.read(start2 + ":" + end2 + ":" + step);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }

            return new Heights(read1, read2);
        }
    }
}
