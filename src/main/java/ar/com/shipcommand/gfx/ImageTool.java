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

    /**
     * Resize the specified buffered image
     *
     * @param img Image to resize
     * @param newW New width
     * @param newH New height
     * @return Resized image
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage newImg = new BufferedImage(newW, newH, img.getType());

        Graphics2D g = newImg.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);

        g.dispose();
        return newImg;
    }

}
