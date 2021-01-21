package ar.com.shipcommand.main.loops;

import ar.com.shipcommand.input.MouseHandler;

public class UtilitiesLoop extends GameLoop {
    @Override
    public boolean loop(double dt, long time) {
        MouseHandler.update();
        return true;
    }
}
