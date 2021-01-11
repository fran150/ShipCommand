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
    public static double EARTH_RADIUS = 6371e3;
    /**
     * Min height of earth (max depth)
     */
    public static long EARTH_MIN_HEIGHT = -11000;
    /**
     * Max height of earth
     */
    public static long EARTH_MAX_HEIGHT = 8500;

}
