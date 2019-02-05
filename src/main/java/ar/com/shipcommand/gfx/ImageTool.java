package ar.com.shipcommand.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Image tools
 */
public class ImageTool {
    /**
     * Graphics default configuration
     */
    private static GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

    /**
     * Creates a new hardware accelerated image
     *
     * @param width Image width in pixels
     * @param height Image height in pixels
     * @param alpha Alpha enabled
     * @return Created image
     */
    public static BufferedImage createHardwareAccelerated(final int width, final int height,
                                      final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha
                ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }
}
