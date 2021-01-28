package ar.com.shipcommand.ui.map;

import ar.com.shipcommand.geo.*;
import ar.com.shipcommand.input.*;
import lombok.*;

import java.awt.*;

class MapResizingBox {
    @Getter
    private boolean resizing;
    private int resizeStartPosX;
    private int resizeStartPosY;

    private int width;
    private int height;

    private final double widthHeightRatio;

    private final Map map;

    MapResizingBox(Map map) {
        this.map = map;
        this.widthHeightRatio = (double) map.getHeight() / (double) map.getWidth();
    }

    /**
     * Given a width returns the map height by applying the width to height ratio
     * @return Height of the map determined by it's ratio
     */
    private int getHeightFromWidth(int width) {
        return (int) Math.round(width * widthHeightRatio);
    }

    /**
     * Given a height returns the map width by applying the width to height ratio
     * @return Width of the map determined by it's ratio
     */
    private int getWidthFromHeight(int height) {
        return (int) Math.round(height / widthHeightRatio);
    }

    /**
     * Draws the resizing rectangle
     * @param graphics Graphics object
     * @param width resizing rectangle width
     * @param height resizing rectangle height
     */
    private void drawRectangle(Graphics2D graphics, int width, int height) {
        graphics.setColor(Color.yellow);
        graphics.drawRect(resizeStartPosX, resizeStartPosY, width, height);
    }

    /**
     * Draw a rectangle when resizing
     * @param graphics Graphics object for drawing
     */
    public void drawResizeRectangle(Graphics2D graphics) {
        width = MouseHandler.getCurrentX() - resizeStartPosX;
        height = getHeightFromWidth(width);

        // Limit resizing if mouse is moved below bottom
        if ((resizeStartPosY + height) > map.getHeight()) {
            height = map.getHeight() - resizeStartPosY;
            width = getWidthFromHeight(height);
        }

        // If the resizing rectangle has a valid width and height draw it
        if (width > 0 && height > 0) {
            drawRectangle(graphics, width, height);
        }
    }

    public void startResizing() {
        resizing = true;
        resizeStartPosX = MouseHandler.getCurrentX();
        resizeStartPosY = MouseHandler.getCurrentY();
    }

    public void stopResizing() {
        resizing = false;

        if (resizeStartPosX < MouseHandler.getCurrentX()) {
            int x = resizeStartPosX;
            int y = resizeStartPosY;

            Geo2DPosition upperLeft = screenToPos(x, y);
            Geo2DPosition lowerRight = screenToPos(x + width, y + height);

            map.pushArea(upperLeft, lowerRight);
        }
    }

    /**
     * Converts the given screen position to geographical position on the map
     * @param x X position on the screen
     * @param y Y position on the screen
     * @return Geographical position of corresponding point in the screen
     */
    private Geo2DPosition screenToPos(int x, int y) {
        MapZoom zoom = map.getCurrentZoomConfig();

        double lat = zoom.getUpperLeft().getLat() - (y * zoom.getLatPerPixel());
        double lon = (x * zoom.getLonPerPixel()) + zoom.getUpperLeft().getLon();

        return new Geo2DPosition(lat, lon);
    }
}
