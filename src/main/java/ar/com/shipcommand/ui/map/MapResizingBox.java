package ar.com.shipcommand.ui.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class MapResizingBox {
    private boolean resizing;
    private int resizeStartPosX;
    private int resizeStartPosY;

    private int width;
    private int height;
}
