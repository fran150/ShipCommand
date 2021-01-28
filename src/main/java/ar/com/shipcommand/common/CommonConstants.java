package ar.com.shipcommand.common;

/**
 * Common constants for reuse through the code
 */
public class CommonConstants {
    /**
     * Number of nano seconds in a second
     */
    public static final double NANO_SECONDS_IN_A_SECOND = 1000000000;
    /**
     * Milliseconds in one second
     */
    public static final int MILLISECONDS_IN_ONE_SECOND = 1000;
    /**
     * Double buffering
     */
    public static final int DOUBLE_BUFFERING = 2;
    /**
     * Triple buffering
     */
    public static final int TRIPLE_BUFFERING = 3;
    /**
     * Earth radius
     */
    public static final double EARTH_RADIUS = 6371e3;
    /**
     * Min height of earth (max depth)
     */
    public static final long EARTH_MIN_HEIGHT = -11000;
    /**
     * Max height of earth
     */
    public static final long EARTH_MAX_HEIGHT = 8500;
    /**
     * Heading north
     */
    public static final double NORTH_HEADING = 0.0;
    /**
     * Heading south
     */
    public static final double SOUTH_HEADING = 180.0;
    /**
     * Heading east
     */
    public static final double EAST_HEADING = 90.0;
    /**
     * Heading west
     */
    public static final double WEST_HEADING = 270.0;
    /**
     * Heading north west
     */
    public static final double NORTH_WEST_HEADING = 315.0;
    /**
     * Heading north east
     */
    public static final double NORTH_EAST_HEADING = 45.0;
    /**
     * Heading south west
     */
    public static final double SOUTH_WEST_HEADING = 225;
    /**
     * Heading south east
     */
    public static final double SOUTH_EAST_HEADING = 135.0;
}
