package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.main.loops.GameLoopsManager;
import ar.com.shipcommand.main.loops.PhysicsLoop;
import ar.com.shipcommand.main.windows.MainWindow;
import ar.com.shipcommand.main.windows.WindowManager;
import ar.com.shipcommand.ui.Tactical;
import ar.com.shipcommand.ui.UIManager;
import ucar.ma2.InvalidRangeException;

import java.io.IOException;

/**
 * Main game class
 */
public class Game {
    private static boolean running = false;
    private static boolean physicsRunning = false;

    /**
     *
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
     * Return if game is running
     * @return True game is running
     */
    public static boolean isRunning() {
        return running;
    }

    /**
     * Return if physics engine is running.
     * When physics engine is stopped, only the render loop is processed
     * @return True if the physics engine is running
     */
    public static boolean isPhysicsRunning() {
        return physicsRunning;
    }

    /**
     * Run the physics engine
     */
    public static void runPhysics() {
        physicsRunning = true;
    }

    /**
     * Stop the physics engine
     */
    public static void stopPhysics() {
        physicsRunning = false;
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
        GameLoopsManager.stop();
        running = false;
    }
}