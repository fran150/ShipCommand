package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.main.loops.GameLoopsManager;
import ar.com.shipcommand.ui.map.Map;
import ar.com.shipcommand.ui.tactical.Tactical;
import lombok.SneakyThrows;

/**
 * User interface manager
 */
public class UIManager {
    /**
     * Main map
     */
    private static Renderable mainMap;

    /**
     * Gets the current main map
     * @return current main map
     */
    private static Renderable getMainMap() {
        return mainMap;
    }

    /**
     * Initialize the user interface
     */
    @SneakyThrows
    public static void initialize() {
        mainMap = new Tactical();
        GameLoopsManager.getGraphicsLoop().add(mainMap);
    }
}
