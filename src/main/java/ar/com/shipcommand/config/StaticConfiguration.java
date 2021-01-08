package ar.com.shipcommand.config;

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
}
