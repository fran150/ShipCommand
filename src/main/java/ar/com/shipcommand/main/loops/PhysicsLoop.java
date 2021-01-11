package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.common.CommonConstants;
import ar.com.shipcommand.common.config.StaticConfiguration;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.GameObject;
import ar.com.shipcommand.utils.TimeUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Main game loop implementation
 */
public class PhysicsLoop implements Runnable {
    /**
     * Time for each physics step
     */
    private static final double FRAME_TIME = 1.0 / StaticConfiguration.TARGET_PHYSICS_STEP_RATE;

    /**
     * List of game objects
     */
    private final Set<GameObject> gameObjects;

    /**
     * Physics frames per second
     */
    private int fps;

    /**
     * Creates a new physics loop
     */
    public PhysicsLoop() {
        gameObjects = new HashSet<>();
    }

    /**
     * Adds the specified object to the physics loop, it's state will be updated
     * on each time step
     * @param object Game object to be added
     */
    public void add(GameObject object) {
        gameObjects.add((GameObject) object);
    }

    /**
     * Removes the specified object from the physics loop. It's state will
     * not be updated anymore
     * @param object Game object to remove.
     */
    public void remove(GameObject object) {
        gameObjects.remove(object);
    }

    /**
     * Gets the physics frames per second
     * @return Physics frames per second
     */
    public int getFps() {
        return fps;
    }

    /**
     * Call timestep method on all game objects to make them update it's state
     */
    private void timestep() {
        // Step all game objects state
        for (GameObject gameObject : gameObjects) {
            gameObject.timeStep(FRAME_TIME);
        }
    }

    /**
     * Update all the physics objects in the game
     */
    @Override
    public void run() {
        // Time step size
        double dt;
        // Current physics time step size
        double physicsStep = 0;
        // Store the current time to calculate the time step
        long lastTime = System.nanoTime();

        // Store the current time for the physics FPS calculation
        long fpsTimer = System.currentTimeMillis();
        // Number of calculated frames inside the current second
        int numberOfCalculatedFrames = 0;

        while (Game.isRunning()) {
            // Calculate elapsed time and reset last time
            long now = System.nanoTime();
            dt = TimeUtils.getElapsedTime(lastTime, now);
            lastTime = now;

            if (Game.isPhysicsRunning()) {
                // Add the time to the current physics step
                physicsStep += dt;

                // If the current step is more than the target frame time, calculate the frame
                // (This actually controls the frame rate)
                if (physicsStep >= FRAME_TIME) {
                    // Remove used time from remaining physics time to maintain a fixed time step
                    physicsStep -= FRAME_TIME;
                    timestep();
                    numberOfCalculatedFrames++;
                }
            }

            if (TimeUtils.aSecondHasPassedFrom(fpsTimer)) {
                fpsTimer += CommonConstants.MILLISECONDS_IN_ONE_SECOND;
                fps = numberOfCalculatedFrames;
                System.out.printf("Physics FPS: %d\n", fps);
                numberOfCalculatedFrames = 0;
            }
        }
    }
}
