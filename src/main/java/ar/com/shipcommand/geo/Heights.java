package ar.com.shipcommand.geo;

import ucar.ma2.Array;

/**
 * Heights list retrieved from heightmap file
 */
public class Heights {
    /**
     * Primary array of heights
     */
    private final Array primary;
    /**
     * Secondary array of heights
     */
    private final Array secondary;

    /**
     * Creates a new array of heights
     * @param primary Primary list of data
     */
    Heights(Array primary) {
        this.primary = primary;
        this.secondary = null;
    }

    /**
     * Creates a new array of heights. (Used when two segments of heights are needed)
     * If two segments of data are specified the primary data segment is returned first o the
     * getHeight function and then the secondary
     * @param primary Primary data segment
     * @param secondary Secondary data segment
     */
    Heights(Array primary, Array secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    /**
     * Returns the number of heights in the heightmap
     * @return Number of items in the heightmap
     */
    public long getSize() {
        return secondary != null ? primary.getSize() + secondary.getSize() : primary.getSize();
    }

    /**
     * Return the height at the given index
     * @param index Index of the height to obtain
     * @return Height in meters
     */
    public int getHeight(int index) {
        // If a secondary list is specified and
        // the index is larger than the primary size then return from the secondary segment
        if (secondary != null && index > primary.getSize() - 1) {
            return secondary.getInt((int) (index - primary.getSize()));
        } else {
            // If not return from the primary segment
            return primary.getInt(index);
        }
    }
}
