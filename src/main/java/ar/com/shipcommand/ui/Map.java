package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.MainWindow;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.awt.*;
import java.io.IOException;

public class Map implements IRenderable {
    NetcdfFile file;
    Variable z;
    Array read = null;

    private Geo2DPosition upperLeft;
    private Geo2DPosition lowerRight;

    private static long GRID_WIDTH = 21601;
    private static long GRID_HEIGHT = 10801;

    public Map() throws IOException {
        file = NetcdfFile.open("./src/main/resources/GRIDONE_1D.nc");
        z = file.findVariable("z");

        // World
        upperLeft = new Geo2DPosition(-90, -180);
        lowerRight = new Geo2DPosition(90, 180);

        // Argentina
        /*
        upperLeft = new Geo2DPosition(5, -105);
        lowerRight = new Geo2DPosition(63, -25);
        */
    }

    /*
     * 0 ----------------- 21601
     * |                     |
     * |                     |
     * |                     |
     * 10801-----------------+
     */

    public long posToGrid(Geo2DPosition position) {
        return Math.round((Math.round((position.getLat() + 90) * 60) * GRID_WIDTH) + ((position.getLon() + 180) * 60));
    }

    public void render(Graphics2D graphics, double dt) {
        MainWindow win = Game.getMainWindow();

        int winWidth = win.getWidth();
        int winHeight = win.getHeight();

        double areaWidth = lowerRight.getLon() - upperLeft.getLon();
        double areaHeight = lowerRight.getLat() - upperLeft.getLat();

        double lonPerPixel = areaWidth / winWidth;
        double latPerPixel = areaHeight / winHeight;

        Geo2DPosition currentLeft = upperLeft.clone();

        Geo2DPosition currentRight = currentLeft.clone();
        currentRight.setLon(currentRight.getLon() + areaWidth);

        for (int y = 0; y < winHeight; y++) {
            long start = posToGrid(currentLeft);
            long end = posToGrid(currentRight);

            try {
                long step = Math.round(lonPerPixel * 60);
                read = z.read(start + ":" + end + ":" + step);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidRangeException e) {
                e.printStackTrace();
            }

            for (int x = 0; x < read.getSize(); x++) {
                int depth = read.getInt(x);

                if (depth < 0) {
                    Double blue = ((-1 * depth) / 11000.0) * 255;
                    int c = blue.intValue();

                    Color color = new Color(0, 0, 255 - c);
                    graphics.setColor(color);
                } else {
                    if (depth > 2000) {
                        Double gray = ((depth - 2000) / 7000.0) * 255;
                        int c = gray.intValue();

                        Color color = new Color(c, c, c);
                        graphics.setColor(color);
                    } else {
                        Double green = (depth / 2000.0) * 180;
                        int c = green.intValue();

                        Color color = new Color(0, 255 - c, 0);
                        graphics.setColor(color);
                    }
                }

                graphics.fillRect(x, y, 1, 1);
            }

            double newLat = currentLeft.getLat() + latPerPixel;

            currentLeft.setLat(newLat);
            currentRight.setLat(newLat);
        }
    }
}
