package ar.com.shipcommand.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;

/**
 * Handles mouse input
 */
public class MouseHandler extends MouseAdapter {
    // Tracks the buttons pressed on the mouse
    private static final HashSet<Integer> pressed = new HashSet<>();

    // Current mouse positions on the screen
    private static int currentX = 0;
    private static int currentY = 0;

    // Current wheel status
    private static int wheel = 0;

    /**
     * Called when a mouse button is pressed
     * @param event Mouse event data
     */
    @Override
    public void mousePressed(MouseEvent event) {
        // Adds the pressed button to the list
        pressed.add(event.getButton());
    }

    /**
     * Called when a mouse button is released
     * @param event Mouse event data
     */
    @Override
    public void mouseReleased(MouseEvent event) {
        // Removes the pressed button from the list
        pressed.remove(event.getButton());
    }

    /**
     * Called when the mouse moves over the window
     * @param event Mouse event data
     */
    public void mouseMoved(MouseEvent event) {
        // When the mouse is moved the current position is updated
        currentX = event.getX();
        currentY = event.getY();
    }

    /**
     * Called when the mouse is dragged over the window
     * @param event Mouse event data
     */
    public void mouseDragged(MouseEvent event) {
        // When the mouse is dragged the current position is updated
        currentX = event.getX();
        currentY = event.getY();
    }

    /**
     * Called when the mouse wheel is scrolled. It updates the wheel property with a number
     * representing how much the wheel was moved. The sign represents the movement direction.
     * @param e Mouse event data
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheel += e.getWheelRotation();
    }

    /**
     * Returns the current X position of the cursor
     * @return X position of the cursor
     */
    public static int getCurrentX() {
        return currentX;
    }

    /**
     * Returns the current Y position of the cursor
     * @return Y position of the cursor
     */
    public static int getCurrentY() {
        return currentY;
    }

    /**
     * Returns the current scroll position of the mouse wheel
     * @return Scroll position of the wheel: negative values if the mouse wheel was rotated up/away from the user,
     * and positive values if the mouse wheel was rotated down/ towards the user
     */
    public static int getWheel() {
        return wheel;
    }

    /**
     * Returns if any button in the mouse is pressed
     * @return true if the mouse button is pressed
     */
    public static boolean isAnyButtonPressed() {
        return pressed.size() > 0;
    }

    /**
     * Returns true if the specified button is pressed
     * @param button Number of button to check
     * @return True if the specified button is pressed
     */
    public static boolean isButtonPressed(int button) {
        return pressed.contains(button);
    }

    /**
     * Returns true if the mouse wheel was moved this time step
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
