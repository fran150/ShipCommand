package ar.com.shipcommand.main.windows;

import ar.com.shipcommand.config.StaticConfiguration;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.input.MouseHandler;
import lombok.Builder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Represents a window in the game
 */
public class MainWindow extends Canvas {
    private final int width;
    private final int height;
    private final JFrame frame;

    /**
     * Creates the main game window
     * @param width Width of the window
     * @param height Height of the window
     * @param title Title of the window
     */
    @Builder
    public MainWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.frame = new JFrame(title);

        initWindowFrame(frame);
        initKeyboardHandler();
        initMouseHandler();
    }

    /**
     * Gets or creates a buffer strategy for the window
     * @return Buffer strategy for the window
     */
    public BufferStrategy getOrCreateBufferStrategy() {
        // Get a buffer strategy
        BufferStrategy bufferStrategy = this.getBufferStrategy();

        // If a buffer strategy is not found create one
        if (bufferStrategy == null) {
            this.createBufferStrategy(StaticConfiguration.BUFFERING_STRATEGY);
            return this.getBufferStrategy();
        } else {
            return bufferStrategy;
        }
    }

    /**
     * Returns the current width of the window
     * @return in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the current height of the window
     * @return in pixels
     */
    public int getHeight() {
        return height;
    }

    /**
     * Shows the window
     */
    public void showWindow() {
        frame.setVisible(true);
    }

    /**
     * Hides the window
     */
    public void hideWindow() {
        frame.setVisible(false);
    }

    /**
     * Init the specified window frame
     * @param frame Window's frame
     */
    private void initWindowFrame(JFrame frame) {
        Dimension dimension = new Dimension(width, height);

        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);
    }

    /**
     * Attaches a keyboard handler to the window
     */
    private void initKeyboardHandler() {
        addKeyListener(new KeyHandler());
    }

    /**
     * Attaches a mouse handler to the window
     */
    private void initMouseHandler() {
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }
}
