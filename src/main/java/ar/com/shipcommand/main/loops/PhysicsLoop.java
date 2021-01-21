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
public class PhysicsLoop extends GameLoop {
    /**
     * Time for each physics step
     */
    private static final double FRAME_TIME = 1.0 / StaticConfiguration.TARGET_PHYSICS_STEP_RATE;

    /**
     * List of game objects
     */
    private final Set<GameObject> gameObjects;

    /**
     * Current physics time step size
     */
    double physicsStep;

    /**
     * Creates a new physics loop
     */
    public PhysicsLoop() {
        physicsStep = 0.0;
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
    public boolean loop(double dt, long time) {
        // Add the time to the current physics step
        physicsStep += dt;

        // If the current step is more than the target frame time, calculate the frame
        if (physicsStep >= FRAME_TIME) {
            // Remove used time from remaining physics time to maintain a fixed time step
            physicsStep -= FRAME_TIME;
            timestep();

            return true;
        } else {
            return false;
        }
    }
}
