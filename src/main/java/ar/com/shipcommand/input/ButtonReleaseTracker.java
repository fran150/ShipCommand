package ar.com.shipcommand.input;

import lombok.*;

/**
 * Allows to track a button state and perform an action when the button is released
 */
public class ButtonReleaseTracker {
    // Stores the button being tracked
    private final int trackedButton;
    // Flag that shows if the button was pressed
    private boolean buttonWasPressed;

    /**
     * Creates a button release tracker that allow to execute some code when the specified
     * button is released
     * @param trackedButton Button number to track
     */
    public ButtonReleaseTracker(int trackedButton) {
        this.trackedButton = trackedButton;
    }

    /**
     * When the tracked button is release perform the action specified
     * @param action Action to execute once the button is depressed
     */
    public void onButtonRelease(Runnable action) {
        if (MouseHandler.isButtonPressed(trackedButton)) {
            buttonWasPressed = true;
        } else {
            if (buttonWasPressed) {
                buttonWasPressed = false;
                action.run();
            }
        }
    }
}
