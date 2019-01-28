package ar.com.shipcommand.ui;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.main.Game;
import ar.com.shipcommand.main.MainWindow;
import ar.com.shipcommand.physics.geo.Geo2DPosition;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.awt.*;
import java.io.IOException;

public class Map implements IRenderable {
    NetcdfFile file;
    Variable z;

    private Geo2DPosition upperLeft;
    private Geo2DPosition lowerRight;

    private static int WIDTH = 21601;
    private static int HEIGHT = 10801;

    public Map() throws IOException {
        file = NetcdfFile.open("resources/GRIDONE_1D.nc");
        z = file.findVariable("z");

        upperLeft = new Geo2DPosition(90, -180);
        lowerRight = new Geo2DPosition(-90, 180);
    }

    /*
     * 0 ----------------- 21601
     * |                     |
     * |                     |
     * |                     |
     * 10801-----------------+
     */

    public void render(Graphics2D graphics, double dt) {
        MainWindow win = Game.getMainWindow();

        int winWidth = win.getWidth();
        int winHeight = win.getHeight();

        double areaWidth = lowerRight.getLat() - upperLeft.getLat();
        double areaHeight = lowerRight.getLon() - upperLeft.getLon();

        double latPerPixel = areaWidth / winWidth;
        double lonPerPixel = areaHeight / winHeight;


        /*
        for (int y = 0; y < HEIGHT; y++) {
            long start = WIDTH * (y * (10801 / HEIGHT));
            long end = start + 21601;

            Array read = z.read(start + ":" + end + ":" + (21601 / WIDTH));

            for (int x = 0; x < read.getSize(); x++) {
                int depth = read.getInt(x);

                if (depth < 0) {
                    Double blue = ((-1 * depth) / 11000.0) * 255;
                    int c = blue.intValue();

                    Color color = new Color(0, 0, 255 - c);
                    g.setColor(color);
                } else {
                    if (depth > 2000) {
                        Double gray = ((depth - 2000) / 7000.0) * 255;
                        int c = gray.intValue();

                        Color color = new Color(c, c, c);
                        g.setColor(color);
                    } else {
                        Double green = (depth / 2000.0) * 180;
                        int c = green.intValue();

                        Color color = new Color(0, 255 - c, 0);
                        g.setColor(color);
                    }
                }

                g.fillRect(x, y, 1, 1);
            }
        }
*/
    }
}
