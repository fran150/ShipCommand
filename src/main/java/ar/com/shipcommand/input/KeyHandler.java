package ar.com.shipcommand.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles keyboard input over a window
 */
public class KeyHandler extends KeyAdapter {
    /**
     * Set of currently pressed keys
     */
    private static final Set<Integer> pressed = new HashSet<>();

    /**
     * Called when a key is pressed over the window
     * @param event Key event
     */
    public void keyPressed(KeyEvent event) {
        pressed.add(event.getKeyCode());
    }

    /**
     * Called when a key is released
     * @param event Key event
     */
    public void keyReleased(KeyEvent event) {
        pressed.remove(event.getKeyCode());
    }

    /**
     * Returns if the specified key is currently pressed
     * @param keyCode Key code
     * @return True if the specified key is pressed
     */
    public static boolean isDown(int keyCode) {
        return pressed.contains(keyCode);
    }

    /**
     * Returns the pressed keys
     * @return Set of the pressed keys
     */
    public static Set<Integer> getPressedKeys() {
        return Collections.unmodifiableSet(pressed);
    }
}
