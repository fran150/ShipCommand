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
    private double[] heights = null;


    private Geo2DPosition upperLeft;
    private Geo2DPosition lowerRight;

    private static long GRID_WIDTH = 21601;
    private static long GRID_HEIGHT = 10801;

    public Map() throws IOException {
        file = NetcdfFile.open("./src/main/resources/GRIDONE_1D.nc");
        z = file.findVariable("z");

        MainWindow win = Game.getMainWindow();

        // World
/**/
        upperLeft = new Geo2DPosition(-90, -180);
        lowerRight = new Geo2DPosition(90, 180);
/**/
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

    /*
newR = currentR + (255 - currentR) * tint_factor
newG = currentG + (255 - currentG) * tint_factor
newB = currentB + (255 - currentB) * tint_factor
     */

    protected Color tint(double r, double g, double b, double factor) {
        Double red = new Double(r + (255 - r) * factor);
        Double green = new Double(g + (255 - g) * factor);
        Double blue = new Double(b + (255 - b) * factor);;
        return new Color(red.intValue(), green.intValue(), blue.intValue());
    }

    protected Color shade(double r, double g, double b, double factor) {
        Double red = new Double(r * (1 - factor));
        Double green = new Double(g * (1 - factor));
        Double blue = new Double(b * (1 - factor));
        return new Color(red.intValue(), green.intValue(), blue.intValue());
    }

    protected Color getColor(double depth) {
        double factor = 0;
        double r, g, b;
        double maxFactor;

        if (depth < 0) {
            r = 0; g = 0; b = 255;
            factor = (-1 * depth) / 11000.0;
            return shade(r, g, b, factor);
        } else {
            if (depth > 1300) {
                r = 205; g = 133; b = 63;
                factor = ((depth - 1300) / 7000.0);
                return shade(r, g, b, factor);
            } else if (depth > 500 && depth <= 1300) {
                r = 255; g = 255; b = 153;
                factor = ((depth - 500) / 1800.0);
                return shade(r, g, b, factor);
            } else {
                r = 0; g = 156; b = 76;
                factor = (depth / 1000.0);
                return tint(r, g, b, factor);
            }

        }
    }

    public void render(Graphics2D graphics, double dt) {
        MainWindow win = Game.getMainWindow();

        int winWidth = win.getWidth();
        int winHeight = winWidth / 2;

        if (heights != null) {
            for (int y = 0; y < winWidth; y++) {
                for (int x = 0; x < winHeight; x++) {
                    graphics.setColor(getColor(heights[x * y]));
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        } else {
            //heights = new double[winWidth * winHeight];

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

                long step = Math.round(lonPerPixel * 60);

                Array read = null;

                try {
                    read = z.read(start + ":" + end + ":" + step);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidRangeException e) {
                    e.printStackTrace();
                }

                for (int x = 0; x < read.getSize() && x < win.getWidth(); x++) {
                    int depth = read.getInt(x);
                    graphics.setColor(getColor(depth));
                    graphics.fillRect(x, y, 1, 1);
              //      heights[x * y] = depth;
                }

                double newLat = currentLeft.getLat() + latPerPixel;

                currentLeft.setLat(newLat);
                currentRight.setLat(newLat);
            }
        }
    }
}
