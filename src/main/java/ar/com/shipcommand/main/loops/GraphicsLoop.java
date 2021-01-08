package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.config.StaticConfiguration;
import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.windows.MainWindow;
import ar.com.shipcommand.main.windows.WindowManager;
import ar.com.shipcommand.utils.TimeUtils;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashSet;
import java.util.Set;

/**
 * The graphic loop is the class that controls frame rate and iterates over all objects and
 * manges its rendering on the main window
 */
public class GraphicsLoop implements Runnable {
    /**
     * Time for each frame
     */
    private static final double FRAME_TIME = 1.0 / StaticConfiguration.TARGET_FRAME_RATE;

    /**
     * List of objects to render
     */
    private final Set<Renderable> renderables;

    /**
     * Current frames per second for the graphic loop
     */
    private int fps;

    /**
     * Creates a new graphic loop
     */
    public GraphicsLoop() {
        fps = 0;
        renderables = new HashSet<>();
    }

    /**
     * Adds a renderable object to the graphic loop. This object will be picked up and rendered
     * on the next loop.
     * @param renderable Renderable to add
     */
    public void add(Renderable renderable) {
        renderables.add(renderable);
    }

    /**
     * Removes a renderable object from the graphic loop. This object will be no longer rendered.
     * @param renderable Renderable to remove
     */
    public void remove(Renderable renderable) {
        renderables.remove(renderable);
    }

    public int getFPS() {
        return fps;
    }

    /**
     * Renders the frame
     * @param dt Delta time from the previous frame
     */
    private void renderFrame(double dt) {
        // Gets the game's main window and buffer strategy
        MainWindow mainWindow = WindowManager.getMainWindow();
        BufferStrategy bufferStrategy = mainWindow.getOrCreateBufferStrategy();

        // Get the graphics context
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        // Render all objects
        for (Renderable object : renderables) {
            object.render(graphics, dt);
        }

        // Dispose graphics context
        graphics.dispose();

        // Show the created buffer
        bufferStrategy.show();
    }



    /**
     * Runs the graphics loop while the game is running
     */
    @Override
    public void run() {
        // Time step size
        double dt;
        // Current graphics step size in seconds
        double graphicStep = 0;
        // Store the current time to calculate the time step
        long lastTime = System.nanoTime();

        // Used for timing seconds to calculate FPS
        long fpsTimer = System.currentTimeMillis();
        // Number of rendered frames inside the current second
        int numberOfRenderedFrames = 0;

        while (Game.isRunning()) {
            // Calculate elapsed time and reset last time
            long now = System.nanoTime();
            dt = TimeUtils.getElapsedTime(lastTime, now);
            lastTime = now;

            // Add the elapsed time to the current step
            graphicStep += dt;

            // If the current step is more than the target frame time, draw the frame
            // (This actually controls the frame rate)
            if (graphicStep >= FRAME_TIME) {
                renderFrame(graphicStep);
                numberOfRenderedFrames++;
                graphicStep = 0;
            }

            // If a second has passed calculate FPS
            if (TimeUtils.aSecondHasPassedFrom(fpsTimer)) {
                fpsTimer += CommonConstants.MILLISECONDS_IN_ONE_SECOND;
                fps = numberOfRenderedFrames;
                System.out.printf("FPS: %d\n", fps);
                numberOfRenderedFrames = 0;
            }
        }
    }
}
