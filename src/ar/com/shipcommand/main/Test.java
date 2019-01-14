package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderizable;

import java.awt.*;

public class Test implements IGameObject, IRenderizable {
    public void timestep(double dt) {
    }

    public void render(double dt, Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.fillRect(100,100, 640,480);
    }
}
