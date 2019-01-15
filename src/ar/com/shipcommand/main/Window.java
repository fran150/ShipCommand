package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Represents a window in the game
 */
public class Window extends Canvas implements IRenderable {

    HashSet<IRenderable> renderables;

    /**
     * Creates a centered window
     *
     * @param width Width of the window
     * @param height Height of the window
     * @param title Title of the window
     */
    public Window(int width, int height, String title) {
        renderables = new HashSet<>();

        JFrame frame = new JFrame(title);

        Dimension dimension = new Dimension(width, height);

        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);

        frame.setVisible(true);
    }

    public void render(Graphics graphics, double dt) {
        for (IRenderable renderable : renderables) {
            renderable.render(graphics, dt);
        }
    }
}
