package ar.com.shipcommand.main;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a window in the game
 */
public class Window extends Canvas {

    /**
     * Creates a centered window in the game
     *
     * @param width Width of the window
     * @param height Height of the window
     * @param title Title of the window
     * @param game Game object
     */
    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);

        Dimension dimension = new Dimension(width, height);

        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(game);

        frame.setVisible(true);
    }
}
