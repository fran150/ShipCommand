package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;

import java.awt.*;

public class Test implements IGameObject, IRenderable {
    double x = 100;

    public void timestep(double dt) {
        x += dt;
    }

    public void render(Graphics graphics, double dt) {
        Double d = new Double(x);
        graphics.setColor(Color.green);
        graphics.drawRect(d.intValue() ,d.intValue(), 640,480);
    }
}
