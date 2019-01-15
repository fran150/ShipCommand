package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;

import java.awt.*;

public class Test implements IGameObject, IRenderable {
    public void timestep(double dt) {
    }

    public void render(Graphics graphics, double dt) {
        graphics.setColor(Color.green);
        graphics.fillRect(100,100, 640,480);
    }
}
