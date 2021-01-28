package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.Renderable;
import ar.com.shipcommand.main.loops.GameLoopsManager;
import ar.com.shipcommand.ui.tactical.Tactical;
import lombok.SneakyThrows;

public class UIManager {
    private static Renderable mainMap;

    private static Renderable getMainMap() {
        return mainMap;
    }

    @SneakyThrows
    public static void initialize() {
        mainMap = new Tactical();
        GameLoopsManager.getGraphicsLoop().add(mainMap);
    }
}
