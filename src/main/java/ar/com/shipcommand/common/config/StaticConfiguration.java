package ar.com.shipcommand.common.config;

import ar.com.shipcommand.common.CommonConstants;

/**
 * Static configuration for the game
 */
public class StaticConfiguration {
    /**
     * Target frame rate for the main window
     */
    public static final double TARGET_FRAME_RATE = 20;
    /**
     * Target physics step size
     */
    public static final double TARGET_PHYSICS_STEP_RATE = 60;
    /**
     * Buffering strategy for render main window
     */
    public static final int BUFFERING_STRATEGY = CommonConstants.DOUBLE_BUFFERING;
    /**
     * Main window's title
     */
    public static final String MAIN_WINDOW_TITLE = "Ship Command";
    /**
     * Main windows width
     */
    public static final int MAIN_WINDOW_WIDTH = 1360;
    /**
     * Main windows height
     */
    public static final int MAIN_WINDOW_HEIGHT = 760;
    /**
     * Heightmap file
     */
    public static final String HEIGHT_MAP_FILE = "./src/main/resources/GEBCO_2014_2D.nc";
            //"./src/main/resources/GRIDONE_2D.nc";
    /**
     * Height variable name inside the heightmap file
     */
    public static final String HEIGHT_MAP_VARIABLE_NAME = "elevation";
    /**
     * Latitude dimension inside the heightmap file
     */
    public static final String HEIGHT_MAP_LATITUDE_DIMENSION_NAME = "lat";
    /**
     * Longitude dimension inside the heightmap file
     */
    public static final String HEIGHT_MAP_LONGITUDE_DIMENSION_NAME = "lon";
}
