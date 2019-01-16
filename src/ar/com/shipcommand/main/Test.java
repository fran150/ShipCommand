package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.input.MouseHandler;
import org.jcp.xml.dsig.internal.MacOutputStream;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Test implements IGameObject, IRenderable {
    double x = 100;
    double y = 100;

    public void timestep(double dt) {
        if (KeyHandler.isDown(KeyEvent.VK_D)) {
            x += 5;
        }

        if (KeyHandler.isDown(KeyEvent.VK_W)) {
            y -= 5;
        }

        if (KeyHandler.isDown(KeyEvent.VK_A)) {
            x -= 5;
        }

        if (KeyHandler.isDown(KeyEvent.VK_S)) {
            y += 5;
        }

        if (MouseHandler.isPressed(1)) {
            x = MouseHandler.getX();
            y = MouseHandler.getY();
        }

        if (MouseHandler.isPressed(3)) {
            x += MouseHandler.getWheel() * 5;
        } else {
            if (MouseHandler.isWheelMoved()) {
                y += MouseHandler.getWheel() * 5;
            }
        }
    }

    public void render(Graphics2D graphics, double dt) {
        Double X = new Double(x);
        Double Y = new Double(y);

        graphics.setColor(Color.green);
        graphics.drawRect(X.intValue(), Y.intValue(), 100,100);
    }
}
