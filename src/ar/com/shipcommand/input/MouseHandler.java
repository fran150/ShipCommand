package ar.com.shipcommand.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;

/**
 * Handles mouse input
 */
public class MouseHandler extends MouseAdapter {
    private static HashSet<Integer> pressed = new HashSet<>();
    private static int x = 0;
    private static int y = 0;
    private static int wheel = 0;

    /**
     * Called when a mouse button is pressed
     *
     * @param e Mouse event data
     */
    public void mousePressed(MouseEvent e) {
        pressed.add(e.getButton());
    }

    /**
     * Called when a mouse button is released
     *
     * @param e Mouse event data
     */
    public void mouseReleased(MouseEvent e) {
        pressed.remove(e.getButton());
    }

    /**
     * Called when the mouse moves over the window
     *
     * @param e Mouse event data
     */
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    /**
     * Called when the mouse is dragged over the window
     *
     * @param e Mouse event data
     */
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    /**
     * Called when the mouse wheel is scrolled
     *
     * @param e Mouse event data
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheel += e.getWheelRotation();
    }

    /**
     * Returns the current X position of the cursor
     *
     * @return X position of the cursor
     */
    public static int getX() {
        return x;
    }

    /**
     * Returns the current Y position of the cursor
     *
     * @return Y position of the cursor
     */
    public static int getY() {
        return y;
    }

    /**
     * Returns the current scroll position of the mouse wheel
     *
     * @return Scroll position of the wheel: negative values if the mouse wheel was rotated up/away from the user,
     * and positive values if the mouse wheel was rotated down/ towards the user
     */
    public static int getWheel() {
        return wheel;
    }

    /**
     * Returns if any button in the mouse is pressed
     *
     * @return true if the mouse button is pressed
     */
    public static boolean buttonPressed() {
        return pressed.size() > 0;
    }

    /**
     * Returns true if the specified button is pressed
     *
     * @param button Number of button to check
     * @return True if the specified button is pressed
     */
    public static boolean isPressed(int button) {
        return pressed.contains(button);
    }

    /**
     * Returns true if the mouse wheel was moved this time step
     *
     * @return True if the mouse wheel was moved
     */
    public static boolean isWheelMoved() {
        return wheel != 0;
    }

    /**
     * Updates the mouse state for the next time step
     */
    public static void update() {
        wheel = 0;
    }
}
