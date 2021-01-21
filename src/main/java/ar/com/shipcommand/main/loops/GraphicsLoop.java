package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.common.config.StaticConfiguration;
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
public class GraphicsLoop extends GameLoop {
    /**
     * Time for each frame needed to reach the desired frame rate
     */
    private static final double FRAME_TIME = 1.0 / StaticConfiguration.TARGET_FRAME_RATE;

    /**
     * List of objects to render
     */
    private final Set<Renderable> renderables;

    /**
     * Current graphics step size in seconds
     */
    private double graphicStep;

    /**
     * Creates a new graphic loop
     */
    public GraphicsLoop() {
        graphicStep = 0.0;
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

    @Override
    public boolean loop(double dt, long time) {
        // Add the elapsed time to the current step
        graphicStep += dt;

        // If the current step is more than the target frame time, draw the frame
        // (This actually controls the frame rate)
        if (graphicStep >= FRAME_TIME) {
            renderFrame(graphicStep);
            graphicStep = 0;

            return true;
        } else {
            return false;
        }
    }
}
