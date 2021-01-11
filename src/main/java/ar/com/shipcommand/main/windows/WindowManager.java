package ar.com.shipcommand.main.windows;

import ar.com.shipcommand.common.config.StaticConfiguration;

/**
 * Manages all the game's windows
 */
public class WindowManager {
    private static final MainWindow mainWindow;

    static {
        mainWindow = MainWindow.builder()
                .width(StaticConfiguration.MAIN_WINDOW_WIDTH)
                .height(StaticConfiguration.MAIN_WINDOW_HEIGHT)
                .title(StaticConfiguration.MAIN_WINDOW_TITLE)
                .build();
    }

    /**
     * Initializes the game windows on start
     */
    public static void initialize() {
        mainWindow.showWindow();
    }

    /**
     * Gets the main window
     * @return Game's main window
     */
    public static MainWindow getMainWindow() {
        return mainWindow;
    }
}
