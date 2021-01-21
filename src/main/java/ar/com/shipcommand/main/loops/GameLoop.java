package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.utils.TimeUtils;

/**
 * Any of the game loops. This allows to implement a list of objects
 * that needs to be updated regularly as the time passes on the game.
 */
public abstract class GameLoop implements Runnable {
    private boolean paused;
    private int fps;

    /**
     * Returns if this game loop is running
     * @return false if the game loop is running
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pauses the game loop
     */
    public void pause() {
        paused = true;
    }

    /**
     * Unpauses the game loop
     */
    public void unPause() {
        paused = false;
    }

    /**
     * Returns the FPS for this game loop
     * @return Number of frames per second this game loop is rendering
     */
    public int getFps() {
        return fps;
    }

    /**
     * A thread will be run on this method to process the game loop.
     * It will call the initialize method, and then while the game is running and the loop is not
     * paused it will call the loop method
     */
    @Override
    public final void run() {
        // Time step size
        double dt;
        // Used to calculate each timestep
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

            if (!paused) {
                boolean rendered = loop(dt, now);

                if (rendered) {
                    numberOfRenderedFrames++;
                }

                // If a second has passed calculate FPS
                if (TimeUtils.aSecondHasPassedFrom(fpsTimer)) {
                    fpsTimer += CommonConstants.MILLISECONDS_IN_ONE_SECOND;
                    fps = numberOfRenderedFrames;
                    numberOfRenderedFrames = 0;
                }
            }
        }
    }

    public abstract boolean loop(double dt, long time);
}
