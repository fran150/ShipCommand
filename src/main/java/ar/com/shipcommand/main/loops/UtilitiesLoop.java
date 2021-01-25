package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.input.MouseHandler;

/**
 * Handles different utilities that needs to update state on each game frame.
 */
public class UtilitiesLoop extends GameLoop {
    @Override
    public boolean loop(double dt, long time) {
        MouseHandler.update();
        return true;
    }
}
