package ar.com.shipcommand.main;

import ar.com.shipcommand.main.loops.GameLoopsManager;
import ar.com.shipcommand.main.windows.WindowManager;
import ar.com.shipcommand.ui.UIManager;

/**
 * Main game class
 */
public class Game {
    // Indicates that the game is running
    private static boolean running;

    /**
     * Main entry point for the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        initialize(args);
        start();
    }

    /**
     * Initialize main window and game loop
     */
    protected static void initialize(String[] args) {
        WindowManager.initialize();
        UIManager.initialize();
    }

    /**
     * Returns if the game is running
     * @return true if the game is running
     */
    public static boolean isRunning() {
        return running;
    }

    /**
     * Starts the main game thread
     */
    public static synchronized void start() {
        running = true;
        GameLoopsManager.start();
    }

    /**
     * Stops the main game thread
     */
    public static synchronized void stop() {
        running = false;
        GameLoopsManager.stop();
    }
}