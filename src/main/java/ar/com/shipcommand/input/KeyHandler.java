package ar.com.shipcommand.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * Handles keyboard input over a window
 */
public class KeyHandler extends KeyAdapter {
    /**
     * Set of currently pressed keys
     */
    private static HashSet<Integer> pressed = new HashSet<>();

    /**
     * Called when a key is pressed over the window
     *
     * @param e Key event
     */
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyCode());
    }

    /**
     * Called when a key is released
     *
     * @param e Key event
     */
    public void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyCode());
    }

    /**
     * Returns if the specified key is currently pressed
     *
     * @param keyCode Key code
     * @return True if the specified key is pressed
     */
    public static boolean isDown(int keyCode) {
        return pressed.contains(keyCode);
    }
}
