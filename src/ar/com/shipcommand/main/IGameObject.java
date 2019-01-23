package ar.com.shipcommand.main;

/**
 * Interface for objects whose state must be updated in each of the game's time steps
 */
public interface IGameObject {
    /**
     * Method called when needed to update the object's state for this time step
     *
     * @param dt Time passed since last update
     */
    void timeStep(double dt);
}
