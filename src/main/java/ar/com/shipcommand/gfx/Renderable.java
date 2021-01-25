package ar.com.shipcommand.gfx;

import java.awt.*;

/**
 * Interface for objects that can be rendered on the screen
 */
public interface Renderable {
    /**
     * Method called when needed to update the object's render state
     * @param dt Time elapsed from previous time step
     * @param graphics Graphics object reference
     */
    void render(Graphics2D graphics, double dt);
}
